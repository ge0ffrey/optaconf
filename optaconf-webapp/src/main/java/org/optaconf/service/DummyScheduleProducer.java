package org.optaconf.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;

import org.optaconf.domain.Day;
import org.optaconf.domain.Room;
import org.optaconf.domain.Schedule;
import org.optaconf.domain.Talk;
import org.optaconf.domain.TalkExclusion;
import org.optaconf.domain.TalkExclusionType;
import org.optaconf.domain.Timeslot;

@ApplicationScoped
public class DummyScheduleProducer implements Serializable {

    @Produces @SessionScoped
    public Schedule createDummySchedule() {
        Schedule schedule = new Schedule();

        List<Day> dayList = new ArrayList<Day>();
        Day wedDay = new Day("1", "Wed", "2014-11-13");
        dayList.add(wedDay);
        Day thuDay = new Day("2", "Thu", "2014-11-14");
        dayList.add(thuDay);
        schedule.setDayList(dayList);

        List<Timeslot> timeslotList = new ArrayList<Timeslot>();
        timeslotList.add(new Timeslot("1", "Wed 1", wedDay));
        timeslotList.add(new Timeslot("2", "Wed 2", wedDay));
        timeslotList.add(new Timeslot("3", "Thu 1", thuDay));
        timeslotList.add(new Timeslot("4", "Thu 2", thuDay));
        schedule.setTimeslotList(timeslotList);

        List<Room> roomList = new ArrayList<Room>();
        roomList.add(new Room("1", "Room A", 120));
        roomList.add(new Room("2", "Room B", 100));
        roomList.add(new Room("3", "Room C", 80));
        schedule.setRoomList(roomList);

        List<Talk> talkList = new ArrayList<Talk>();
        Talk talk1 = new Talk("1", "T1 JEE Basic");
        talkList.add(talk1);
        Talk talk2 = new Talk("2", "T2 Security");
        talkList.add(talk2);
        Talk talk3 = new Talk("3", "T3 WildFly");
        talkList.add(talk3);
        Talk talk4 = new Talk("4", "T4 JEE Expert");
        talkList.add(talk4);
        Talk talk5 = new Talk("5", "T5 Lambda part 1");
        talkList.add(talk5);
        Talk talk6 = new Talk("6", "T6 Lambda part 2");
        talkList.add(talk6);
        Talk talk7 = new Talk("7", "T7 Glassfish");
        talkList.add(talk7);
        schedule.setTalkList(talkList);

        List<TalkExclusion> talkExclusionList = new ArrayList<TalkExclusion>();
        talkExclusionList.add(new TalkExclusion("1", talk1, talk7, TalkExclusionType.HARD_CONFLICT));
        talkExclusionList.add(new TalkExclusion("2", talk3, talk7, TalkExclusionType.SOFT_CONFLICT));
        schedule.setTalkExclusionList(talkExclusionList);

        return schedule;
    }

}