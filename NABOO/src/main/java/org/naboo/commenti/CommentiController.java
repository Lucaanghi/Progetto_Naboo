package org.naboo.commenti;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.naboo.database.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static org.naboo.pannelloFx.MainJFx.loadFXML;

public class CommentiController implements Initializable {
    ObservableList<ModelTableCommenti> oblista = FXCollections.observableArrayList();
    Label lblValue = new Label();
    @FXML
    private Button aggiorna;
    @FXML
    private Button aggrim;
    @FXML
    private Button rimnotizie;
    @FXML
    private Button imp_esp_file;
    @FXML
    private Button admin;
    @FXML
    private Button gestutenti;
    @FXML
    private TableView<ModelTableCommenti> tabellacommenti = new TableView<ModelTableCommenti>();
    @FXML
    private TableColumn<ModelTableCommenti, String> col_id_commento;
    @FXML
    private TableColumn<ModelTableCommenti, String> col_testo;
    @FXML
    private TableColumn<ModelTableCommenti, String> col_id_notizia;
    @FXML
    private TableColumn<ModelTableCommenti, String> col_utente;
    @FXML
    private TextField lable_rimuovi_per_id;
    @FXML
    private TextField lable_rimuovi_per_notizia;
    @FXML
    private TextField lable_rimuovi_per_utente;
    @FXML
    private Label label_rimosso_comm_notizia;
    @FXML
    private Label label_rimosso_comm_id;
    @FXML
    private Label label_rimosso_comm_utente;
    @FXML
    private Button btn_rimuovi_commenti_notizia;
    @FXML
    private Button btn_rimuovi_commenti_utente;
    @FXML
    private Button btn_rimuovi_commenti;

/*----------------------------------------------------------------------------------------------------*/

    //METODI PER IL CAMBIO DI PANEL
    //Funzione che cambia schermata (Rim. commenti -> Aggiorna)
    public void switchToAggiorna(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/aggiorna_notizie"));
        Stage stage = (Stage) aggiorna.getScene().getWindow();
        stage.setScene(scene);

        stage.show();
    }

    //Funzione che cambia schermata (Rim. commenti -> Agg./Rim. Feed)
    public void switchToAggRimFeed(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/agg_rim_feed"));
        Stage stage = (Stage) aggrim.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Rim. commenti -> Notizie)
    public void switchToRimuoviNotizie(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/rimuovi_notizie"));
        Stage stage = (Stage) rimnotizie.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Rim. commenti -> Area Admin)
    public void switchToAdmin(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/admin"));
        Stage stage = (Stage) admin.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Rim. commenti -> Gestisci Utenti)
    public void switchToGestisciUtenti(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/gestisci_utenti"));
        Stage stage = (Stage) gestutenti.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Rim. commenti -> Imp./Esp. File)
    public void switchToImpEspFile(ActionEvent event) throws  IOException{
        Scene scene = new Scene(loadFXML("/imp_esp_file"));
        Stage stage = (Stage) imp_esp_file.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /*-----------------------------------------------------------------------------------*/

    /*METODI GESTIONE PAGINA COMMENTI*/
    //Funzione che rimuove i commenti secondo un id inserito
    public void rimuoviCommento(ActionEvent event) throws IOException {
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("DELETE org.naboo.commenti.Commento WHERE id = '" + lable_rimuovi_per_id.getText() + "'").executeUpdate();
        label_rimosso_comm_id.setText("Commento rimosso per ID");
        tx.commit();
        session.close();
    }

    //Funzione che rimuove i commenti secondo la notizia di riferimento inserita
    public void rimuoviCommentoPerNotizia(ActionEvent event) {
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("DELETE org.naboo.commenti.Commento WHERE FK_notizia = " + lable_rimuovi_per_notizia.getText()).executeUpdate();
        label_rimosso_comm_notizia.setText("Commenti rimossi per notizia");
        tx.commit();
        session.close();
    }

    //Funzione che rimuove i commenti secondo l'utente di riferimento inserito
    public void rimuoviCommentoPerUtente(ActionEvent event) {
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("DELETE org.naboo.commenti.Commento WHERE FK_utente = " + lable_rimuovi_per_utente.getText()).executeUpdate();
        label_rimosso_comm_utente.setText("Commenti rimossi per utente");
        tx.commit();
        session.close();
    }

    //Caricamento della tabella contenente le notizie presenti sul database
    public void caricaTabellaCommenti(Session session, Transaction tx){
        Query<Commento> query = session.createQuery("FROM org.naboo.commenti.Commento", Commento.class);
        List<Commento> listaCommenti = query.list();
        for (int i = 0; i < listaCommenti.size(); i++) {
            oblista.add(new ModelTableCommenti(listaCommenti.get(i).getId_commento(), listaCommenti.get(i).getTesto(), listaCommenti.get(i).getFK_notizia(), listaCommenti.get(i).getFK_utente()));
        }
        col_id_commento.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.valueOf(cellData.getValue().getId())));
        col_testo.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.valueOf(cellData.getValue().getTesto())));
        col_id_notizia.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getFK_notizia()));
        col_utente.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getFK_utente()));
        tabellacommenti.setItems(oblista);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();

        //Chiamata della funzione per caricare la tabella notizie
        caricaTabellaCommenti(session, tx);

        tx.commit();
        session.close();
    }
}