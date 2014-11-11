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
import org.optaconf.domain.Timeslot;

@ApplicationScoped
public class DummyScheduleProducer implements Serializable {

    @Produces @SessionScoped
    public Schedule createDummySchedule() {
        Schedule schedule = new Schedule();

        List<Day> dayList = new ArrayList<Day>();
        Day wedDay = new Day(1L, "Wed", "2014-11-13");
        dayList.add(wedDay);
        Day thuDay = new Day(2L, "Thu", "2014-11-14");
        dayList.add(thuDay);
        schedule.setDayList(dayList);

        List<Timeslot> timeslotList = new ArrayList<Timeslot>();
        timeslotList.add(new Timeslot(1L, "Wed 1", wedDay));
        timeslotList.add(new Timeslot(2L, "Wed 2", wedDay));
        timeslotList.add(new Timeslot(3L, "Thu 1", thuDay));
        timeslotList.add(new Timeslot(4L, "Thu 2", thuDay));
        schedule.setTimeslotList(timeslotList);

        List<Room> roomList = new ArrayList<Room>();
        roomList.add(new Room(1L, "Room A", 120));
        roomList.add(new Room(2L, "Room B", 100));
        roomList.add(new Room(3L, "Room C", 80));
        schedule.setRoomList(roomList);

        List<Talk> talkList = new ArrayList<Talk>();
        talkList.add(new Talk(1L, "Talk 1"));
        talkList.add(new Talk(2L, "Talk 2"));
        talkList.add(new Talk(3L, "Talk 3"));
        talkList.add(new Talk(4L, "Talk 4"));
        schedule.setTalkList(talkList);

        return schedule;
    }

}
