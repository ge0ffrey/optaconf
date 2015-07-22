package org.optaconf.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * Group of tracks
 */
@Entity(name = "optaconf_track")
public class Track extends AbstractPersistable
{
   @Column
   private String title;
   
   @Column
   private String cssStyleClass;
   
   @OneToMany(mappedBy="track", cascade=CascadeType.ALL)
   private List<Talk> talks = new ArrayList<Talk>();

   @ManyToOne
   @JoinColumn(name="schedule_id", nullable=false)
   private Schedule schedule;
   
   public Track()
   {}

   public Track(String id, String title, String cssStyleClass)
   {
      super(id);
      this.title = title;
      this.cssStyleClass = cssStyleClass;
   }

   public String getTitle()
   {
      return title;
   }

   public void setTitle(String title)
   {
      this.title = title;
   }

   public String getCssStyleClass()
   {
      return cssStyleClass;
   }

   public void setCssStyleClass(String cssStyleClass)
   {
      this.cssStyleClass = cssStyleClass;
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
