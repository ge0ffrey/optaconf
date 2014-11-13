package org.optaconf.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.optaconf.domain.speaker.Speaker;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

@PlanningSolution
public class Schedule extends AbstractPersistable implements Solution<HardSoftScore> {

    private List<Day> dayList = new ArrayList<Day>();
    private List<Timeslot> timeslotList = new ArrayList<Timeslot>();
    private List<Room> roomList = new ArrayList<Room>();
    private List<Track> trackList = new ArrayList<Track>();
    private List<Speaker> speakerList = new ArrayList<Speaker>();
    private List<Talk> talkList = new ArrayList<Talk>();
    private List<TalkExclusion> talkExclusionList = new ArrayList<TalkExclusion>();

    private HardSoftScore score;

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

    @ValueRangeProvider(id = "timeslotRange")
    public List<Timeslot> getTimeslotList() {
        return timeslotList;
    }

    public void setTimeslotList(List<Timeslot> timeslotList) {
        this.timeslotList = timeslotList;
    }

    @ValueRangeProvider(id = "roomRange")
    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    public List<Track> getTrackList() {
        return trackList;
    }

    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
    }

    public List<Speaker> getSpeakerList() {
        return speakerList;
    }

    public void setSpeakerList(List<Speaker> speakerList) {
        this.speakerList = speakerList;
    }

    @PlanningEntityCollectionProperty
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

    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }

    @Override
    public Collection<?> getProblemFacts() {
        List<Object> facts = new ArrayList<Object>();
        facts.addAll(dayList);
        facts.addAll(timeslotList);
        facts.addAll(roomList);
        facts.addAll(trackList);
        facts.addAll(speakerList);
        facts.addAll(talkList);
        facts.addAll(talkExclusionList);
        // Do not add the planning entity's (processList) because that will be done automatically
        return facts;
    }

}
