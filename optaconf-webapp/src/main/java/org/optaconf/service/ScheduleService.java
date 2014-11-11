package org.optaconf.service;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.optaconf.bridge.devoxx.DevoxxImporter;
import org.optaconf.domain.Schedule;
import org.optaconf.domain.Talk;
import org.optaconf.domain.TalkExclusion;

@Path("/{conferenceId}/schedule")
public class ScheduleService {

    @Inject
    private Schedule schedule;

    @Inject
    private DevoxxImporter devoxxImporter;

    @GET
    @Path("/import/devoxx")
    @Produces("application/json")
    public String importDevoxx(@PathParam("conferenceId") Long conferenceId) {
        schedule = devoxxImporter.importSchedule();
        return "Devoxx schedule with " + schedule.getDayList().size() + " days, "
                + schedule.getRoomList().size() + " rooms imported successfully.";
    }

}
