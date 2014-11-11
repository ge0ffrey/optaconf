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
import org.optaconf.domain.TalkExclusion;
import org.optaconf.domain.Timeslot;

@Path("/{conferenceId}/talk")
public class TalkService {

    @Inject
    private Schedule schedule;

    @GET
    @Path("/")
    @Produces("application/json")
    public List<Talk> getTalkList(@PathParam("conferenceId") Long conferenceId) {
        return schedule.getTalkList();
    }

    @GET
    @Path("/{talkId}/exclusion")
    @Produces("application/json")
    public List<TalkExclusion> getTalkExclusionList(@PathParam("conferenceId") Long conferenceId,
            @PathParam("talkId") Long talkId) {
        // TODO do proper query to DB instead of filtering here
        List<TalkExclusion> globalTalkExclusionList = schedule.getTalkExclusionList();
        List<TalkExclusion> talkExclusionList = new ArrayList<TalkExclusion>(globalTalkExclusionList.size());
        for (TalkExclusion talkExclusion : globalTalkExclusionList) {
            if (talkExclusion.getFirstTalk().getId().equals(talkId)
                    || talkExclusion.getSecondTalk().getId().equals(talkId)) {
                talkExclusionList.add(talkExclusion);
            }
        }
        return talkExclusionList;
    }

}
