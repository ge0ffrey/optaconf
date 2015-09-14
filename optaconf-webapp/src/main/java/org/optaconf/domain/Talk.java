package org.optaconf.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.solution.cloner.DeepPlanningClone;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
@DeepPlanningClone
@Entity(name = "optaconf_talk")
public class Talk extends AbstractConferencedPersistable {

    @NotNull @Size(max = 120)
    private String title;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "track_id")
    private Track track;

    @PlanningVariable(valueRangeProviderRefs = {"timeslotRange"})
    @ManyToOne()
    @JoinColumn(name = "timeslot_id")
    @JsonBackReference
    private Timeslot timeslot;

    @PlanningVariable(valueRangeProviderRefs = {"roomRange"})
    @ManyToOne()
    @JoinColumn(name = "room_id")
    @JsonBackReference
    private Room room;

    @OneToMany(mappedBy = "talk", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<SpeakingRelation> speakingRelationList;

    public Talk() {
    }

    public Talk(Conference conference, String externalId, String title, Track track) {
        super(conference, externalId);
        this.title = title;
        this.track = track;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public List<SpeakingRelation> getSpeakingRelationList() {
        return speakingRelationList;
    }

    public void setSpeakingRelationList(List<SpeakingRelation> speakingRelationList) {
        this.speakingRelationList = speakingRelationList;
    }

    // ************************************************************************
    // Real methods
    // ************************************************************************

}
