package org.optaconf.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.optaplanner.core.api.domain.solution.cloner.DeepPlanningClone;

@DeepPlanningClone
@Entity(name = "optaconf_timeslot")
public class Timeslot extends AbstractConferencedPersistable implements Comparable<Timeslot> {

    @Column(length = 255, nullable = false)
    private String name;

    @Column(length = 255, nullable = false)
    private String fromTime;

    @Column(length = 255, nullable = false)
    private String toTime;

    @ManyToOne()
    @JoinColumn(name = "day_id", nullable = false)
    @JsonBackReference
    private Day day;

    @OneToMany(mappedBy = "timeslot", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Talk> talks;

    @OneToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "timeslot_room_penalty_id")
    private UnavailableTimeslotRoomPenalty penalty;

    public Timeslot() {
    }

    public Timeslot(Conference conference, String externalId, String name, Day day, String fromTime, String toTime) {
        super(conference, externalId);
        this.name = name;
        this.day = day;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public List<Talk> getTalks() {
        return talks;
    }

    public void setTalks(List<Talk> talks) {
        this.talks = talks;
    }

    public UnavailableTimeslotRoomPenalty getPenalty() {
        return penalty;
    }

    public void setPenalty(UnavailableTimeslotRoomPenalty penalty) {
        this.penalty = penalty;
    }

    // ************************************************************************
    // Real methods
    // ************************************************************************

    @Override
    public int compareTo(Timeslot other) {
        return new CompareToBuilder()
                .append(day, other.day)
                .append(fromTime, other.fromTime)
                .append(toTime, other.toTime)
                .toComparison();
    }

}
