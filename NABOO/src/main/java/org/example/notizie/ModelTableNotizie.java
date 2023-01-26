package org.example.notizie;

public class ModelTableNotizie {
    private int id_notizia;
    private String squadra;
    private String titolo;
    private String data;
    private int like;
    private int dislike;

    public ModelTableNotizie(int id_notizia, String squadra, String titolo, String data, int like, int dislike) {
        this.id_notizia = id_notizia;
        this.squadra = squadra;
        this.titolo = titolo;
        this.data = data;
        this.like = like;
        this.dislike = dislike;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }

    public int getId() {
        return id_notizia;
    }

    public void setId(int id) {
        this.id_notizia = id;
    }

    public String getSquadra(){
        return squadra;
    }

    public void setSquadra(String squadra){
        this.squadra = squadra;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String squadra) {
        this.titolo = titolo;
    }
    public String getLinkFeed() {
        return data;
    }

    public void setLinkFeed(String linkefeed) {
        this.data = linkefeed;
    }
}


