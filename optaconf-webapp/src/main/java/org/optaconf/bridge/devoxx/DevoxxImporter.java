package org.optaconf.bridge.devoxx;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.LocalBean;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.io.IOUtils;
import org.optaconf.domain.Conference;
import org.optaconf.domain.ConferenceParametrization;
import org.optaconf.domain.Day;
import org.optaconf.domain.Room;
import org.optaconf.domain.Speaker;
import org.optaconf.domain.SpeakingRelation;
import org.optaconf.domain.Talk;
import org.optaconf.domain.Timeslot;
import org.optaconf.domain.Track;
import org.optaconf.domain.UnavailableTimeslotRoomPenalty;
import org.optaconf.util.TangoCssFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@LocalBean
public class DevoxxImporter {

    private static final Logger LOG = LoggerFactory.getLogger(DevoxxImporter.class);

    @PersistenceContext(unitName = "optaconf-webapp-persistence-unit")
    private EntityManager em;

     private static final String REST_URL_ROOT = "http://cfp.devoxx.be/api/conferences/DV16";
//    private static final String REST_URL_ROOT = "http://cfp.devoxx.fr/api/conferences/DevoxxFR2015";

    private static class ImportData {
        protected Conference conference;
        protected Map<String, Track> trackMap;
        protected Map<String, Speaker> speakerMap;
        protected Map<String, Speaker> fullNameToSpeakerMap;
        protected Map<String, Talk> talkMap;
        protected Map<String, Room> roomMap;
        protected Map<String, Timeslot> timeslotMap;
    }

    public Conference importConference(boolean persist) {
        ImportData importData = new ImportData();
        Conference conference = new Conference();
        conference.setName("DEVOXX BE 2016");
//        conference.setName("DEVOXX FR 2015");
        conference.setComment("Imported on " + new Date().toString() + " from " + REST_URL_ROOT);
        conference.setConferenceParametrization(ConferenceParametrization.createDefault(conference));
        importData.conference = conference;
        mapTracks(importData, persist);
        mapSpeakers(importData, persist);
        mapTalks(importData, persist);
        mapRooms(importData, persist);
        mapDays(importData, persist);
        buildUnavailableTimeslotRoomPenaltyList(importData, persist);
        if (persist) {
            em.persist(conference);
        }

        return conference;
    }

    private void mapTracks(ImportData importData, boolean persist) {
        importData.trackMap = new LinkedHashMap<String, Track>();
        JsonObject rootObject = readJsonObject(REST_URL_ROOT + "/tracks");
        JsonArray array = rootObject.getJsonArray("tracks");
        TangoCssFactory tangoColorFactory = new TangoCssFactory();
        for (int i = 0; i < array.size(); i++) {
            JsonObject dTrack = array.getJsonObject(i);
            String id = dTrack.getString("id");
            String title = dTrack.getString("title");
            String colorHex = tangoColorFactory.pickCssClass(id);
            Track track = new Track(importData.conference, id, title, colorHex);
            if (persist) {
                em.persist(track);
            }

            importData.conference.getTrackList().add(track);
            importData.trackMap.put(id, track);
        }
    }

    private void mapSpeakers(ImportData importData, boolean persist) {
        importData.speakerMap = new LinkedHashMap<String, Speaker>();
        importData.fullNameToSpeakerMap = new LinkedHashMap<String, Speaker>();
        JsonArray array = readJsonArray(REST_URL_ROOT + "/speakers/");
        for (int i = 0; i < array.size(); i++) {
            JsonObject dSpeaker = array.getJsonObject(i);
            String id = dSpeaker.getString("uuid");
            String firstName = dSpeaker.getString("firstName");
            String lastName = dSpeaker.getString("lastName");
            String fullName = firstName + " " + lastName;
            Speaker speaker = new Speaker(importData.conference, id, fullName);
            if (persist) {
                em.persist(speaker);
            }
            importData.conference.getSpeakerList().add(speaker);
            importData.speakerMap.put(id, speaker);
            importData.fullNameToSpeakerMap.put(fullName, speaker);
        }
        Collections.sort(importData.conference.getSpeakerList());
    }

    private void mapTalks(ImportData importData, boolean persist) {
        importData.talkMap = new LinkedHashMap<String, Talk>();
        JsonObject rootObject = readJsonObject(REST_URL_ROOT + "/talks/");
        JsonObject subRootObject = rootObject.getJsonObject("talks");
        JsonArray approvedArray = subRootObject.getJsonArray("approved");
        JsonArray acceptedArray = subRootObject.getJsonArray("accepted");

        for (int i = 0; i < approvedArray.size() + acceptedArray.size(); i++) {
            JsonObject dTalk = i < approvedArray.size() ? approvedArray.getJsonObject(i)
                    : acceptedArray.getJsonObject(i - approvedArray.size());
            // TODO Add hands on etc too
            JsonObject dTalkType = dTalk.getJsonObject("talkType");
            if (!dTalkType.getString("id").equalsIgnoreCase("conf")) {
                continue;
            }
            String id = dTalk.getString("id");
            String title = dTalk.getString("title");
            JsonObject dTrack = dTalk.getJsonObject("track");
            String trackId = dTrack.getString("id");
            Track track = importData.trackMap.get(trackId);
            if (track == null) {
                throw new IllegalArgumentException("The trackId (" + trackId
                        + ") is not part of the trackMap (" + importData.trackMap + ").");
            }

            Talk talk = new Talk(importData.conference, id, title, track);
            if (persist) {
                em.persist(talk);
            }
            importData.conference.getTalkList().add(talk);
            importData.talkMap.put(id, talk);

            String mainSpeaker = dTalk.getString("mainSpeaker");
            addSpeakerRelation(importData, talk, mainSpeaker);
            String secondarySpeaker = dTalk.getString("secondarySpeaker", null);
            if (secondarySpeaker != null) {
                addSpeakerRelation(importData, talk, secondarySpeaker);
            }
            JsonArray otherSpeakersArray = dTalk.getJsonArray("otherSpeakers");
            for (int j = 0; j < otherSpeakersArray.size(); j++) {
                String otherSpeaker = otherSpeakersArray.getString(j);
                addSpeakerRelation(importData, talk, otherSpeaker);
            }
        }
    }

    private void addSpeakerRelation(ImportData importData, Talk talk, String speakerFullName) {
//        String speakerId = speakerFullName.getJsonObject("link").getString("href").replaceAll(".*/", "");
//        Speaker speaker = importData.speakerMap.get(speakerId);
        Speaker speaker = importData.fullNameToSpeakerMap.get(speakerFullName);
        if (speaker == null) {
            LOG.warn("Ignoring speaking relation for speaker ({}) to talk ({}) because the speaker doesn't exist in the speaker list.",
                    speakerFullName, talk.getTitle());
            return;
//            throw new IllegalArgumentException("The speakerId (" + speakerFullName
//                    + ") is not part of the fullNameToSpeakerMap (" + importData.fullNameToSpeakerMap + ").");
        }
        SpeakingRelation speakingRelation = new SpeakingRelation(
                importData.conference, talk.getExternalId() + "_" + speaker.getExternalId(), talk, speaker);
        importData.conference.getSpeakingRelationList().add(speakingRelation);
    }

    private void mapRooms(ImportData importData, boolean persist) {
        importData.roomMap = new LinkedHashMap<String, Room>();
        JsonObject rootObject = readJsonObject(REST_URL_ROOT + "/rooms/");
        JsonArray array = rootObject.getJsonArray("rooms");
        for (int i = 0; i < array.size(); i++) {
            JsonObject dRoom = array.getJsonObject(i);
            String id = dRoom.getString("id");
            String name = dRoom.getString("name");
            int capacity = dRoom.getInt("capacity");
            // TODO Add RoomType and store BOF rooms etc too
            if (!dRoom.getString("setup").equals("theatre")) {
                continue;
            }
            Room room = new Room(importData.conference, id, name, capacity);
            if (persist) {
                em.persist(room);
            }
            importData.conference.getRoomList().add(room);
            importData.roomMap.put(id, room);
        }
        Collections.sort(importData.conference.getRoomList());
    }

    private void mapDays(ImportData importData, boolean persist) {
        JsonObject rootObject = readJsonObject(REST_URL_ROOT + "/schedules/");
        JsonArray array = rootObject.getJsonArray("links");
        Pattern[] dTitlePatterns = {
                Pattern.compile("Schedule for (\\w+) (\\d+.*\\d{4})"), // Used in Devoxx FR 2015
                Pattern.compile("(\\w+), (\\d+.*\\d{4})") // Used in Devoxx BE 2015
        };
        for (int i = 0; i < array.size(); i++) {
            JsonObject dDay = array.getJsonObject(i);
            String dHref = dDay.getString("href");
            String dTitle = dDay.getString("title");
            String name = null;
            String date = null;
            for (Pattern dTitlePattern : dTitlePatterns) {
                Matcher dTitleMatcher = dTitlePattern.matcher(dTitle);
                if (dTitleMatcher.find() && dTitleMatcher.groupCount() == 2) {
                    name = dTitleMatcher.group(1);
                    date = dTitleMatcher.group(2);
                    break;
                }
            }
            if (name == null) {
                throw new IllegalStateException("A schedules title (" + dTitle
                        + ") does not match any of the patterns (" + Arrays.toString(dTitlePatterns) + ").");
            }
            Day day = new Day(importData.conference, dHref.replaceAll(".*\\/(.*)/", "$1"), name, date);
            if (persist) {
                em.persist(day);
            }
            importData.conference.getDayList().add(day);
            mapTalksToRoomsAndTimeslots(importData, dHref, day, persist);
        }
        Collections.sort(importData.conference.getDayList());
    }

    private void mapTalksToRoomsAndTimeslots(ImportData importData,
            String dayUrl, Day day, boolean persist) {
        importData.timeslotMap = new LinkedHashMap<String, Timeslot>();
        JsonObject rootObject = readJsonObject(dayUrl);
        JsonArray array = rootObject.getJsonArray("slots");
        for (int i = 0; i < array.size(); i++) {
            JsonObject dSlot = array.getJsonObject(i);
            if (dSlot.isNull("talk")) {
                continue;
            }
            JsonObject dTalk = dSlot.getJsonObject("talk");

            String roomId = dSlot.getString("roomId");
            Room room = importData.roomMap.get(roomId);

            String fromTime = dSlot.getString("fromTime");
            String toTime = dSlot.getString("toTime");

            String timeslotId = fromTime + " - " + toTime;

            Timeslot timeslot = importData.timeslotMap.get(timeslotId);
            if (timeslot == null) {
                timeslot = new Timeslot(importData.conference, timeslotId, timeslotId, day, fromTime,
                        toTime);

                if (persist) {
                    em.persist(timeslot);
                }
                importData.conference.getTimeslotList().add(timeslot);
                importData.timeslotMap.put(timeslotId, timeslot);
            }

            // TODO FIXME

//            Talk talk = new Talk(conference, id, title, room, track, timeslot);
//
//            if (persist) {
//                em.persist(talk);
//            }
//
//            conference.getTalkList().add(talk);
        }
        Collections.sort(importData.conference.getTimeslotList());
    }

    private void buildUnavailableTimeslotRoomPenaltyList(ImportData importData, boolean persist) {
        Map<Timeslot, Map<Room, Talk>> timeslotRoomToTalkMap = new LinkedHashMap<>();
        for (Talk talk : importData.conference.getTalkList()) {
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
        for (Timeslot timeslot : importData.conference.getTimeslotList()) {
            Map<Room, Talk> roomToTalkMap = timeslotRoomToTalkMap.get(timeslot);
            for (Room room : importData.conference.getRoomList()) {
                if (roomToTalkMap == null || !roomToTalkMap.containsKey(room)) {
                    UnavailableTimeslotRoomPenalty penalty = new UnavailableTimeslotRoomPenalty(
                            importData.conference, timeslot.getExternalId() + "_" + room.getExternalId(), timeslot,
                            room);
                    if (persist) {
                        em.persist(penalty);
                    }
                    importData.conference.getUnavailableTimeslotRoomPenaltyList().add(penalty);
                }
            }
        }
    }

    private JsonObject readJsonObject(String url) {
        InputStream schedulesIn = null;
        try {
            schedulesIn = new URL(url).openConnection().getInputStream();
            JsonReader jsonReader = Json.createReader(schedulesIn);
            return jsonReader.readObject();
        } catch (UnknownHostException e) {
            throw new IllegalStateException(
                    "Check your network connection. Import from Devoxx CFP failed on URL ("
                            + url + ").",
                    e);
        } catch (MalformedURLException e) {
            throw new IllegalStateException(
                    "Check the Devoxx CFP URL. Import from Devoxx CFP failed on URL ("
                            + url + ").",
                    e);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Import from Devoxx CFP failed on URL (" + url + ").", e);
        } finally {
            IOUtils.closeQuietly(schedulesIn);
        }
    }

    private JsonArray readJsonArray(String url) {
        InputStream schedulesIn = null;
        try {
            schedulesIn = new URL(url).openConnection().getInputStream();
            JsonReader jsonReader = Json.createReader(schedulesIn);
            return jsonReader.readArray();
        } catch (MalformedURLException e) {
            throw new IllegalStateException(
                    "Import from Devoxx CFP failed on URL (" + url + ").", e);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Import from Devoxx CFP failed on URL (" + url + ").", e);
        } finally {
            IOUtils.closeQuietly(schedulesIn);
        }
    }

}
