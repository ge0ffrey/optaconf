package org.optaconf.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name="optaconf_talkexclusion")
public class TalkExclusion extends AbstractPersistable {

   @Column private Talk firstTalk;
   @Column private Talk secondTalk;

    private TalkExclusionType type;

    public TalkExclusion() {
    }

    public TalkExclusion(String id, Talk firstTalk, Talk secondTalk, TalkExclusionType type) {
        super(id);
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

}
