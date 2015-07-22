package org.optaconf.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity(name = "optaconf_speakingrelation")
public class SpeakingRelation extends AbstractPersistable
{

   @OneToOne
   private Speaker speaker;
   @OneToOne
   private Talk talk;
   
   @ManyToOne
   @JoinColumn(name="schedule_id", nullable=false)
   private Schedule schedule;

   public SpeakingRelation()
   {}

   public SpeakingRelation(String id, Talk talk, Speaker speaker)
   {
      super(id);
      this.talk = talk;
      this.speaker = speaker;
   }

   public Speaker getSpeaker()
   {
      return speaker;
   }

   public void setSpeaker(Speaker speaker)
   {
      this.speaker = speaker;
   }

   public Talk getTalk()
   {
      return talk;
   }

   public void setTalk(Talk talk)
   {
      this.talk = talk;
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
