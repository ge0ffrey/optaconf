package org.optaconf.cdi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;

import org.optaconf.domain.Day;
import org.optaconf.domain.Room;
import org.optaconf.domain.Schedule;
import org.optaconf.domain.Talk;
import org.optaconf.domain.TalkExclusion;
import org.optaconf.domain.TalkExclusionType;
import org.optaconf.domain.Timeslot;
import org.optaplanner.core.api.solver.SolverFactory;

@ApplicationScoped
public class SolverFactoryProducer implements Serializable {

    @Produces @ApplicationScoped
    public SolverFactory createSolverFactory() {
        SolverFactory solverFactory = SolverFactory.createFromXmlResource("org/optaconf/solver/solverConfig.xml");
        return solverFactory;
    }

}
