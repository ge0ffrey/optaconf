package org.optaconf.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
import org.optaconf.domain.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/{conferenceId}/room")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomService
{

   private static final Logger LOG = LoggerFactory.getLogger(RoomService.class);

   @PersistenceContext(unitName = "optaconf-webapp-persistence-unit")
   private EntityManager em;

   @Inject
   private UserTransaction utx;

   @GET
   @Path("/")
   public Set<Room> getRoomList(@PathParam("conferenceId") Long conferenceId)
   {
      Set<Room> rooms = new LinkedHashSet<>();
      try {
         utx.begin();

         em.joinTransaction();
         Conference conference = em.find(Conference.class, conferenceId);
         conference.getRoomList().iterator().hasNext();
         rooms = new LinkedHashSet(conference.getRoomList());
      }
      catch (NotSupportedException | SystemException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      finally {
         try {
            utx.commit();
         }
         catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
                  | HeuristicRollbackException | SystemException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }

      return rooms;
   }

}
