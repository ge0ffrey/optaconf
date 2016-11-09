package org.optaconf.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.optaplanner.core.api.domain.solution.cloner.DeepPlanningClone;

import com.fasterxml.jackson.annotation.JsonBackReference;

@DeepPlanningClone
@Entity(name = "optaconf_talkexclusion")
public class TalkExclusion extends AbstractConferencedPersistable {

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    private Talk firstTalk;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    private Talk secondTalk;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TalkExclusionType type;

    public TalkExclusion() {
    }

    public TalkExclusion(Conference conference, String externalId, Talk firstTalk, Talk secondTalk, TalkExclusionType type) {
        super(conference, externalId);
        this.firstTalk = firstTalk;
        this.secondTalk = secondTalk;
        this.type = type;
    }

    public Talk getFirstTalk() {
        return firstTalk;
    }

    public void setFirstTalk(Talk firstTalk) {
        this.firstTalk = firstTalk;
    }

    public Talk getSecondTalk() {
        return secondTalk;
    }

    public void setSecondTalk(Talk secondTalk) {
        this.secondTalk = secondTalk;
    }

    public TalkExclusionType getType() {
        return type;
    }

    public void setType(TalkExclusionType type) {
        this.type = type;
    }

    // ************************************************************************
    // Real methods
    // ************************************************************************

}
