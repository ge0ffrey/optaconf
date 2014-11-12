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
import org.optaconf.domain.Track;

@Path("/{conferenceId}/track")
public class TrackService {

    @Inject
    private ScheduleManager scheduleManager;

    @GET
    @Path("/")
    @Produces("application/json")
    public List<Track> getTrackList(@PathParam("conferenceId") Long conferenceId) {
        Schedule schedule = scheduleManager.getSchedule();
        return schedule.getTrackList();
    }

}
