package org.optaconf.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.optaplanner.core.api.domain.solution.cloner.DeepPlanningClone;

import com.fasterxml.jackson.annotation.JsonBackReference;

@DeepPlanningClone
@Entity(name = "optaconf_speakingrelation")
public class SpeakingRelation extends AbstractConferencedPersistable {

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "speaker_id")
    @JsonBackReference
    private Speaker speaker;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "talk_id")
    @JsonBackReference
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
