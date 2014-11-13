package org.optaconf.domain;

/**
 * Group of tracks
 */
public class Track extends AbstractPersistable {

    private String title;
    private String colorHex;

    public Track() {
    }

    public Track(String id, String title, String colorHex) {
        super(id);
        this.title = title;
        this.colorHex = colorHex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

}
