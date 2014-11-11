package org.optaconf.domain;

import java.util.List;
import javax.enterprise.inject.Vetoed;

@Vetoed
public class Schedule extends AbstractPersistable {

    private List<Timeslot> timeslotList;
    private List<Room> roomList;
    private List<Talk> talkList;

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

}
