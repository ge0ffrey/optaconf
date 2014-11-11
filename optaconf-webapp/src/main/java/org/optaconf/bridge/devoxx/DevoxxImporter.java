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
import org.optaconf.domain.Schedule;
import org.optaconf.domain.Talk;

@ApplicationScoped
public class DevoxxImporter {

    private static final String REST_URL_ROOT = "http://cfp.devoxx.be/api/conferences/DevoxxBe2014";

    public Schedule importSchedule() {
        Schedule schedule = new Schedule();
        JsonObject dSchedules = readJsonObject(REST_URL_ROOT + "/schedules");
        JsonArray dDayArray = dSchedules.getJsonArray("links");
        List<Day> dayList = new ArrayList<Day>();
        Pattern dTitlePattern = Pattern.compile("Schedule for (\\w+) (\\d+.*\\d{4})");
        long dayId = 0L;
        for (int i = 0; i < dDayArray.size(); i++) {
            JsonObject dDay = dDayArray.getJsonObject(i);
            String dHref = dDay.getString("href");
            String dTitle = dDay.getString("title");
            Matcher dTitleMatcher = dTitlePattern.matcher(dTitle);
            if (!dTitleMatcher.find() && dTitleMatcher.groupCount() != 2) {
                throw new IllegalStateException("A schedules title (" + dTitle
                        + ") does not match the pattern (" + dTitlePattern.pattern() + ").");
            }
            String name = dTitleMatcher.group(1);
            String date = dTitleMatcher.group(2);
            dayList.add(new Day(dayId, name, date));
            dayId++;
        }
        schedule.setDayList(dayList);
        return schedule;
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
