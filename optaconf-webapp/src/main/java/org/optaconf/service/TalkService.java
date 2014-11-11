package org.optaconf.service;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.optaconf.domain.Schedule;
import org.optaconf.domain.Talk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/talk")
public class TalkService {

    private final static Logger logger = LoggerFactory.getLogger(TalkService.class);

    @Inject
    private Schedule schedule;

    @GET
    @Path("/{conferenceId}")
    @Produces("application/json")
    public List<Talk> getTalkList(@PathParam("conferenceId") Long conferenceId) {
        logger.debug("Called getTalkList for conferenceId ({}).", conferenceId);
        return schedule.getTalkList();
    }

}
