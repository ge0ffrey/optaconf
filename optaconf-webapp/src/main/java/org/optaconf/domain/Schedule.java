package org.optaconf.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.enterprise.inject.Vetoed;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

@PlanningSolution
public class Schedule extends AbstractPersistable implements Solution<HardSoftScore> {

    private List<Day> dayList;
    private List<Timeslot> timeslotList;
    private List<Room> roomList;
    private List<Talk> talkList;
    private List<TalkExclusion> talkExclusionList;

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
        facts.addAll(talkList);
        facts.addAll(talkExclusionList);
        // Do not add the planning entity's (processList) because that will be done automatically
        return facts;
    }

}
