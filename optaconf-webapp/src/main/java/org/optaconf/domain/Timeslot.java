package org.optaconf.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity(name = "optaconf_timeslot")
public class Timeslot extends AbstractPersistable implements Comparable<Timeslot>
{
   @Column(length = 255, nullable = false)
   private String name;

   @Column(length = 255, nullable = false)
   private String fromTime;

   @Column(length = 255, nullable = false)
   private String toTime;

   @ManyToOne(cascade=CascadeType.ALL)
   @JoinColumn(name = "day_id", nullable = false)
   @JsonBackReference
   private Day day;

   @OneToMany(mappedBy="timeslot", cascade=CascadeType.ALL)
   private List<Talk> talks;
   
   @OneToOne(optional = true, cascade=CascadeType.ALL)
   @JoinColumn(name = "timeslot_room_penalty_id")
   private UnavailableTimeslotRoomPenalty penalty;

   @ManyToOne(cascade=CascadeType.ALL)
   @JoinColumn(name="schedule_id", nullable=false)
   @JsonBackReference
   private Schedule schedule;
   
   
   public Timeslot()
   {}

   public Timeslot(String id, String name, Day day, String fromTime, String toTime, Schedule schedule)
   {
      super(id);
      this.name = name;
      this.day = day;
      this.fromTime = fromTime;
      this.toTime = toTime;
      this.schedule = schedule;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public Day getDay()
   {
      return day;
   }

   public void setDay(Day day)
   {
      this.day = day;
   }

   public String getFromTime()
   {
      return fromTime;
   }

   public void setFromTime(String fromTime)
   {
      this.fromTime = fromTime;
   }

   public String getToTime()
   {
      return toTime;
   }

   public void setToTime(String toTime)
   {
      this.toTime = toTime;
   }

   @Override
   public int compareTo(Timeslot other)
   {
      return new CompareToBuilder()
               .append(day, other.day)
               .append(fromTime, other.fromTime)
               .append(toTime, other.toTime)
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

   public Schedule getSchedule()
   {
      return schedule;
   }

   public void setSchedule(Schedule schedule)
   {
      this.schedule = schedule;
   }
   
}
