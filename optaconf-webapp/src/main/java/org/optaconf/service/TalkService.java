package org.optaconf.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.optaconf.cdi.ScheduleManager;
import org.optaconf.domain.Day;
import org.optaconf.domain.Room;
import org.optaconf.domain.Schedule;
import org.optaconf.domain.Talk;
import org.optaconf.domain.TalkExclusion;
import org.optaconf.domain.Timeslot;

@Path("/{conferenceId}/talk")
public class TalkService {

    @Inject
    private ScheduleManager scheduleManager;

    @GET
    @Path("/")
    @Produces("application/json")
    public List<Talk> getTalkList(@PathParam("conferenceId") Long conferenceId) {
        Schedule schedule = scheduleManager.getSchedule();
        return schedule.getTalkList();
    }

    @GET
    @Path("/map")
    @Produces("application/json")
    public Map<String, Map<String, Map<String, Talk>>> getDayTimeslotRoomToTalkMap(@PathParam("conferenceId") Long conferenceId) {
        Schedule schedule = scheduleManager.getSchedule();
        Map<String, Map<String, Map<String, Talk>>> dayTimeslotRoomToTalkMap = new LinkedHashMap<String, Map<String, Map<String, Talk>>>();
        for (Day day : schedule.getDayList()) {
            dayTimeslotRoomToTalkMap.put(day.getId(), new LinkedHashMap<String, Map<String, Talk>>());
        }
        for (Timeslot timeslot : schedule.getTimeslotList()) {
            Day day = timeslot.getDay();
            LinkedHashMap<String, Talk> roomToTalkMap = new LinkedHashMap<String, Talk>();
            dayTimeslotRoomToTalkMap.get(day.getId()).put(timeslot.getId(), roomToTalkMap);
            for (Room room : schedule.getRoomList()) {
                roomToTalkMap.put(room.getId(), null);
            }
        }
        for (Talk talk : schedule.getTalkList()) {
            Timeslot timeslot = talk.getTimeslot();
            Day day = timeslot.getDay();
            Room room = talk.getRoom();
            dayTimeslotRoomToTalkMap.get(day.getId()).get(timeslot.getId()).put(room.getId(), talk);
        }
        return dayTimeslotRoomToTalkMap;
    }

    @GET
    @Path("/{talkId}/exclusion")
    @Produces("application/json")
    public List<TalkExclusion> getTalkExclusionList(@PathParam("conferenceId") Long conferenceId,
            @PathParam("talkId") Long talkId) {
        Schedule schedule = scheduleManager.getSchedule();
        // TODO do proper query to DB instead of filtering here
        List<TalkExclusion> globalTalkExclusionList = schedule.getTalkExclusionList();
        List<TalkExclusion> talkExclusionList = new ArrayList<TalkExclusion>(globalTalkExclusionList.size());
        for (TalkExclusion talkExclusion : globalTalkExclusionList) {
            if (talkExclusion.getFirstTalk().getId().equals(talkId)
                    || talkExclusion.getSecondTalk().getId().equals(talkId)) {
                talkExclusionList.add(talkExclusion);
            }
        }
        return talkExclusionList;
    }

}
