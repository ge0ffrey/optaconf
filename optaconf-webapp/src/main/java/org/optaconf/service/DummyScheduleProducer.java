package org.optaconf.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;

import org.optaconf.domain.Schedule;
import org.optaconf.domain.Talk;

@ApplicationScoped
public class DummyScheduleProducer implements Serializable {

    @Produces @SessionScoped
    public Schedule createDummySchedule() {
        Schedule schedule = new Schedule();

        List<Talk> talkList = new ArrayList<Talk>();
        talkList.add(new Talk(1L, "Talk 1"));
        talkList.add(new Talk(2L, "Talk 2"));
        talkList.add(new Talk(3L, "Talk 3"));
        talkList.add(new Talk(4L, "Talk 4"));
        schedule.setTalkList(talkList);

        return schedule;
    }

}
