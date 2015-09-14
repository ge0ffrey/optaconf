package org.optaconf.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.optaplanner.core.api.domain.solution.cloner.DeepPlanningClone;

@DeepPlanningClone
@Entity(name = "optaconf_talkexclusion")
public class TalkExclusion extends AbstractConferencedPersistable {

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    private Talk firstTalk;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    private Talk secondTalk;

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
