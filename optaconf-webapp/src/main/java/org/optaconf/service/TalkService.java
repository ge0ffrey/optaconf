package org.optaconf.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.optaconf.cdi.ScheduleManager;
import org.optaconf.domain.Day;
import org.optaconf.domain.Room;
import org.optaconf.domain.Schedule;
import org.optaconf.domain.Talk;
import org.optaconf.domain.TalkExclusion;
import org.optaconf.domain.Timeslot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/{conferenceId}/talk")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TalkService {
	
	private static final Logger LOG = LoggerFactory.getLogger(TalkService.class);

    @Inject
    private ScheduleManager scheduleManager;

    @GET
    @Path("/")
    public List<Talk> getTalkList(@PathParam("conferenceId") Long conferenceId) {
        Schedule schedule = scheduleManager.getSchedule();
        return schedule.getTalkList();
    }

    @GET
    @Path("/map")
    public Map<String, Map<String, Map<String, Talk>>> getDayTimeslotRoomToTalkMap(@PathParam("conferenceId") Long conferenceId) {
        Schedule schedule = scheduleManager.getSchedule();
        Map<String, Map<String, Map<String, Talk>>> dayTimeslotRoomToTalkMap = new LinkedHashMap<String, Map<String, Map<String, Talk>>>();
        for (Day day : schedule.getDayList()) {
            dayTimeslotRoomToTalkMap.put(day.getExternalId(), new LinkedHashMap<String, Map<String, Talk>>());
        }
        for (Timeslot timeslot : schedule.getTimeslotList()) {
            Day day = timeslot.getDay();
            LinkedHashMap<String, Talk> roomToTalkMap = new LinkedHashMap<String, Talk>();
            dayTimeslotRoomToTalkMap.get(day.getExternalId()).put(timeslot.getExternalId(), roomToTalkMap);
            for (Room room : schedule.getRoomList()) {
                roomToTalkMap.put(room.getExternalId(), null);
            }
        }
        for (Talk talk : schedule.getTalkList()) {
            Timeslot timeslot = talk.getTimeslot();
            Day day = timeslot.getDay();
            Room room = talk.getRoom();
            if(room != null && room.getExternalId() != null){
               dayTimeslotRoomToTalkMap.get(day.getExternalId()).get(timeslot.getExternalId()).put(room.getExternalId(), talk);
            }
        }
        return dayTimeslotRoomToTalkMap;
    }

    @GET
    @Path("/{talkId}/exclusion")
    public List<TalkExclusion> getTalkExclusionList(@PathParam("conferenceId") Long conferenceId,
            @PathParam("talkId") Long talkId) {
        Schedule schedule = scheduleManager.getSchedule();
        // TODO do proper query to DB instead of filtering here
        List<TalkExclusion> globalTalkExclusionList = schedule.getTalkExclusionList();
        List<TalkExclusion> talkExclusionList = new ArrayList<TalkExclusion>(globalTalkExclusionList.size());
        for (TalkExclusion talkExclusion : globalTalkExclusionList) {
            if (talkExclusion.getFirstTalk().getExternalId().equals(talkId)
                    || talkExclusion.getSecondTalk().getExternalId().equals(talkId)) {
                talkExclusionList.add(talkExclusion);
            }
        }
        return talkExclusionList;
    }

}
