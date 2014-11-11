package org.optaconf.bridge.devoxx;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;

import org.apache.commons.io.IOUtils;
import org.optaconf.domain.Day;
import org.optaconf.domain.Room;
import org.optaconf.domain.Schedule;
import org.optaconf.domain.Talk;
import org.optaconf.domain.TalkExclusion;
import org.optaconf.domain.Timeslot;

@ApplicationScoped
public class DevoxxImporter {

    private static final String REST_URL_ROOT = "http://cfp.devoxx.be/api/conferences/DevoxxBe2014";

    public Schedule importSchedule() {
        Schedule schedule = new Schedule();
        schedule.setDayList(new ArrayList<Day>());
        schedule.setTimeslotList(new ArrayList<Timeslot>());
        schedule.setRoomList(new ArrayList<Room>());
        schedule.setTalkList(new ArrayList<Talk>());
        schedule.setTalkExclusionList(new ArrayList<TalkExclusion>());
        Map<String, Room> roomMap = mapRooms(schedule);
        mapDays(schedule, roomMap);
        return schedule;
    }

    private Map<String, Room> mapRooms(Schedule schedule) {
        Map<String, Room> roomMap = new HashMap<String, Room>();
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
            Room room = new Room(id, name, capacity);
            schedule.getRoomList().add(room);
            roomMap.put(id, room);
        }
        return roomMap;
    }

    private void mapDays(Schedule schedule, Map<String, Room> roomMap) {
        JsonObject rootObject = readJsonObject(REST_URL_ROOT + "/schedules");
        JsonArray array = rootObject.getJsonArray("links");
        Pattern dTitlePattern = Pattern.compile("Schedule for (\\w+) (\\d+.*\\d{4})");
        for (int i = 0; i < array.size(); i++) {
            JsonObject dDay = array.getJsonObject(i);
            String dHref = dDay.getString("href");
            String dTitle = dDay.getString("title");
            Matcher dTitleMatcher = dTitlePattern.matcher(dTitle);
            if (!dTitleMatcher.find() && dTitleMatcher.groupCount() != 2) {
                throw new IllegalStateException("A schedules title (" + dTitle
                        + ") does not match the pattern (" + dTitlePattern.pattern() + ").");
            }
            String name = dTitleMatcher.group(1);
            String date = dTitleMatcher.group(2);
            Day day = new Day(dHref.replaceAll(".*\\/(.*)/", "$1"), name, date);
            schedule.getDayList().add(day);
            mapTalks(schedule, roomMap, dHref, day);
        }
    }

    private void mapTalks(Schedule schedule, Map<String, Room> roomMap, String dayUrl, Day day) {
        Map<String, Timeslot> timeslotMap = new HashMap<String, Timeslot>();
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
            String id = dTalk.getString("id");
            String title = dTalk.getString("title");
            Talk talk = new Talk(id, title);
            schedule.getTalkList().add(talk);

            String roomId = dSlot.getString("roomId");
            Room room = roomMap.get(roomId);
            talk.setRoom(room);

            String fromTime = dSlot.getString("fromTime");
            String toTime = dSlot.getString("toTime");
            String timeslotId = fromTime + " - " + toTime;
            Timeslot timeslot = timeslotMap.get(timeslotId);
            if (timeslot == null) {
                timeslot = new Timeslot(timeslotId, timeslotId, day, fromTime, toTime);
                schedule.getTimeslotList().add(timeslot);
                timeslotMap.put(timeslotId, timeslot);
            }
            talk.setTimeslot(timeslot);
        }
    }

    private JsonObject readJsonObject(String url) {
        InputStream schedulesIn = null;
        try {
            schedulesIn = new URL(url).openConnection().getInputStream();
            JsonReader jsonReader = Json.createReader(schedulesIn);
            return jsonReader.readObject();
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Import from Devoxx CFP failed on URL (" + url + ").", e);
        } catch (IOException e) {
            throw new IllegalStateException("Import from Devoxx CFP failed on URL (" + url + ").", e);
        } finally {
            IOUtils.closeQuietly(schedulesIn);
        }
    }

}
