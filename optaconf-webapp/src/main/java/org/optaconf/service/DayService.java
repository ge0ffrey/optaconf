package org.optaconf.service;

import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.optaconf.domain.Day;
import org.optaconf.domain.Room;
import org.optaconf.domain.Schedule;

@Path("/{conferenceId}/day")
public class DayService {

    @Inject
    private Schedule schedule;

    @GET
    @Path("/")
    @Produces("application/json")
    public List<Day> getDayList(@PathParam("conferenceId") Long conferenceId) {
        return schedule.getDayList();
    }

}
