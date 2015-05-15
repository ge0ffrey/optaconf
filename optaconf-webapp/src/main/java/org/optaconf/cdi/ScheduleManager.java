package org.optaconf.cdi;

import java.io.Serializable;

import javax.enterprise.inject.Vetoed;

import org.optaconf.domain.Schedule;
import org.optaplanner.core.api.solver.Solver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Vetoed
public class ScheduleManager implements Serializable {

	private static final Logger LOG = LoggerFactory.getLogger(ScheduleManager.class);
	
    private Schedule schedule;
    private Solver solver;

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

    public Solver getSolver() {
        return solver;
    }

    public void setSolver(Solver solver) {
        this.solver = solver;
    }

}
