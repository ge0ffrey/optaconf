package org.optaconf.cdi;

import java.io.Serializable;

import javax.enterprise.inject.Vetoed;

import org.optaconf.domain.Conference;
import org.optaplanner.core.api.solver.Solver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Vetoed
public class ScheduleManager implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleManager.class);

    private Conference schedule;
    private Solver solver;

    public ScheduleManager() {
    }

    public ScheduleManager(Conference schedule) {
        this.schedule = schedule;
    }

    public Conference getSchedule() {
        return schedule;
    }

    public void setSchedule(Conference schedule) {
        this.schedule = schedule;
    }

    public Solver getSolver() {
        return solver;
    }

    public void setSolver(Solver solver) {
        this.solver = solver;
    }

}
