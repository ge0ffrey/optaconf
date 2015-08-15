package org.optaconf.service;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.optaconf.cdi.ScheduleManager;
import org.optaconf.domain.Room;
import org.optaconf.domain.Conference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/{conferenceId}/room")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomService {

	
	private static final Logger LOG = LoggerFactory.getLogger(RoomService.class);
	
    @Inject
    private ScheduleManager scheduleManager;

    @GET
    @Path("/")
    public List<Room> getRoomList(@PathParam("conferenceId") Long conferenceId) {
        Conference schedule = scheduleManager.getSchedule();
        return schedule.getRoomList();
    }

}
