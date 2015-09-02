package org.optaconf.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.optaconf.domain.Conference;
import org.optaconf.domain.Speaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/{conferenceId}/speaker")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SpeakerService {
	
	private static final Logger LOG = LoggerFactory.getLogger(SpeakerService.class);

    @PersistenceContext(unitName = "optaconf-webapp-persistence-unit")
    private EntityManager em;

    @GET
    @Path("/")
    public List<Speaker> getSpeakerList(@PathParam("conferenceId") Long conferenceId) {
        Conference conference = em.find(Conference.class, conferenceId);
        return conference.getSpeakerList();
    }

}
