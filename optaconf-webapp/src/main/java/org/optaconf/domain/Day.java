package org.optaconf.domain;

public class Day extends AbstractPersistable {

    private String name;

    public Day() {
    }

    public Day(long id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
