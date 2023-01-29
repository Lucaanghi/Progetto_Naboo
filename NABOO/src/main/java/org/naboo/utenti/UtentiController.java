package org.naboo.utenti;

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

public class UtentiController implements Initializable {
    ObservableList<ModelTableUtenti> oblista = FXCollections.observableArrayList();
    @FXML
    private Button aggiorna;
    @FXML
    private Button aggrim;
    @FXML
    private Button rimnotizie;
    @FXML
    private Button admin;
    @FXML
    private Button rim_commenti;
    @FXML
    private Button btn_blocca_per_userid;
    @FXML
    private Button btn_sblocca_per_userid;
    @FXML
    private Button imp_esp_file;
    @FXML
    private TextField inserimento_userid;
    @FXML
    private TextField inserimento_sblocco_userid;
    @FXML
    private Label label_rimosso_per_userid;
    @FXML
    private Label label_sbloccato;
    @FXML
    private TableView<ModelTableUtenti> tabella_utenti = new TableView<ModelTableUtenti>();
    @FXML
    private TableColumn<ModelTableUtenti, String> col_chatid_utenti;
    @FXML
    private TableColumn<ModelTableUtenti, String> col_nickname_utenti;
    @FXML
    private TableColumn<ModelTableUtenti, String> col_ban;

/*----------------------------------------------------------------------------------------------------*/

    //METODI PER IL CAMBIO DI PANEL
    //Funzione che cambia schermata (Gestione Utenti -> Aggiorna)
    public void switchToAggiorna(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/aggiorna_notizie"));
        Stage stage = (Stage) aggiorna.getScene().getWindow();
        stage.setScene(scene);

        stage.show();
    }

    //Funzione che cambia schermata (Gestione Utenti -> Agg./Rim. Feed)
    public void switchToAggRimFeed(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/agg_rim_feed"));
        Stage stage = (Stage) aggrim.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Gestione Utenti -> Notizie)
    public void switchToRimuoviNotizie(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/rimuovi_notizie"));
        Stage stage = (Stage) rimnotizie.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Aggiorna -> Rimuovi Commenti)
    public void switchToRimuoviCommenti(ActionEvent event) throws IOException{
        Scene scene = new Scene(loadFXML("/rimuovi_commenti"));
        Stage stage = (Stage) rim_commenti.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Gestione Utenti -> Area Admin)
    public void switchToAdmin(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/admin"));
        Stage stage = (Stage) admin.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Gestione Utenti -> Imp./Esp. File)
    public void switchToImpEspFile(ActionEvent event) throws  IOException{
        Scene scene = new Scene(loadFXML("/imp_esp_file"));
        Stage stage = (Stage) imp_esp_file.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

/*-----------------------------------------------------------------------------------*/

    /*METODI GESTIONE PAGINA DI GESIONE UTENTI*/
    //Metodo che blocca l'utente al relativo chat ID inserito dall'amministratore, settando ad 1 (true) il campo ban
    public void bloccaUtentePerUserId(ActionEvent actionEvent) {
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();

        String query = "UPDATE utenti SET ban = true WHERE chat_id = " + inserimento_userid.getText();
        session.createNativeQuery(query).executeUpdate();

        label_rimosso_per_userid.setText("Utente bloccato con successo");

        tx.commit();
        session.close();
    }

    //Metodo che sblocca l'utente al relativo chat ID inserito dall'amministratore, settando a 0 (false) il campo ban
    public void sbloccaUtentePerUserId(ActionEvent actionEvent) {
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();

        String query = "UPDATE utenti SET ban = false WHERE chat_id = " + inserimento_sblocco_userid.getText() + "";
        session.createNativeQuery(query).executeUpdate();

        label_sbloccato.setText("Utente sbloccato con successo");

        tx.commit();
        session.close();
    }

    //Caricamento della tabella contenente gli utenti presenti sul database
    public void caricaTabellaUtenti(Session session, Transaction tr){
        Query<Utente> query = session.createQuery("FROM org.naboo.utenti.Utente", Utente.class);
        List<Utente> utentilist = query.list();
        for (int i = 0; i < utentilist.size(); i++) {
            oblista.add(new ModelTableUtenti(utentilist.get(i).getChat_id(), utentilist.get(i).getNickname(), utentilist.get(i).getBan()));
        }
        col_chatid_utenti.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.valueOf(cellData.getValue().getChat_id())));
        col_nickname_utenti.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getNickname()));
        col_ban.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getBan()));
        tabella_utenti.setItems(oblista);
    }

/*-----------------------------------------------------------------------------------*/

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();

        //Chiamata della funzione per caricare la tabella feed
        caricaTabellaUtenti(session, tx);

        tx.commit();
        session.close();
    }


}
