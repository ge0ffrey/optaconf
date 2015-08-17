package org.optaconf.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.optaconf.bridge.devoxx.DevoxxImporter;
import org.optaconf.domain.Conference;
import org.optaconf.domain.Day;
import org.optaconf.domain.Room;
import org.optaconf.domain.Speaker;
import org.optaconf.domain.SpeakingRelation;
import org.optaconf.domain.Talk;
import org.optaconf.domain.TalkExclusion;
import org.optaconf.domain.Timeslot;
import org.optaconf.domain.Track;
import org.optaconf.domain.UnavailableTimeslotRoomPenalty;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/conference")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConferenceService
{

   private static final Logger LOG = LoggerFactory
            .getLogger(ConferenceService.class);

   @Inject
   UserTransaction utx;

   @PersistenceContext(unitName = "optaconf-webapp-persistence-unit")
   private EntityManager em;

   private Solver solver;

   @Inject
   private DevoxxImporter devoxxImporter;

   @Inject
   private SolverFactory solverFactory;

   @Resource(name = "DefaultManagedExecutorService")
   private ManagedExecutorService executor;

   @POST
   @Path("/import/devoxx")
   public Response importDevoxx()
   {
      StringBuilder message = new StringBuilder();
      try {
         utx.begin();

         em.joinTransaction();

         Conference conference = devoxxImporter.importSchedule();

         message.append("Devoxx conference with ")
                  .append(conference.getDayList().size()).append(" days, ")
                  .append(conference.getTimeslotList().size())
                  .append(" timeslots, ").append(conference.getRoomList().size())
                  .append(" rooms, ").append(conference.getTrackList().size())
                  .append(" tracks, ").append(conference.getSpeakerList().size())
                  .append(" speakers, ").append(conference.getTalkList().size())
                  .append(" talks imported successfully.");

         LOG.info(message.toString());
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

      return Response.ok(message).build();

   }

   @GET
   @Path("")
   public List<Conference> getAll()
   {
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<Conference> cq = cb.createQuery(Conference.class);
      Root<Conference> rootEntry = cq.from(Conference.class);
      CriteriaQuery<Conference> all = cq.select(rootEntry);
      TypedQuery<Conference> allQuery = em.createQuery(all);
      List<Conference> conferences = allQuery.getResultList();

      return conferences;
   }

   @GET
   @Path("/{conferenceId}")
   public Conference getOne(@PathParam("conferenceId") Long conferenceId)
   {
      Conference conference = em.find(Conference.class, conferenceId);
      return conference;
   }

   @DELETE
   @Path("/{conferenceId}")
   public void deleteOne(@PathParam("conferenceId") Long conferenceId)
   {
      // TODO: take care of orphaned entries
      em.remove(em.find(Conference.class, conferenceId));
   }

   @PUT
   @GET
   @Path("/{conferenceId}/solve")
   public Response solveSchedule(@PathParam("conferenceId") Long conferenceId)
   {

      Conference conference = new Conference();

      try {
         utx.begin();

         em.joinTransaction();

         conference = em.find(Conference.class, conferenceId);
         conference.getDayList().iterator().hasNext();
         conference.getTalkList().iterator().hasNext();
         conference.getTalkExclusionList().iterator().hasNext();
         conference.getTimeslotList().iterator().hasNext();
         conference.getRoomList().iterator().hasNext();
         conference.getSpeakerList().iterator().hasNext();
         conference.getSpeakingRelationList().iterator().hasNext();
         conference.getTrackList().iterator().hasNext();
         conference.getUnavailableTimeslotRoomPenaltyList().iterator().hasNext();

//         em.remove(conference);
//         em.flush();
         Solver oldSolver = solver;
         if (oldSolver != null && oldSolver.isSolving()) {
            oldSolver.terminateEarly();
         }
         Solver solver = solverFactory.buildSolver();
         // TODO Use async solving https://developer.jboss.org/message/910391
         // executor.submit(new SolverCallable(solver,
         // scheduleManager.getSchedule()));
         // return "Solving started.";
         solver.solve(conference);

         conference = (Conference) solver.getBestSolution();
//         em.merge(conference);

//         CopyOnWriteArrayList<Day> conferenceDays = new CopyOnWriteArrayList<>(conference.getDayList());
//         for (Day day : conferenceDays)
//            em.merge(day);
//
//         CopyOnWriteArrayList<Talk> conferenceTalks = new CopyOnWriteArrayList<>(conference.getTalkList());
//         for (Talk talk : conferenceTalks)
//            em.merge(talk);
//
//         CopyOnWriteArrayList<TalkExclusion> conferenceTalkExclusions = new CopyOnWriteArrayList<>(
//                  conference.getTalkExclusionList());
//         for (TalkExclusion te : conferenceTalkExclusions)
//            em.merge(te);
//
//         CopyOnWriteArrayList<Timeslot> conferenceTimeslots = new CopyOnWriteArrayList<>(conference.getTimeslotList());
//         for (Timeslot timeslot : conferenceTimeslots)
//            em.merge(timeslot);
//
//         CopyOnWriteArrayList<Room> conferenceRooms = new CopyOnWriteArrayList<>(conference.getRoomList());
//         for (Room room : conferenceRooms)
//            em.merge(room);
//
//         CopyOnWriteArrayList<Speaker> conferenceSpeakers = new CopyOnWriteArrayList<>(conference.getSpeakerList());
//         for (Speaker speaker : conferenceSpeakers)
//            em.merge(speaker);
//
//         CopyOnWriteArrayList<SpeakingRelation> conferenceSpeakingRelations = new CopyOnWriteArrayList<>(
//                  conference.getSpeakingRelationList());
//         for (SpeakingRelation sr : conferenceSpeakingRelations)
//            em.merge(sr);
//
//         CopyOnWriteArrayList<Track> conferenceTracks = new CopyOnWriteArrayList<>(conference.getTrackList());
//         for (Track track : conferenceTracks)
//            em.merge(track);
//
//         CopyOnWriteArrayList<UnavailableTimeslotRoomPenalty> conferenceUnavailableTimeslotRoomPenaltys = new CopyOnWriteArrayList<>(
//                  conference.getUnavailableTimeslotRoomPenaltyList());
//         for (UnavailableTimeslotRoomPenalty utrp : conferenceUnavailableTimeslotRoomPenaltys)
//            em.merge(utrp);

//         em.flush();

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
      return Response.ok(conference).build();
   }

   @GET
   @Path("/{conferenceId}/isSolving")
   public Response isSolving(@PathParam("conferenceId") Long conferenceId)
   {
      return Response.ok(solver != null && solver.isSolving()).build();
   }

   @PUT
   @Path("/{conferenceId}/terminateSolving")
   public Response terminateSolving(@PathParam("conferenceId") Long conferenceId)
   {
      if (solver != null) {
         solver.terminateEarly();
         return Response.ok("Solving terminated!").build();
      }

      return Response.status(Response.Status.NOT_FOUND).entity("Solver not found.").build();
   }
   /*
   	private class SolverCallable implements Runnable {
   
   		private final Solver solver;
   		private final Schedule schedule;
   
   		private SolverCallable(Solver solver, Schedule schedule) {
   			this.solver = solver;
   			this.schedule = schedule;
   		}
   
   		public void run() {
   			solver.addEventListener(new SolverEventListener() {
   				@Override
   				public void bestSolutionChanged(
   						BestSolutionChangedEvent bestSolutionChangedEvent) {
   					scheduleManager
   							.setSchedule((Schedule) bestSolutionChangedEvent
   									.getNewBestSolution()); // TODO throws eaten
   															// Exception
   				}
   			});
   			solver.solve(schedule);
   			Schedule bestSchedule = (Schedule) solver.getBestSolution(); // TODO
   																			// throws
   																			// eaten
   																			// Exception
   			scheduleManager.setSchedule(bestSchedule);
   			scheduleManager.setSolver(null);
   		}
   	}
   */
}
