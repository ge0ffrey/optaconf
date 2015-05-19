package org.optaconf.domain;

import org.optaconf.service.DayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
