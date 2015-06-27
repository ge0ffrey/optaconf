package org.optaconf.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name="optaconf_day")
public class Day extends AbstractPersistable implements Comparable<Day> {

    @Column
    private String name;
    @Column
    private String date;

    public Day() {
    }

    public Day(String id, String name, String date) {
        super(id);
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int compareTo(Day other) {
        return date.compareTo(other.date);
    }

}
