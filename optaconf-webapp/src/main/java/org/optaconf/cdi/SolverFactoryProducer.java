package org.optaconf.cdi;

import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.optaconf.domain.Conference;
import org.optaplanner.core.api.solver.SolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class SolverFactoryProducer implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(SolverFactoryProducer.class);

    @Produces
    @ApplicationScoped
    public SolverFactory<Conference> createSolverFactory() {
        return SolverFactory.createFromXmlResource("org/optaconf/planner/solverConfig.xml");
    }

}
