package org.optaconf.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "optaconf_speakingrelation")
public class SpeakingRelation extends AbstractPersistable
{

   @OneToOne(cascade=CascadeType.ALL)
   private Speaker speaker;
   
   @OneToOne(cascade=CascadeType.ALL)
   private Talk talk;
   
   @ManyToOne(cascade=CascadeType.MERGE)
   @JoinColumn(name="conference_id", nullable=false)
   @JsonIgnore
   private Conference conference;

   public SpeakingRelation()
   {}

   public SpeakingRelation(String id, Talk talk, Speaker speaker, Conference conference)
   {
      super(id);
      this.talk = talk;
      this.speaker = speaker;
      this.conference = conference;
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
   
   public Conference getConference()
   {
      return conference;
   }

   void setConference(Conference conference)
   {
      this.conference = conference;
   }

}
