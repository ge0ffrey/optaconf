package org.optaconf.domain;

import java.util.List;
import javax.enterprise.inject.Vetoed;

public class Schedule extends AbstractPersistable {

    private List<Day> dayList;
    private List<Timeslot> timeslotList;
    private List<Room> roomList;
    private List<Talk> talkList;
    private List<TalkExclusion> talkExclusionList;

    public Schedule() {
    }

    public Schedule(String id) {
        super(id);
    }

    public List<Day> getDayList() {
        return dayList;
    }

    public void setDayList(List<Day> dayList) {
        this.dayList = dayList;
    }

    public List<Timeslot> getTimeslotList() {
        return timeslotList;
    }

    public void setTimeslotList(List<Timeslot> timeslotList) {
        this.timeslotList = timeslotList;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    public List<Talk> getTalkList() {
        return talkList;
    }

    public void setTalkList(List<Talk> talkList) {
        this.talkList = talkList;
    }

    public List<TalkExclusion> getTalkExclusionList() {
        return talkExclusionList;
    }

    public void setTalkExclusionList(List<TalkExclusion> talkExclusionList) {
        this.talkExclusionList = talkExclusionList;
    }
}
