package org.optaconf.service;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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

    @Inject
    private UserTransaction utx;

    @GET
    @Path("/")
    public List<Speaker> getSpeakerList(@PathParam("conferenceId") Long conferenceId) {
        Conference conference = em.find(Conference.class, conferenceId);
        return conference.getSpeakerList();
    }

    @PUT
    @Path("/{speakerId}/toggleRockstar")
    public Speaker toggleRockstar(@PathParam("speakerId") Long speakerId) {
        
        Speaker s = null;
        
        try {
            utx.begin();
            em.joinTransaction();

            Speaker speaker = em.find(Speaker.class, speakerId);
            speaker.setRockstar(!speaker.getRockstar());
            s = em.merge(speaker);
        } catch (NotSupportedException | SystemException e) {
            LOG.error(e.getLocalizedMessage(), e);
        } finally {
            try {
                utx.commit();
            } catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
                    | HeuristicRollbackException | SystemException e) {
                LOG.error(e.getLocalizedMessage(), e);
            }
        }
        
        return s;

    }

}
