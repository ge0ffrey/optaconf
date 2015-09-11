package org.optaconf.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import org.optaconf.domain.Day;
import org.optaconf.domain.Room;
import org.optaconf.domain.Talk;
import org.optaconf.domain.TalkExclusion;
import org.optaconf.domain.Timeslot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/{conferenceId}/talk")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TalkService {

    private static final Logger LOG = LoggerFactory.getLogger(TalkService.class);

    @PersistenceContext(unitName = "optaconf-webapp-persistence-unit")
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    @GET
    @Path("/")
    public List<Talk> getTalkList(@PathParam("conferenceId") Long conferenceId) {
        Conference conference = em.find(Conference.class, conferenceId);
        return conference.getTalkList();
    }

    @GET
    @Path("/map")
    public Map<String, Map<String, Map<String, Talk>>> getDayTimeslotRoomToTalkMap(
            @PathParam("conferenceId") Long conferenceId) {
        Conference conference = new Conference();
        try {
            utx.begin();
            em.joinTransaction();
            conference = em.find(Conference.class, conferenceId);
            conference.getDayList().iterator().hasNext();
            conference.getTalkList().iterator().hasNext();
            conference.getTimeslotList().iterator().hasNext();
            conference.getRoomList().iterator().hasNext();
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
        return dayTimeslotRoomToTalkMap;
    }

    @GET
    @Path("/schedule")
    public Conference getSchedule(@PathParam("conferenceId") Long conferenceId) {
        Conference conference = em.find(Conference.class, conferenceId);

        return conference;
    }

    @GET
    @Path("/{talkId}/exclusion")
    public List<TalkExclusion> getTalkExclusionList(@PathParam("conferenceId") Long conferenceId,
            @PathParam("talkId") Long talkId) {

        Conference conference = em.find(Conference.class, conferenceId);
        // TODO do proper query to DB instead of filtering here
        List<TalkExclusion> globalTalkExclusionList = conference.getTalkExclusionList();
        List<TalkExclusion> talkExclusionList = new ArrayList<TalkExclusion>(globalTalkExclusionList.size());
        for (TalkExclusion talkExclusion : globalTalkExclusionList) {
            if (talkExclusion.getFirstTalk().getExternalId().equals(talkId)
                    || talkExclusion.getSecondTalk().getExternalId().equals(talkId)) {
                talkExclusionList.add(talkExclusion);
            }
        }
        return talkExclusionList;
    }

}
