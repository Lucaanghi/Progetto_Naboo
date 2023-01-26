package org.example.feed;

import jakarta.persistence.*;

@Entity
@Table(name = "feed")
public class Feed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id_feed;
    @Column(name = "nomefeed")
    public String nomefeed;

    @Column(name = "stato")
    private boolean stato;
    @Column(name = "squadra")
    private String squadra;

    public int getId_feed() {
        return this.id_feed;
    }

    public void setId_feed(int id_feed) {
        this.id_feed = id_feed;
    }

    public String getNomefeed() {
        return this.nomefeed;
    }

    public void setNomefeed(String nomefeed) {
        this.nomefeed = nomefeed;
    }

    public boolean isStato() {
        return this.stato;
    }

    public void setStato(boolean stato) {
        this.stato = stato;
    }

    public String getSquadra() {
        return this.squadra;
    }

    public void setSquadra(String squadra) {
        this.squadra = squadra;
    }
}

