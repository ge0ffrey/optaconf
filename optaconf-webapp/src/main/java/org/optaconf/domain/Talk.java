package org.optaconf.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@PlanningEntity
@Entity(name = "optaconf_talk")
public class Talk extends AbstractPersistable
{

   @Column(length=255, nullable=false)
   private String title;

   @ManyToOne()
   @JoinColumn(name="track_id", nullable=false)
   private Track track;
   
   @ManyToOne()
   @JoinColumn(name="timeslot_id", nullable=false)
   @JsonBackReference
   private Timeslot timeslot;
   
   @ManyToOne()
   @JoinColumn(name="room_id", nullable=true)
   @JsonBackReference
   private Room room;
   
   @OneToOne(optional=true, cascade=CascadeType.ALL)
   @JoinColumn(name="speaking_relation_id", nullable=true)
   private SpeakingRelation speakingRelation;
   
   @OneToOne(optional = true, cascade=CascadeType.ALL)
   @JoinColumn(name = "talk_exclusion_id")
   private TalkExclusion talkExclusion;
   
   @ManyToOne()
   @JoinColumn(name="conference_id", nullable=false)
   @JsonBackReference
   private Conference conference;

   public Talk()
   {}

   public Talk(String id, String title, Conference conference, Room room, Track track, Timeslot timeslot)
   {
      super(id);
      this.title = title;
      this.track = track;
      this.conference = conference;
      this.room = room;
      this.timeslot = timeslot;
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
   
   public Conference getConference()
   {
      return conference;
   }

   public void setConference(Conference conference)
   {
      this.conference = conference;
   }

}
