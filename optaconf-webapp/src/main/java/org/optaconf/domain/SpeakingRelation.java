package org.optaconf.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.optaplanner.core.api.domain.solution.cloner.DeepPlanningClone;

@DeepPlanningClone
@Entity(name = "optaconf_speakingrelation")
public class SpeakingRelation extends AbstractConferencedPersistable {

    @OneToOne(cascade = CascadeType.ALL)
    private Speaker speaker;

    @OneToOne(cascade = CascadeType.ALL)
    private Talk talk;

    public SpeakingRelation() {
    }

    public SpeakingRelation(Conference conference, String externalId, Talk talk, Speaker speaker) {
        super(conference, externalId);
        this.talk = talk;
        this.speaker = speaker;
    }

    public Speaker getSpeaker() {
        return speaker;
    }

    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }

    public Talk getTalk() {
        return talk;
    }

    public void setTalk(Talk talk) {
        this.talk = talk;
    }

    // ************************************************************************
    // Real methods
    // ************************************************************************

}
