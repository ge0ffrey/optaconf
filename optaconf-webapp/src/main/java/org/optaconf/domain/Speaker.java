package org.optaconf.domain;

public class Speaker extends AbstractPersistable {

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

}
