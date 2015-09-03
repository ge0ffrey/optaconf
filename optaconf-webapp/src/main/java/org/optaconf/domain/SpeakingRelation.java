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
public class SpeakingRelation extends AbstractPersistable {

    @OneToOne(cascade = CascadeType.ALL)
    private Speaker speaker;

    @OneToOne(cascade = CascadeType.ALL)
    private Talk talk;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "conference_id", nullable = false)
    @JsonIgnore
    private Conference conference;

    public SpeakingRelation() {
    }

    public SpeakingRelation(String id, Talk talk, Speaker speaker, Conference conference) {
        super(id);
        this.talk = talk;
        this.speaker = speaker;
        this.conference = conference;
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

    public Conference getConference() {
        return conference;
    }

    void setConference(Conference conference) {
        this.conference = conference;
    }

}
