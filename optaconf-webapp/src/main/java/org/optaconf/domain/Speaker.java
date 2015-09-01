package org.optaconf.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity(name = "optaconf_speaker")
public class Speaker extends AbstractPersistable implements Comparable<Speaker>
{

   @Column
   private String name;
   
   @Column
   private Boolean rockstar = false;

   @OneToOne(optional = true, cascade=CascadeType.ALL)
   @JoinColumn(name = "speaking_relation_id", nullable = true)
   private SpeakingRelation relation;
   
   @ManyToOne(cascade=CascadeType.ALL)
   @JoinColumn(name="conference_id", nullable=false)
   @JsonIgnore
   private Conference conference;

   public Speaker()
   {}

   public Speaker(String id, String name, Conference conference)
   {
      super(id);
      this.name = name;
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

   @Override
   public String toString()
   {
      return name;
   }

   public Boolean getRockstar()
   {
      return rockstar;
   }

   public void setRockstar(Boolean rockstar)
   {
      this.rockstar = rockstar;
   }

   @Override
   public int compareTo(Speaker other)
   {
      return new CompareToBuilder()
               .append(name, other.name)
               .toComparison();
   }

   public SpeakingRelation getRelation()
   {
      return relation;
   }

   public void setRelation(SpeakingRelation relation)
   {
      this.relation = relation;
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
