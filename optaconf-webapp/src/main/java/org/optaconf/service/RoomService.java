package org.optaconf.service;

import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.optaconf.cdi.ScheduleManager;
import org.optaconf.domain.Room;
import org.optaconf.domain.Schedule;
import org.optaconf.domain.Talk;

@Path("/{conferenceId}/room")
public class RoomService {

    @Inject
    private ScheduleManager scheduleManager;

    @GET
    @Path("/")
    @Produces("application/json")
    public List<Room> getRoomList(@PathParam("conferenceId") Long conferenceId) {
        Schedule schedule = scheduleManager.getSchedule();
        return schedule.getRoomList();
    }

}
