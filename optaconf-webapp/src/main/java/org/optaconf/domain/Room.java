package org.optaconf.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.optaplanner.core.api.domain.solution.cloner.DeepPlanningClone;

@DeepPlanningClone
@Entity(name = "optaconf_room")
public class Room extends AbstractPersistable implements Comparable<Room>
{

   @Column
   private String name;

   @Column
   private int seatingCapacity;

   @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   @JsonManagedReference
   private List<Talk> talks = new ArrayList<Talk>();

   @OneToOne(optional = true, cascade=CascadeType.ALL, fetch = FetchType.EAGER)
   @JoinColumn(name = "timeslot_room_penalty_id")
   private UnavailableTimeslotRoomPenalty penalty;

   @ManyToOne(cascade=CascadeType.ALL)
   @JoinColumn(name = "conference_id", nullable = false)
   @JsonIgnore
   private Conference conference;

   public Room()
   {}

   public Room(String id, String name, int seatingCapacity, Conference conference)
   {
      super(id);
      this.name = name;
      this.seatingCapacity = seatingCapacity;
      this.conference = conference;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public int getSeatingCapacity()
   {
      return seatingCapacity;
   }

   public void setSeatingCapacity(int seatingCapacity)
   {
      this.seatingCapacity = seatingCapacity;
   }

   @Override
   public int compareTo(Room other)
   {
      return new CompareToBuilder()
               .append(name, other.name)
               .append(other.seatingCapacity, seatingCapacity)
               .toComparison();
   }

   public List<Talk> getTalks()
   {
      return talks;
   }

   public void setTalks(List<Talk> talks)
   {
      this.talks = talks;
   }

   public UnavailableTimeslotRoomPenalty getPenalty()
   {
      return penalty;
   }

   public void setPenalty(UnavailableTimeslotRoomPenalty penalty)
   {
      this.penalty = penalty;
   }

   public Conference getConference()
   {
      return conference;
   }

   public void setConference(Conference conference)
   {
      this.conference = conference;
   }

}
