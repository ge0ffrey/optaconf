package org.optaconf.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
@Entity(name = "optaconf_talk")
public class Talk extends AbstractPersistable
{

   @Column(length=255, nullable=false)
   private String title;

   @ManyToOne
   @JoinColumn(name="track_id", nullable=false)
   private Track track;
   
   @ManyToOne
   @JoinColumn(name="timeslot_id", nullable=false)
   private Timeslot timeslot;
   
   @ManyToOne
   @JoinColumn(name="room_id", nullable=false)
   private Room room;
   
   @OneToOne(optional=false)
   @JoinColumn(name="speaking_relation_id", nullable=false)
   private SpeakingRelation speakingRelation;
   
   @OneToOne(optional = true)
   @JoinColumn(name = "talk_exclusion_id")
   private TalkExclusion talkExclusion;
   
   @ManyToOne
   @JoinColumn(name="schedule_id", nullable=false)
   private Schedule schedule;

   public Talk()
   {}

   public Talk(String id, String title, Track track)
   {
      super(id);
      this.title = title;
      this.track = track;
   }

   public String getTitle()
   {
      return title;
   }

   public void setTitle(String title)
   {
      this.title = title;
   }

   public Track getTrack()
   {
      return track;
   }

   public void setTrack(Track track)
   {
      this.track = track;
   }

   @PlanningVariable(valueRangeProviderRefs = { "timeslotRange" })
   public Timeslot getTimeslot()
   {
      return timeslot;
   }

   public void setTimeslot(Timeslot timeslot)
   {
      this.timeslot = timeslot;
   }

   @PlanningVariable(valueRangeProviderRefs = { "roomRange" })
   public Room getRoom()
   {
      return room;
   }

   public void setRoom(Room room)
   {
      this.room = room;
   }

   public SpeakingRelation getSpeakingRelation()
   {
      return speakingRelation;
   }

   public void setSpeakingRelation(SpeakingRelation speakingRelation)
   {
      this.speakingRelation = speakingRelation;
   }

   public TalkExclusion getTalkExclusion()
   {
      return talkExclusion;
   }

   public void setTalkExclusion(TalkExclusion talkExclusion)
   {
      this.talkExclusion = talkExclusion;
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
