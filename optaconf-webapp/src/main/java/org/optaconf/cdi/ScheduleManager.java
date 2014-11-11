package org.optaconf.cdi;

import java.io.Serializable;

import javax.enterprise.inject.Vetoed;

import org.optaconf.domain.Schedule;

@Vetoed
public class ScheduleManager implements Serializable {

    private Schedule schedule;

    public ScheduleManager() {
    }

    public ScheduleManager(Schedule schedule) {
        this.schedule = schedule;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

}
