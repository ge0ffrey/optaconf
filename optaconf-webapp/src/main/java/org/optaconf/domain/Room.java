package org.optaconf.domain;

import org.apache.commons.lang.builder.CompareToBuilder;

public class Room extends AbstractPersistable implements Comparable<Room> {

    private String name;
    private int seatingCapacity;

    public Room() {
    }

    public Room(String id, String name, int seatingCapacity) {
        super(id);
        this.name = name;
        this.seatingCapacity = seatingCapacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(int seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    @Override
    public int compareTo(Room other) {
        return new CompareToBuilder()
                .append(name, other.name)
                .append(other.seatingCapacity, seatingCapacity)
                .toComparison();
    }

}
