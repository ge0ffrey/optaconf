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

    private static final Logger LOG = LoggerFactory
            .getLogger(DevoxxImporter.class);

    @PersistenceContext(unitName = "optaconf-webapp-persistence-unit")
    private EntityManager em;

     private static final String REST_URL_ROOT = "http://cfp.devoxx.be/api/conferences/DV15";
//    private static final String REST_URL_ROOT = "http://cfp.devoxx.fr/api/conferences/DevoxxFR2015";

    public Conference importConference(boolean persist) {
        StringBuilder comment = new StringBuilder("Imported on ").append(new Date().toString()).append(" from ")
                .append(REST_URL_ROOT);

        Conference conference = new Conference();
        conference.setName("DEVOXX BE 2016");
//        conference.setName("DEVOXX FR 2015");
        conference.setComment(comment.toString());

        Map<String, Track> titleToTrackMap = mapTracks(conference, persist);
        Map<String, Speaker> speakerMap = mapSpeakers(conference, persist);
        Map<String, Room> roomMap = mapRooms(conference, persist);
        mapDays(conference, titleToTrackMap, speakerMap, roomMap, persist);
        buildUnavailableTimeslotRoomPenaltyList(conference, persist);
        if (persist) {
            em.persist(conference);
        }

        return conference;
    }

    private Map<String, Track> mapTracks(Conference conference, boolean persist) {
        Map<String, Track> titleToTrackMap = new LinkedHashMap<String, Track>();
        JsonObject rootObject = readJsonObject(REST_URL_ROOT + "/tracks");
        JsonArray array = rootObject.getJsonArray("tracks");
        TangoCssFactory tangoColorFactory = new TangoCssFactory();
        for (int i = 0; i < array.size(); i++) {
            JsonObject dTrack = array.getJsonObject(i);
            String id = dTrack.getString("id");
            String title = dTrack.getString("title");
            String colorHex = tangoColorFactory.pickCssClass(id);
            Track track = new Track(conference, id, title, colorHex);
            if (persist) {
                em.persist(track);
            }

            conference.getTrackList().add(track);
            titleToTrackMap.put(title, track);
        }
        return titleToTrackMap;
    }

    private Map<String, Speaker> mapSpeakers(Conference conference, boolean persist) {
        Map<String, Speaker> speakerMap = new LinkedHashMap<String, Speaker>();
        JsonArray array = readJsonArray(REST_URL_ROOT + "/speakers");
        for (int i = 0; i < array.size(); i++) {
            JsonObject dSpeaker = array.getJsonObject(i);
            String id = dSpeaker.getString("uuid");
            String firstName = dSpeaker.getString("firstName");
            String lastName = dSpeaker.getString("lastName");
            String name = firstName + " " + lastName;
            Speaker speaker = new Speaker(conference, id, name);

            if (persist) {
                em.persist(speaker);
            }
            conference.getSpeakerList().add(speaker);
            speakerMap.put(id, speaker);
        }
        Collections.sort(conference.getSpeakerList());
        return speakerMap;
    }

    private Map<String, Room> mapRooms(Conference conference, boolean persist) {
        Map<String, Room> roomMap = new LinkedHashMap<String, Room>();
        JsonObject rootObject = readJsonObject(REST_URL_ROOT + "/rooms");
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
            Room room = new Room(conference, id, name, capacity);
            if (persist) {
                em.persist(room);
            }
            conference.getRoomList().add(room);
            roomMap.put(id, room);
        }
        Collections.sort(conference.getRoomList());

        return roomMap;
    }

    private void mapDays(Conference conference, Map<String, Track> titleToTrackMap,
            Map<String, Speaker> speakerMap, Map<String, Room> roomMap, boolean persist) {
        JsonObject rootObject = readJsonObject(REST_URL_ROOT + "/schedules");
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
            Day day = new Day(conference, dHref.replaceAll(".*\\/(.*)/", "$1"), name, date);
            if (persist) {
                em.persist(day);
            }
            conference.getDayList().add(day);
            mapTalks(conference, titleToTrackMap, speakerMap, roomMap, dHref, day, persist);
        }
        Collections.sort(conference.getDayList());
    }

    private void mapTalks(Conference conference,
            Map<String, Track> titleToTrackMap,
            Map<String, Speaker> speakerMap, Map<String, Room> roomMap,
            String dayUrl, Day day, boolean persist) {
        Map<String, Timeslot> timeslotMap = new LinkedHashMap<String, Timeslot>();
        JsonObject rootObject = readJsonObject(dayUrl);
        JsonArray array = rootObject.getJsonArray("slots");
        for (int i = 0; i < array.size(); i++) {
            JsonObject dSlot = array.getJsonObject(i);
            if (dSlot.isNull("talk")) {
                continue;
            }
            JsonObject dTalk = dSlot.getJsonObject("talk");
            // TODO Add hands on etc too
            if (!dTalk.getString("talkType").equalsIgnoreCase("Conference")) {
                continue;
            }
            String trackTitle = dTalk.getString("track");
            if (trackTitle.equalsIgnoreCase("Startups")) {
                // Workaround to a bug in the Devoxx REST API, because
                // "Startups" doesn't exist as a track id or title
                trackTitle = "Startup and entrepreneurship";
            }
            if (trackTitle
                    .equalsIgnoreCase("Architecture, Performance and Security")) {
                // Workaround to a bug in the Devoxx REST API
                trackTitle = "Architecture, Performance & Security";
            }
            if (trackTitle.equalsIgnoreCase("Cloud & DevOps")) {
                // Workaround to a bug in the Devoxx REST API
                trackTitle = "Cloud, DevOps and Tools";
            }
            if (trackTitle.equalsIgnoreCase("Web, Mobile & UX")) {
                // Workaround to a bug in the Devoxx REST API
                trackTitle = "Web, Mobile &  UX";
            }
            if (trackTitle.equalsIgnoreCase("Agility, Methodology & Tests")) {
                // Workaround to a bug in the Devoxx REST API
                trackTitle = "Agility, Methodology & Test";
            }
            String id = dTalk.getString("id");
            String title = dTalk.getString("title");
            Track track = titleToTrackMap.get(trackTitle);
            if (track == null) {
                throw new IllegalArgumentException("The trackTitle ("
                        + trackTitle + ") is not part of the titleToTrackMap ("
                        + titleToTrackMap + ").");
            }

            String roomId = dSlot.getString("roomId");
            Room room = roomMap.get(roomId);

            String fromTime = dSlot.getString("fromTime");
            String toTime = dSlot.getString("toTime");

            String timeslotId = fromTime + " - " + toTime;

            Timeslot timeslot = timeslotMap.get(timeslotId);
            if (timeslot == null) {
                timeslot = new Timeslot(conference, timeslotId, timeslotId, day, fromTime,
                        toTime);

                if (persist) {
                    em.persist(timeslot);
                }
                conference.getTimeslotList().add(timeslot);
                timeslotMap.put(timeslotId, timeslot);
            }

            Talk talk = new Talk(conference, id, title, room, track, timeslot);

            if (persist) {
                em.persist(talk);
            }

            conference.getTalkList().add(talk);

            JsonArray speakersArray = dTalk.getJsonArray("speakers");
            for (int j = 0; j < speakersArray.size(); j++) {
                JsonObject dSpeaker = speakersArray.getJsonObject(j);
                String speakerId = dSpeaker.getJsonObject("link")
                        .getString("href").replaceAll(".*/", "");
                Speaker speaker = speakerMap.get(speakerId);
                if (speaker == null) {
                    LOG.warn(
                            "Ignoring speaking relation for speaker ({}) to talk ({}) because the speaker doesn't exist in the speaker list.",
                            dSpeaker.getString("name"), talk.getTitle());
                    continue;
                    // throw new IllegalArgumentException("The speakerId (" +
                    // speakerId
                    // + ") is not part of the speakerMap (" + speakerMap +
                    // ").");
                }
                SpeakingRelation speakingRelation = new SpeakingRelation(
                        conference, talk.getExternalId() + "_" + speaker.getExternalId(), talk, speaker);
                conference.getSpeakingRelationList().add(speakingRelation);
            }

        }
        Collections.sort(conference.getTimeslotList());
    }

    private void buildUnavailableTimeslotRoomPenaltyList(Conference conference, boolean persist) {
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
                            conference, timeslot.getExternalId() + "_" + room.getExternalId(), timeslot,
                            room);
                    if (persist) {
                        em.persist(penalty);
                    }
                    conference.getUnavailableTimeslotRoomPenaltyList().add(
                            penalty);
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
