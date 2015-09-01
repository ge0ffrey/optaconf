package org.optaconf.bridge.optaplanner;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.optaconf.domain.Conference;
import org.optaconf.domain.Day;
import org.optaconf.domain.Room;
import org.optaconf.domain.Speaker;
import org.optaconf.domain.SpeakingRelation;
import org.optaconf.domain.Talk;
import org.optaconf.domain.Timeslot;
import org.optaconf.domain.Track;
import org.optaconf.domain.UnavailableTimeslotRoomPenalty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@LocalBean
public class JpaConferenceExporter
{

   @PersistenceContext(unitName = "optaconf-webapp-persistence-unit")
   private EntityManager em;

   Conference entity;

   @Inject
   private UserTransaction utx;

   private static final Logger LOG = LoggerFactory
            .getLogger(JpaConferenceExporter.class);

   public Conference exportSchedule(Long conferenceId)
   {
      StringBuilder comment = new StringBuilder("Exported on ").append(new Date().toString())
               .append(" from Conference ID: ")
               .append(conferenceId);
      
      LOG.info(comment.toString());

      entity = em.find(Conference.class, conferenceId);

      Conference conference = new Conference();
      conference.setName(entity.getName());
      conference.setComment(comment.toString());
      conference.setExternalId(entity.getExternalId());
      conference.setId(entity.getId());

      Map<String, Track> titleToTrackMap = mapTracks(conference);
      Map<String, Speaker> speakerMap = mapSpeakers(conference);
      Map<String, Room> roomMap = mapRooms(conference);
      mapDays(conference, titleToTrackMap, speakerMap, roomMap);
      buildUnavailableTimeslotRoomPenalties(conference);
      return conference;
   }

   private Map<String, Track> mapTracks(Conference conference)
   {
      Map<String, Track> titleToTrackMap = new LinkedHashMap<String, Track>();

      List<Track> childEntity = entity.getTrackList();

      for (Track track : childEntity) {

         Track t = new Track(track.getExternalId(), track.getTitle(), track.getCssStyleClass(), conference);
         conference.getTrackList().add(t);
         titleToTrackMap.put(track.getTitle(), t);
      }
      return titleToTrackMap;
   }

   private Map<String, Speaker> mapSpeakers(Conference conference)
   {
      Map<String, Speaker> speakerMap = new LinkedHashMap<String, Speaker>();

      List<Speaker> speakers = entity.getSpeakerList();

      for (Speaker s : speakers) {
         Speaker speaker = new Speaker(s.getExternalId(), s.getName(), conference);
         conference.getSpeakerList().add(speaker);
         speakerMap.put(s.getExternalId(), speaker);
      }
      Collections.sort(conference.getSpeakerList());
      return speakerMap;
   }

   private Map<String, Room> mapRooms(Conference conference)
   {
      Map<String, Room> roomMap = new LinkedHashMap<String, Room>();

      List<Room> rooms = entity.getRoomList();

      for (Room r : rooms) {
         Room room = new Room(r.getExternalId(), r.getName(), r.getSeatingCapacity(), conference);
         conference.getRoomList().add(room);
         roomMap.put(r.getExternalId(), room);
      }
      Collections.sort(conference.getRoomList());

      return roomMap;
   }

   private void mapDays(Conference conference, Map<String, Track> titleToTrackMap,
            Map<String, Speaker> speakerMap, Map<String, Room> roomMap)
   {
      List<Day> days = entity.getDayList();

      for (Day d : days) {
         Day day = new Day(d.getExternalId(), d.getName(), d.getDate(), conference);
         conference.getDayList().add(day);
         mapTalks(conference, titleToTrackMap, speakerMap, roomMap, day);
      }
      Collections.sort(conference.getDayList());
   }

   private void mapTalks(Conference conference, Map<String, Track> titleToTrackMap, Map<String, Speaker> speakerMap,
            Map<String, Room> roomMap, Day day)
   {
      Map<String, Timeslot> timeslotMap = new LinkedHashMap<String, Timeslot>();

      List<Timeslot> timeslots = day.getTimeslots();

      for (Timeslot t : timeslots) {
         
         Timeslot timeslot = timeslotMap.get(t.getExternalId());
         if (timeslot == null) {
            timeslot = new Timeslot(t.getExternalId(), t.getName(), day, t.getFromTime(),
                     t.getToTime(), conference);

            conference.getTimeslotList().add(timeslot);
            timeslotMap.put(t.getExternalId(), timeslot);
         }

         List<Talk> talks = t.getTalks();

         for (Talk tk : talks) {
            
            Room room = roomMap.get(tk.getRoom().getExternalId());

            Talk talk = new Talk(tk.getExternalId(), tk.getTitle(), conference, room, tk.getTrack(), timeslot);
            conference.getTalkList().add(talk);
            
            Speaker speaker = speakerMap.get(tk.getSpeakingRelation().getSpeaker().getExternalId());
            SpeakingRelation speakingRelation = new SpeakingRelation(
                     talk.getExternalId() + "_" + speaker.getExternalId(), talk, speaker, conference);
            conference.getSpeakingRelationList().add(speakingRelation);

         }

      }
      Collections.sort(conference.getTimeslotList());
   }

   private void buildUnavailableTimeslotRoomPenalties(Conference conference)
   {
      Map<Timeslot, Map<Room, Talk>> timeslotRoomToTalkMap = new LinkedHashMap<>();
      for (Talk talk : conference.getTalkList()) {
         Timeslot timeslot = talk.getTimeslot();
         Room room = talk.getRoom();
         if (timeslot == null || room == null) {
            continue;
         }
         Map<Room, Talk> roomToTalkMap = timeslotRoomToTalkMap.get(timeslot);
         if (roomToTalkMap == null) {
            roomToTalkMap = new LinkedHashMap<>();
            timeslotRoomToTalkMap.put(timeslot, roomToTalkMap);
         }
         roomToTalkMap.put(room, talk);
      }
      for (Timeslot timeslot : conference.getTimeslotList()) {
         Map<Room, Talk> roomToTalkMap = timeslotRoomToTalkMap.get(timeslot);
         for (Room room : conference.getRoomList()) {
            if (roomToTalkMap == null || !roomToTalkMap.containsKey(room)) {
               UnavailableTimeslotRoomPenalty penalty = new UnavailableTimeslotRoomPenalty(
                        timeslot.getExternalId() + "_" + room.getExternalId(), timeslot,
                        room, conference);
               conference.getUnavailableTimeslotRoomPenaltyList().add(
                        penalty);
            }
         }
      }
   }

}
