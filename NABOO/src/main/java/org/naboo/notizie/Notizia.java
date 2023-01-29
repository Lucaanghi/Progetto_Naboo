package org.naboo.notizie;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notizie")
public class Notizia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int notizie_id;
    @Column(name = "squadra")
    private String squadra;
    @Column(name = "titolo", unique = true)
    private String titolo;
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    @Column(name = "descrizione")
    private String descrizione;
    @Column(name = "autore")
    private String autore;
    @Column(name = "fonte")
    private String fonte;
    @Column(name = "link")
    private String link;

    public int getNotizie_id() {
        return this.notizie_id;
    }
    public void setNotizie_id(int notizie_id) {
        this.notizie_id = notizie_id;
    }

    public String getSquadra() {
        return this.squadra;
    }

    public void setSquadra(String squadra) {
        this.squadra = squadra;
    }

    public String getTitolo() {
        return this.titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getAutore() {
        return this.autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getFonte() {
        return this.fonte;
    }

    public void setFonte(String fonte) {
        this.fonte = fonte;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return  "NUMERO NOTIZIA: " + getNotizie_id() + "\n" +
                "TITOLO: " + getTitolo() + "\n" +
                "DATA: " + getTimestamp() + "\n" +
                "DESCRIZIONE: " + getDescrizione() + "\n" +
                "AUTORE: " + getAutore() + "\n" +
                "FONTE: " + getFonte() + "\n" +
                "LINK: " + getLink() + "\n" + "\n";
    }
}
