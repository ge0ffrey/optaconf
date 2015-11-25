package org.optaconf.service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.optaconf.domain.Conference;
import org.optaconf.domain.Day;
import org.optaconf.domain.Room;
import org.optaconf.domain.Talk;
import org.optaconf.domain.Timeslot;
import org.optaconf.util.ScheduleCommand;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ServerEndpoint("/schedule")
@Stateful
public class ScheduleService {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleService.class);

    @PersistenceContext(unitName = "optaconf-webapp-persistence-unit")
    private EntityManager em;

    @Inject
    TalkService ts;

    private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());

    @Inject
    private SolverFactory solverFactory;
    
    @Resource(name = "DefaultManagedExecutorService")
    private ManagedExecutorService executor;

    private Solver solver;
    private ObjectMapper mapper =  new ObjectMapper();

    @OnOpen
    public void init(Session session) throws IOException {
        clients.add(session);
        LOG.info("Open session: " + session.getId());
    }

    @OnMessage
    public void routeMessage(String message, Session session) throws IOException {

        LOG.info("Received : " + message + ", session:" + session.getId());
        
        
        ScheduleCommand command = mapper.readValue(message, ScheduleCommand.class);
        
        LOG.info(command.toString());
        
        if (command.getAction().equals("solve")){
            solveConference(session, command.getId());
        }else if ("view".equals(command.getAction())){
            viewConference(session, command.getId());
        }
        
    }

    private void solveConference(Session session, Long conferenceId) throws JsonProcessingException, IOException {
        Conference conference = em.find(Conference.class, conferenceId);

        Solver oldSolver = solver;
        if (oldSolver != null && oldSolver.isSolving()) {
            oldSolver.terminateEarly();
        }
        Solver solver = solverFactory.buildSolver();
        solver.addEventListener(new SolverEventListener<Conference>() {

            public void bestSolutionChanged(BestSolutionChangedEvent<Conference> event) {

                if (event.isNewBestSolutionInitialized()) {
                    LOG.info(event.getNewBestSolution().toString());
                    try {
                        broadcast(getDayTimeslotRoomToTalkString(event.getNewBestSolution()));
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        LOG.error(e.getLocalizedMessage(), e);
                    }
                }
            }
        });
        // TODO Use async solving https://developer.jboss.org/message/910391
        // executor.submit(new SolverCallable(solver,
        // scheduleManager.getSchedule()));
        // return "Solving started.";
        solver.solve(conference);

        conference = (Conference) solver.getBestSolution();

        em.merge(conference);
        
        broadcast(getDayTimeslotRoomToTalkString(conference));
            
    }
    
    private void viewConference(Session session, Long conferenceId) throws JsonProcessingException, IOException {
        Conference conference = em.find(Conference.class, conferenceId);
        conference.getDayList().iterator().hasNext();
        conference.getTalkList().iterator().hasNext();
        conference.getTimeslotList().iterator().hasNext();
        conference.getRoomList().iterator().hasNext();
        broadcast(getDayTimeslotRoomToTalkString(conference));

    }
    
    private void broadcast(String message){
        synchronized (clients) {
            // Iterate over the connected sessions
            // and broadcast the received message
            for (Session client : clients) {
                client.getAsyncRemote().sendText(message);
            }
        }   
    }

    private String getDayTimeslotRoomToTalkString(Conference conference) throws JsonProcessingException {

        Map<String, Map<String, Map<String, Talk>>> dayTimeslotRoomToTalkMap = new LinkedHashMap<String, Map<String, Map<String, Talk>>>();
        for (Day day : conference.getDayList()) {
            dayTimeslotRoomToTalkMap.put(day.getExternalId(), new LinkedHashMap<String, Map<String, Talk>>());
        }
        for (Timeslot timeslot : conference.getTimeslotList()) {
            Day day = timeslot.getDay();
            LinkedHashMap<String, Talk> roomToTalkMap = new LinkedHashMap<String, Talk>();
            dayTimeslotRoomToTalkMap.get(day.getExternalId()).put(timeslot.getExternalId(), roomToTalkMap);
            for (Room room : conference.getRoomList()) {
                roomToTalkMap.put(room.getExternalId(), null);
            }
        }
        for (Talk talk : conference.getTalkList()) {
            Timeslot timeslot = talk.getTimeslot();
            Room room = talk.getRoom();

            if (timeslot != null && room != null && room.getExternalId() != null) {
                Day day = timeslot.getDay();
                Map<String, Map<String, Talk>> map = dayTimeslotRoomToTalkMap.get(day.getExternalId());
                Map<String, Talk> map2 = map.get(timeslot.getExternalId());
                map2.put(room.getExternalId(), talk);
            }
        }

        String sched = mapper.writeValueAsString(dayTimeslotRoomToTalkMap);

        return sched;
    }

    @OnClose
    public void close(Session session, CloseReason c) {
        LOG.info("Closing:" + session.getId());
        clients.remove(session);
        session = null;
    }

}
