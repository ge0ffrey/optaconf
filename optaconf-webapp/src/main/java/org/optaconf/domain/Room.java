package org.optaconf.domain;

public class Room extends AbstractPersistable {

    private String name;
    private int seatingCapacity;

    public Room() {
    }

    public Room(long id, String name, int seatingCapacity) {
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

}
