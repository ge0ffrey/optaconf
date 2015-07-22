package org.optaconf.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.apache.commons.lang.builder.CompareToBuilder;

@Entity(name = "optaconf_timeslot")
public class Timeslot extends AbstractPersistable implements Comparable<Timeslot>
{

   @Column(length = 255, nullable = false)
   private String name;

   @Column(length = 255, nullable = false)
   private String fromTime;

   @Column(length = 255, nullable = false)
   private String toTime;

   @ManyToOne
   @JoinColumn(name = "day_id", nullable = false)
   private Day day;

   @ManyToOne
   @JoinColumn(name = "talk_id", nullable = false)
   private Talk talk;
   
   @OneToOne(optional = false)
   @JoinColumn(name = "timeslot_room_penalty_id")
   private UnavailableTimeslotRoomPenalty penalty;

   @ManyToOne
   @JoinColumn(name="schedule_id", nullable=false)
   private Schedule schedule;
   
   public Timeslot()
   {}

   public Timeslot(String id, String name, Day day, String fromTime, String toTime)
   {
      super(id);
      this.name = name;
      this.day = day;
      this.fromTime = fromTime;
      this.toTime = toTime;
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

   public Talk getTalk()
   {
      return talk;
   }

   public void setTalk(Talk talk)
   {
      this.talk = talk;
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
