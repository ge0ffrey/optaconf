package org.optaconf.domain.speaker;

import org.optaconf.domain.AbstractPersistable;
import org.optaconf.domain.Talk;

public class SpeakingRelation extends AbstractPersistable {

    private Speaker speaker;
    private Talk talk;

    public SpeakingRelation() {
    }

    public SpeakingRelation(String id, Talk talk, Speaker speaker) {
        super(id);
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

}
