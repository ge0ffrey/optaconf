package org.optaconf.domain;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.optaplanner.core.api.domain.solution.cloner.DeepPlanningClone;

/**
 * Group of tracks
 */
@DeepPlanningClone
@Entity(name = "optaconf_track")
public class Track extends AbstractConferencedPersistable {

    @NotNull @Size(max = 120)
    private String title;

    @NotNull
    private String cssStyleClass;

    public Track() {
    }

    public Track(Conference conference, String externalId, String title, String cssStyleClass) {
        super(conference, externalId);
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

    // ************************************************************************
    // Real methods
    // ************************************************************************

}
