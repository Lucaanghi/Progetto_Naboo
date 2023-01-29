package org.naboo.commenti;
import jakarta.persistence.*;

@Entity
@Table(name = "commenti")
public class Commento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_commento")
    private int id_commento;
    @Column(name = "testo")
    private String testo;
    @Column(name = "FK_notizia")
    private int FK_notizia;
    @Column(name = "FK_utente")
    private int FK_utente;



    public int getId_commento() {
        return id_commento;
    }

    public void setId_commento(int id_commento) {
        this.id_commento = id_commento;
    }
    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public int getFK_notizia() {
        return FK_notizia;
    }

    public void setFK_notizia(int FK_notizia) {
        this.FK_notizia = FK_notizia;
    }

    public int getFK_utente() {
        return FK_utente;
    }

    public void setFK_utente(int FK_utente) {
        this.FK_utente = FK_utente;
    }
}
