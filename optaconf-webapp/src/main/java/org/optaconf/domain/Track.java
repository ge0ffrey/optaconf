package org.optaconf.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Group of tracks
 */
@Entity(name="optaconf_track")
public class Track extends AbstractPersistable {
   @Column
    private String title;
   @Column 
   private String cssStyleClass;

    public Track() {
    }

    public Track(String id, String title, String cssStyleClass) {
        super(id);
        this.title = title;
        this.cssStyleClass = cssStyleClass;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCssStyleClass() {
        return cssStyleClass;
    }

    public void setCssStyleClass(String cssStyleClass) {
        this.cssStyleClass = cssStyleClass;
    }

}
