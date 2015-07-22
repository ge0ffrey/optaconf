package org.optaconf.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.apache.commons.lang.builder.CompareToBuilder;

@Entity(name = "optaconf_speaker")
public class Speaker extends AbstractPersistable implements Comparable<Speaker>
{

   @Column
   private String name;
   
   @Column
   private Boolean rockstar;

   @OneToOne(optional = false)
   @JoinColumn(name = "speaking_relation_id", nullable = false)
   private SpeakingRelation relation;
   
   @ManyToOne
   @JoinColumn(name="schedule_id", nullable=false)
   private Schedule schedule;

   public Speaker()
   {}

   public Speaker(String id, String name)
   {
      super(id);
      this.name = name;
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
   
   public Schedule getSchedule()
   {
      return schedule;
   }

   public void setSchedule(Schedule schedule)
   {
      this.schedule = schedule;
   }

}
