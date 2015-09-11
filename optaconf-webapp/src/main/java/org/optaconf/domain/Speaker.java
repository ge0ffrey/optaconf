package org.optaconf.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.optaplanner.core.api.domain.solution.cloner.DeepPlanningClone;

@DeepPlanningClone
@Entity(name = "optaconf_speaker")
public class Speaker extends AbstractConferencedPersistable implements Comparable<Speaker> {

    @Column
    private String name;

    @Column
    private Boolean rockstar = false;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "speaking_relation_id", nullable = true)
    private SpeakingRelation relation;

    public Speaker() {
    }

    public Speaker(Conference conference, String externalId, String name) {
        super(conference, externalId);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public Boolean getRockstar() {
        return rockstar;
    }

    public void setRockstar(Boolean rockstar) {
        this.rockstar = rockstar;
    }

    public SpeakingRelation getRelation() {
        return relation;
    }

    public void setRelation(SpeakingRelation relation) {
        this.relation = relation;
    }

    // ************************************************************************
    // Real methods
    // ************************************************************************

    @Override
    public int compareTo(Speaker other) {
        return new CompareToBuilder()
                .append(name, other.name)
                .toComparison();
    }

}
