package org.optaconf.service;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.optaconf.domain.Talk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
public class TalkService {

    private final static Logger logger = LoggerFactory.getLogger(TalkService.class);

    @GET
    @Path("/json/{name}")
    @Produces("application/json")
    public List<Talk> getTalkList() {
        List<Talk> talkList = new ArrayList<Talk>();
        talkList.add(new Talk(1L, "Talk 1"));
        talkList.add(new Talk(2L, "Talk 2"));
        talkList.add(new Talk(3L, "Talk 3"));

        return talkList;
    }

}
