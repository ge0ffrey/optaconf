package org.optaconf.domain;

/**
 * Group of tracks
 */
public class Track extends AbstractPersistable {

    private String title;

    public Track() {
    }

    public Track(String id, String title) {
        super(id);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
