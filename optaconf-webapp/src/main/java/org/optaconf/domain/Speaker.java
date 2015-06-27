package org.optaconf.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.CompareToBuilder;

@Entity(name="optaconf_speaker")
public class Speaker extends AbstractPersistable implements Comparable<Speaker> {

   @Column private String name;
   @Column private Boolean rockstar;

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
    
    public Boolean getRockstar() {
		return rockstar;
	}

	public void setRockstar(Boolean rockstar) {
		this.rockstar = rockstar;
	}

	@Override
    public int compareTo(Speaker other) {
        return new CompareToBuilder()
                .append(name, other.name)
                .toComparison();
    }

}
