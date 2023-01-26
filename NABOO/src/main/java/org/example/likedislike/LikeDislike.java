package org.example.likedislike;

import jakarta.persistence.*;

@Entity
@Table(name = "likedislike")
public class LikeDislike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_likeDislike")
    private int id_likeDislike;
    @Column(name = "likeDislike")
    private int likeDislike;
    @Column(name = "FK_utente")
    private int FK_utente;
    @Column(name = "FK_notizia")
    private int FK_notizie;

    public int getId_likeDislike() {
        return id_likeDislike;
    }

    public void setId_likeDislike(int id_likeDislike) {
        this.id_likeDislike = id_likeDislike;
    }

    public int getLikeDislike() {
        return likeDislike;
    }

    public void setLikeDislike(int likeDislike) {
        this.likeDislike = likeDislike;
    }

    public int getFK_utente() {
        return FK_utente;
    }

    public void setFK_utente(int FK_utente) {
        this.FK_utente = FK_utente;
    }

    public int getFK_notizie() {
        return FK_notizie;
    }

    public void setFK_notizie(int FK_notizia) {
        this.FK_notizie = FK_notizia;
    }
}

