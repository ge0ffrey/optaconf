package org.optaconf.domain;

public class TalkRelationship extends AbstractPersistable {

    private Talk firstTalk;
    private Talk secondTalk;

    private TalkRelationshipType type;

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

    public TalkRelationshipType getType() {
        return type;
    }

    public void setType(TalkRelationshipType type) {
        this.type = type;
    }

}
