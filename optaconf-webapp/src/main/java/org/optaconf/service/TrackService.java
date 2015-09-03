package org.optaconf.service;

import java.util.ArrayList;
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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.optaconf.domain.Conference;
import org.optaconf.domain.Track;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/{conferenceId}/track")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TrackService {

    private static final Logger LOG = LoggerFactory.getLogger(TrackService.class);

    @PersistenceContext(unitName = "optaconf-webapp-persistence-unit")
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    @GET
    @Path("/")
    public List<Track> getTrackList(@PathParam("conferenceId") Long conferenceId) {

        List<Track> trackList = new ArrayList<>();
        try {
            utx.begin();

            em.joinTransaction();
            Conference conference = em.find(Conference.class, conferenceId);
            conference.getTrackList().iterator().hasNext();
            trackList = conference.getTrackList();
        } catch (NotSupportedException | SystemException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                utx.commit();
            } catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
                    | HeuristicRollbackException | SystemException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return trackList;
    }

}
