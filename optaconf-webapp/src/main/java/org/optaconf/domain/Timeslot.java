package org.optaconf.domain;

public class Timeslot extends AbstractPersistable {

    private String name;
    private Day day;
    private String fromTime;
    private String toTime;

    public Timeslot() {
    }

    public Timeslot(String id, String name, Day day, String fromTime, String toTime) {
        super(id);
        this.name = name;
        this.day = day;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

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

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }
}
