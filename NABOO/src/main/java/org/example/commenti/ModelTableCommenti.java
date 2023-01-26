package org.example.commenti;

public class ModelTableCommenti {
    private int id_commento;
    private String testo;
    private String FK_notizia;
    private String FK_utente;

    public ModelTableCommenti(int id_commento, String testo, int FK_notizia, int FK_utente) {
        this.id_commento = id_commento;
        this.testo = testo;
        this.FK_notizia = String.valueOf(FK_notizia);
        this.FK_utente = String.valueOf(FK_utente);
    }

    public int getId() {
        return id_commento;
    }

    public void setId(int id) {
        this.id_commento = id;
    }

    public String getTesto(){
        return testo;
    }

    public void setTesto(String testo){
        this.testo = testo;
    }

    public String getFK_notizia() {
        return FK_notizia;
    }

    public void setFK_notizia(String FK_notizia) {
        this.FK_notizia = FK_notizia;
    }
    public String getFK_utente() {
        return FK_utente;
    }

    public void setFK_utente(String FK_utente) {
        this.FK_utente = FK_utente;
    }
}
