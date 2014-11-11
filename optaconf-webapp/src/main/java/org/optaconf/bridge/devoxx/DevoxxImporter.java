package org.optaconf.bridge.devoxx;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
        mapRooms(schedule);
        mapDays(schedule);
        return schedule;
    }

    private void mapRooms(Schedule schedule) {
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
            schedule.getRoomList().add(new Room(id, name, capacity));
        }
    }

    private void mapDays(Schedule schedule) {
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
            schedule.getDayList().add(new Day(dHref.replaceAll(".*\\/(.*)/", "$1"), name, date));
            mapTalks(schedule, dHref);
        }
    }

    private void mapTalks(Schedule schedule, String dayUrl) {
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
            schedule.getTalkList().add(new Talk(id, title));
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
