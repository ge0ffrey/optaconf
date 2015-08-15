package org.optaconf.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "optaconf_talkexclusion")
public class TalkExclusion extends AbstractPersistable
{

   @OneToOne(cascade=CascadeType.ALL)
   @JsonBackReference
   private Talk firstTalk;
   
   @OneToOne(cascade=CascadeType.ALL)
   @JsonBackReference
   private Talk secondTalk;
   
   @Enumerated(EnumType.STRING)
   private TalkExclusionType type;
   
   @ManyToOne(cascade=CascadeType.ALL)
   @JoinColumn(name="schedule_id", nullable=false)
   @JsonBackReference
   private Conference schedule;

   public TalkExclusion()
   {}

   public TalkExclusion(String id, Talk firstTalk, Talk secondTalk, TalkExclusionType type, Conference schedule)
   {
      super(id);
      this.firstTalk = firstTalk;
      this.secondTalk = secondTalk;
      this.type = type;
      this.schedule = schedule;
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

   public Conference getSchedule()
   {
      return schedule;
   }

   public void setSchedule(Conference schedule)
   {
      this.schedule = schedule;
   }

}
