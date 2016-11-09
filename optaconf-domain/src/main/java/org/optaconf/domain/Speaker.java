package org.optaconf.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.optaplanner.core.api.domain.solution.cloner.DeepPlanningClone;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@DeepPlanningClone
@Entity(name = "optaconf_speaker")
public class Speaker extends AbstractConferencedPersistable implements Comparable<Speaker> {

    @NotNull @Size(max = 120)
    private String name;
    private Boolean rockstar = false;

    @OneToMany(mappedBy = "speaker", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<SpeakingRelation> speakingRelationList;

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

    public List<SpeakingRelation> getSpeakingRelationList() {
        return speakingRelationList;
    }

    public void setSpeakingRelationList(List<SpeakingRelation> speakingRelationList) {
        this.speakingRelationList = speakingRelationList;
    }

    // ************************************************************************
    // Real methods
    // ************************************************************************

    @Override
    public int compareTo(Speaker other) {
        return new CompareToBuilder()
                .append(name, other.name)
                .append(id, other.id)
                .toComparison();
    }

}
