package org.optaconf.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.optaplanner.core.api.domain.solution.cloner.DeepPlanningClone;

@DeepPlanningClone
@Entity(name = "optaconf_room")
public class Room extends AbstractConferencedPersistable implements Comparable<Room> {

    @NotNull @Size(max = 120)
    private String name;

    @NotNull @Min(0)
    private int seatingCapacity;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Talk> talkList = new ArrayList<Talk>();

    public Room() {
    }

    public Room(Conference conference, String externalId, String name, int seatingCapacity) {
        super(conference, externalId);
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

    public List<Talk> getTalkList() {
        return talkList;
    }

    public void setTalkList(List<Talk> talkList) {
        this.talkList = talkList;
    }

    // ************************************************************************
    // Real methods
    // ************************************************************************

    @Override
    public int compareTo(Room other) {
        return new CompareToBuilder()
                .append(name, other.name)
                .append(other.seatingCapacity, seatingCapacity)
                .append(id, other.id)
                .toComparison();
    }

}
