package org.optaconf.domain;

public class Timeslot extends AbstractPersistable {

    private String name;
    private Day day;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

}
