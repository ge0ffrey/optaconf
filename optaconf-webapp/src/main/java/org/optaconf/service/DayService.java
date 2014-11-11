package org.optaconf.service;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.optaconf.cdi.ScheduleManager;
import org.optaconf.domain.Day;
import org.optaconf.domain.Schedule;
import org.optaconf.domain.Timeslot;

@Path("/{conferenceId}/day")
public class DayService {

    @Inject
    private ScheduleManager scheduleManager;

    @GET
    @Path("/")
    @Produces("application/json")
    public List<Day> getDayList(@PathParam("conferenceId") Long conferenceId) {
        Schedule schedule = scheduleManager.getSchedule();
        return schedule.getDayList();
    }

    @GET
    @Path("/{dayId}/timeslot")
    @Produces("application/json")
    public List<Timeslot> getTimeslotList(@PathParam("conferenceId") Long conferenceId,
            @PathParam("dayId") Long dayId) {
        Schedule schedule = scheduleManager.getSchedule();
        // TODO do proper query to DB instead of filtering here
        List<Timeslot> globalTimeslotList = schedule.getTimeslotList();
        List<Timeslot> timeslotList = new ArrayList<Timeslot>(globalTimeslotList.size());
        for (Timeslot timeslot : globalTimeslotList) {
            if (timeslot.getDay().getId().equals(dayId)) {
                timeslotList.add(timeslot);
            }
        }
        return timeslotList;
    }

}
