package org.optaconf.domain.speaker;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.optaconf.domain.AbstractPersistable;

public class Speaker extends AbstractPersistable implements Comparable<Speaker> {

    private String name;

    public Speaker() {
    }

    public Speaker(String id, String name) {
        super(id);
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

    @Override
    public int compareTo(Speaker other) {
        return new CompareToBuilder()
                .append(name, other.name)
                .toComparison();
    }

}
