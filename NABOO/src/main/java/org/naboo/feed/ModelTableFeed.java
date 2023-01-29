package org.naboo.feed;

public class ModelTableFeed {
    private int id_feed;
    private String nomefeed;
    private String squadra;

    public ModelTableFeed(int id_feed, String squadra, String nomefeed) {
        this.id_feed = id_feed;
        this.nomefeed = nomefeed;
        this.squadra = squadra;
    }

    public int getId() {
        return id_feed;
    }

    public void setId(int id) {
        this.id_feed = id;
    }

    public String getSquadra() {
        return squadra;
    }

    public void setSquadra(String squadra) {
        this.squadra = squadra;
    }

    public String getLinkFeed() {
        return nomefeed;
    }

    public void setLinkFeed(String linkefeed) {
        this.nomefeed = linkefeed;
    }
}

