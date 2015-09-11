package org.optaconf.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.optaplanner.core.api.domain.solution.cloner.DeepPlanningClone;

@DeepPlanningClone
@Entity(name = "optaconf_day")
public class Day extends AbstractConferencedPersistable implements Comparable<Day> {

    @Column(length = 255, nullable = false)
    private String name;
    @Column(length = 255, nullable = false)
    private String date;

    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Timeslot> timeslots = new ArrayList<Timeslot>();

    protected Day() {}

    public Day(Conference conference, String externalId, String name, String date) {
        super(conference, externalId);
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Timeslot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(List<Timeslot> timeslots) {
        this.timeslots = timeslots;
    }

    // ************************************************************************
    // Real methods
    // ************************************************************************

    @Override
    public int compareTo(Day other) {
        return date.compareTo(other.date);
    }

}
