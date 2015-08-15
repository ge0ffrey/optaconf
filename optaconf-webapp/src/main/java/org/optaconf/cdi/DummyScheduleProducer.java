package org.optaconf.cdi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;

import org.optaconf.domain.Day;
import org.optaconf.domain.Room;
import org.optaconf.domain.Conference;
import org.optaconf.domain.Talk;
import org.optaconf.domain.TalkExclusion;
import org.optaconf.domain.TalkExclusionType;
import org.optaconf.domain.Timeslot;
import org.optaconf.domain.Track;
import org.optaconf.util.TangoCssFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class DummyScheduleProducer implements Serializable {

	private static final Logger LOG = LoggerFactory.getLogger(DummyScheduleProducer.class);
	
    @Produces @SessionScoped
    public ScheduleManager createDummySchedule() {
        Conference schedule = new Conference();

        List<Day> dayList = new ArrayList<Day>();
        Day wedDay = new Day("1", "Wed", "2014-11-13", schedule);
        dayList.add(wedDay);
        Day thuDay = new Day("2", "Thu", "2014-11-14", schedule);
        dayList.add(thuDay);
        schedule.setDayList(dayList);



        List<Room> roomList = new ArrayList<Room>();
        roomList.add(new Room("1", "Room A", 120, schedule));
        roomList.add(new Room("2", "Room B", 100, schedule));
        roomList.add(new Room("3", "Room C", 80, schedule));
        schedule.setRoomList(roomList);

        TangoCssFactory tangoCssFactory = new TangoCssFactory();
        List<Track> trackList = new ArrayList<Track>();
        Track jseTrack = new Track("JSE", "Standard Java", tangoCssFactory.pickCssClass("JSE"), schedule);
        trackList.add(jseTrack);
        Track jeeTrack = new Track("JEE", "Enterprise Java", tangoCssFactory.pickCssClass("JEE"), schedule);
        trackList.add(jeeTrack);
        schedule.setTrackList(trackList);

        List<Timeslot> timeslotList = new ArrayList<Timeslot>();
        timeslotList.add(new Timeslot("1", "Wed 1", wedDay, "9:00", "10:00", schedule));
        timeslotList.add(new Timeslot("2", "Wed 2", wedDay, "10:00", "11:00", schedule));
        timeslotList.add(new Timeslot("3", "Thu 1", thuDay, "9:00", "10:00",  schedule));
        timeslotList.add(new Timeslot("4", "Thu 2", thuDay, "10:00", "11:00", schedule));
        schedule.setTimeslotList(timeslotList);
        
        List<Talk> talkList = new ArrayList<Talk>();
        Talk talk1 = new Talk("1", "T1 JEE Basic", schedule, roomList.get(0), jeeTrack, timeslotList.get(0));
        talkList.add(talk1);
        Talk talk2 = new Talk("2", "T2 Security", schedule, roomList.get(1), jseTrack, timeslotList.get(1));
        talkList.add(talk2);
        Talk talk3 = new Talk("3", "T3 WildFly", schedule, roomList.get(2), jeeTrack, timeslotList.get(2));
        talkList.add(talk3);
        Talk talk4 = new Talk("4", "T4 JEE Expert", schedule, roomList.get(0), jeeTrack, timeslotList.get(3));
        talkList.add(talk4);
        Talk talk5 = new Talk("5", "T5 Lambda part 1", schedule, roomList.get(1), jseTrack, timeslotList.get(0));
        talkList.add(talk5);
        Talk talk6 = new Talk("6", "T6 Lambda part 2", schedule, roomList.get(2), jseTrack, timeslotList.get(1));
        talkList.add(talk6);
        Talk talk7 = new Talk("7", "T7 Glassfish", schedule, roomList.get(0), jeeTrack, timeslotList.get(2));
        talkList.add(talk7);
        schedule.setTalkList(talkList);

        List<TalkExclusion> talkExclusionList = new ArrayList<TalkExclusion>();
        talkExclusionList.add(new TalkExclusion("1", talk1, talk7, TalkExclusionType.HARD_CONFLICT, schedule));
        talkExclusionList.add(new TalkExclusion("2", talk3, talk7, TalkExclusionType.SOFT_CONFLICT, schedule));
        schedule.setTalkExclusionList(talkExclusionList);

        return new ScheduleManager(schedule);
    }

}
