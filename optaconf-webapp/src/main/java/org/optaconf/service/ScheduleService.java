package org.optaconf.service;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.optaconf.bridge.devoxx.DevoxxImporter;
import org.optaconf.cdi.ScheduleManager;
import org.optaconf.domain.Conference;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/{conferenceId}/schedule")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
@LocalBean
public class ScheduleService {

	private static final Logger LOG = LoggerFactory
			.getLogger(ScheduleService.class);
	
	@PersistenceContext(unitName="optaconf-webapp-persistence-unit")
	private EntityManager em;

	@Inject
	private ScheduleManager scheduleManager;

	@Inject
	private DevoxxImporter devoxxImporter;

	@Inject
	private SolverFactory solverFactory;

	@Resource(name = "DefaultManagedExecutorService")
	private ManagedExecutorService executor;

	@POST
	@Path("/import/devoxx")
	public Response importDevoxx() {
		Conference schedule = devoxxImporter.importSchedule();
		scheduleManager.setSchedule(schedule);
		
		StringBuilder message = new StringBuilder()
				.append("Devoxx schedule with ")
				.append(schedule.getDayList().size()).append(" days, ")
				.append(schedule.getTimeslotList().size())
				.append(" timeslots, ").append(schedule.getRoomList().size())
				.append(" rooms, ").append(schedule.getTrackList().size())
				.append(" tracks, ").append(schedule.getSpeakerList().size())
				.append(" speakers, ").append(schedule.getTalkList().size())
				.append(" talks imported successfully.");
		
		LOG.info(message.toString());
		
		return Response.ok(message).build();

	}

	@GET
	@Path("/all")
	public List<Conference> getAll(){
	   CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<Conference> cq = cb.createQuery(Conference.class);
      Root<Conference> rootEntry = cq.from(Conference.class);
      CriteriaQuery<Conference> all = cq.select(rootEntry);
      TypedQuery<Conference> allQuery = em.createQuery(all);
      return allQuery.getResultList();
	}
	
	@PUT
	@Path("/solve")
	public Response solveSchedule(@PathParam("conferenceId") Long conferenceId) {
		Solver oldSolver = scheduleManager.getSolver();
		if (oldSolver != null && oldSolver.isSolving()) {
			oldSolver.terminateEarly();
		}
		Solver solver = solverFactory.buildSolver();
		// TODO Use async solving https://developer.jboss.org/message/910391
		// scheduleManager.setSolver(solver);
		// executor.submit(new SolverCallable(solver,
		// scheduleManager.getSchedule()));
		// return "Solved started.";
		solver.solve(scheduleManager.getSchedule());
		scheduleManager.setSchedule((Conference) solver.getBestSolution());
		return Response.ok("Solved!").build();
	}

	@GET
	@Path("/isSolving")
	public Response isSolving(@PathParam("conferenceId") Long conferenceId) {
		Solver solver = scheduleManager.getSolver();
		return Response.ok(solver != null && solver.isSolving()).build();
	}

	@PUT
	@Path("/terminateSolving")
	public Response terminateSolving(@PathParam("conferenceId") Long conferenceId) {
		Solver solver = scheduleManager.getSolver();
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
