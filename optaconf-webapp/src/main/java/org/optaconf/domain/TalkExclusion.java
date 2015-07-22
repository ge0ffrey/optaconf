package org.optaconf.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity(name = "optaconf_talkexclusion")
public class TalkExclusion extends AbstractPersistable
{

   @OneToOne
   private Talk firstTalk;
   
   @OneToOne
   private Talk secondTalk;
   
   @Enumerated(EnumType.STRING)
   private TalkExclusionType type;
   
   @ManyToOne
   @JoinColumn(name="schedule_id", nullable=false)
   private Schedule schedule;

   public TalkExclusion()
   {}

   public TalkExclusion(String id, Talk firstTalk, Talk secondTalk, TalkExclusionType type)
   {
      super(id);
      this.firstTalk = firstTalk;
      this.secondTalk = secondTalk;
      this.type = type;
   }

   public Talk getFirstTalk()
   {
      return firstTalk;
   }

   public void setFirstTalk(Talk firstTalk)
   {
      this.firstTalk = firstTalk;
   }

   public Talk getSecondTalk()
   {
      return secondTalk;
   }

   public void setSecondTalk(Talk secondTalk)
   {
      this.secondTalk = secondTalk;
   }

   public TalkExclusionType getType()
   {
      return type;
   }

   public void setType(TalkExclusionType type)
   {
      this.type = type;
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
