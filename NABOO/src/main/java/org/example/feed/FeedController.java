package org.example.feed;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.database.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static org.example.pannelloFx.MainJFx.loadFXML;

public class FeedController implements Initializable {
    ObservableList<ModelTableFeed> oblista = FXCollections.observableArrayList();
    Label lblValue = new Label();
    @FXML
    private Button aggiorna;
    @FXML
    private Button aggrim;
    @FXML
    private Button rimnotizie;
    @FXML
    private Button admin;
    @FXML
    private Button gestutenti;
    @FXML
    private Button rim_commenti;
    @FXML
    private TableView<ModelTableFeed> tabella = new TableView<ModelTableFeed>();
    @FXML
    private TableColumn<ModelTableFeed, String> col_id_feed;
    @FXML
    private TableColumn<ModelTableFeed, String> col_squadra;
    @FXML
    private TableColumn<ModelTableFeed, String> col_nomefeed;
    @FXML
    private TextField rim_squadra;
    @FXML
    private TextField label_agg_squadra;
    @FXML
    private TextField label_agg_link_feed;
    @FXML
    private Label alert_rimosso;
    @FXML
    private Label alert_aggiunto;
    @FXML
    private Button rimuovi;
    @FXML
    private Button imp_esp_file;

    //METODI PER IL CAMBIO DI PANEL
    //Funzione che cambia schermata (Agg./Rim. Feed -> Aggiorna)
    public void switchToAggiorna(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/aggiorna_notizie"));
        Stage stage = (Stage) aggiorna.getScene().getWindow();
        stage.setScene(scene);

        stage.show();
    }

    //Funzione che cambia schermata (Agg./Rim. Feed -> Notizie)
    public void switchToRimuoviNotizie(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/rimuovi_notizie"));
        Stage stage = (Stage) rimnotizie.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Agg./Rim. Feed -> Area Admin)
    public void switchToAdmin(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/admin"));
        Stage stage = (Stage) admin.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Agg./Rim. Feed -> Gestisci Utenti)
    public void switchToGestisciUtenti(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/gestisci_utenti"));
        Stage stage = (Stage) gestutenti.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Agg./Rim. Feed -> Rimuovi Commenti)
    public void switchToRimuoviCommenti(ActionEvent event) throws IOException{
        Scene scene = new Scene(loadFXML("/rimuovi_commenti"));
        Stage stage = (Stage) rim_commenti.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Agg./Rim. Feed -> Imp./Esp. File)
    public void switchToImpEspFile(ActionEvent event) throws  IOException{
        Scene scene = new Scene(loadFXML("/imp_esp_file"));
        Stage stage = (Stage) imp_esp_file.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

/*-----------------------------------------------------------------------------------*/

    //METODI GESTIONE PAGINA AGG./RIM. FEED
    //Metodo che rimuove il feed inserito dal database
    public void rimuoviFeed(ActionEvent event) throws IOException {
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("DELETE org.example.feed.Feed WHERE squadra = '" + rim_squadra.getText() + "'").executeUpdate();
        //DA FARE: aggiornare tabella al premere del tasto
        alert_rimosso.setText("FEED rimosso con successo");
        tx.commit();
        session.close();
    }

    //Metodo che aggiunge il feed inserito sul database
    public void aggiungiFeed(ActionEvent event) {
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();
        Feed f1 = new Feed();
        f1.setSquadra(label_agg_squadra.getText());
        f1.setNomefeed(label_agg_link_feed.getText());
        session.persist(f1);
        //DA FARE: aggiornare tabella al premere del tasto
        alert_aggiunto.setText("FEED aggiunto con successo");
        tx.commit();
        session.close();
    }

    //VEDERE
    public void leggiFeed() {
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("SELECT org.example.feed WHERE ", Feed.class);
        query.executeUpdate();
        tx.commit();
        session.close();
    }

    //Caricamento della tabella contenente i feed presenti sul database
    public void caricaTabellaFeed(Session session, Transaction tr){
        Query<Feed> query = session.createQuery("FROM org.example.feed.Feed", Feed.class);
        List<Feed> feed = query.list();
        for (int i = 0; i < feed.size(); i++) {
            oblista.add(new ModelTableFeed(feed.get(i).getId_feed(), feed.get(i).getSquadra(), feed.get(i).getNomefeed()));
        }
        col_id_feed.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.valueOf(cellData.getValue().getId())));
        col_squadra.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getSquadra()));
        col_nomefeed.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getLinkFeed()));
        tabella.setItems(oblista);
    }

/*-----------------------------------------------------------------------------------*/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();

        //Chiamata della funzione per caricare la tabella feed
        caricaTabellaFeed(session, tx);

        tx.commit();
        session.close();
    }
}
