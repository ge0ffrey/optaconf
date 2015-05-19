package org.optaconf.domain;

/**
 * Group of tracks
 */
public class Track extends AbstractPersistable {

    private String title;
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
