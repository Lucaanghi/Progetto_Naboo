package org.naboo.notizie;

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
import org.naboo.likedislike.LikeDislikeController;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static org.naboo.pannelloFx.MainJFx.loadFXML;

public class NotizieController implements Initializable {
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
    private Button imp_esp_file;
    @FXML
    private TextField lable_rimuovi_per_id;
    @FXML
    private TextField lable_rimuovi_per_data_inizio;
    @FXML
    private TextField lable_rimuovi_per_data_fine;
    @FXML
    private TextField lable_rimuovi_per_squadra;
    ObservableList<ModelTableNotizie> oblista = FXCollections.observableArrayList();
    @FXML
    private TableView<ModelTableNotizie> tabellanotizie = new TableView<ModelTableNotizie>();
    @FXML
    private TableColumn<ModelTableNotizie, String> col_id_notizia;
    @FXML
    private TableColumn<ModelTableNotizie, String> col_squadra;
    @FXML
    private TableColumn<ModelTableNotizie, String> col_titolo;
    @FXML
    private TableColumn<ModelTableNotizie, String> col_data;
    @FXML
    private TableColumn<ModelTableNotizie, String> col_like;
    @FXML
    private TableColumn<ModelTableNotizie, String> col_dislike;

    @FXML
    private Label label_notizia_rimossa_squadra;
    @FXML
    private Label label_notizia_rimossa_data;
    @FXML
    private Label label_notizia_rimossa_id;

/*----------------------------------------------------------------------------------------------------*/

    /*METODI PER IL CAMBIO DI PANEL*/
    //Funzione che cambia schermata (Notizie -> Aggiorna)
    public void switchToAggiorna(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/aggiorna_notizie"));
        Stage stage = (Stage) aggiorna.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Notizie -> Agg./Rim. Feed)
    public void switchToAggRimFeed(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/agg_rim_feed"));
        Stage stage = (Stage) aggrim.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Notizie -> Area Admin)
    public void switchToAdmin(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/admin"));
        Stage stage = (Stage) admin.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Notizie -> Gestisci Utenti)
    public void switchToGestisciUtenti(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/gestisci_utenti"));
        Stage stage = (Stage) gestutenti.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Notizie -> Rimuovi Commenti)
    public void switchToRimuoviCommenti(ActionEvent event) throws IOException{
        Scene scene = new Scene(loadFXML("/rimuovi_commenti"));
        Stage stage = (Stage) rim_commenti.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Notizie -> Imp./Esp. File)
    public void switchToImpEspFile(ActionEvent event) throws  IOException{
        Scene scene = new Scene(loadFXML("/imp_esp_file"));
        Stage stage = (Stage) imp_esp_file.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

/*-----------------------------------------------------------------------------------*/

    /*METODI GESTIONE PAGINA NOTIZIA*/
    //Funzione che rimuove le notizie secondo un id inserito
    public void rimuoviNotizia(ActionEvent event) throws IOException {
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("DELETE org.naboo.notizie.Notizia WHERE id = '" + lable_rimuovi_per_id.getText() + "'").executeUpdate();
        label_notizia_rimossa_id.setText("Notizia rimossa per ID");
        tx.commit();
        session.close();
    }

    //Funzione che rimuove le notizie secondo un range di date inserito
    public void rimuoviNotiziePerData(ActionEvent event) {
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("DELETE org.naboo.notizie.Notizia WHERE timestamp >= '" + lable_rimuovi_per_data_inizio.getText() + "' AND timestamp <= '" + lable_rimuovi_per_data_fine.getText() + "'").executeUpdate();
        label_notizia_rimossa_data.setText("Notizie rimosse per data");
        tx.commit();
        session.close();
    }

    //Funzione che rimuove le notizie secondo la squadra inserita
    public void rimuoviNotiziePerSquadra(ActionEvent event) {
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("DELETE org.naboo.notizie.Notizia WHERE squadra = '" + lable_rimuovi_per_squadra.getText() + "'").executeUpdate();
        label_notizia_rimossa_squadra.setText("Notizie rimosse per squadra");
        tx.commit();
        session.close();
    }

    //Caricamento della tabella contenente le notizie presenti sul database
    public void caricaTabellaNotizie(Session session, Transaction tx){
        Query<Notizia> query = session.createQuery("FROM org.naboo.notizie.Notizia", Notizia.class);
        List<Notizia> listaNotizia = query.list();
        int like, dislike;
        LikeDislikeController l1 = new LikeDislikeController();
        for (int i = 0; i < listaNotizia.size(); i++) {
            like = l1.contaLike(listaNotizia.get(i).getNotizie_id());
            dislike = l1.contaDislike(listaNotizia.get(i).getNotizie_id());
            oblista.add(new ModelTableNotizie(listaNotizia.get(i).getNotizie_id(), listaNotizia.get(i).getSquadra(), listaNotizia.get(i).getTitolo(), listaNotizia.get(i).getTimestamp().toString(), like, dislike));
        }
        col_id_notizia.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.valueOf(cellData.getValue().getId())));
        col_squadra.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.valueOf(cellData.getValue().getSquadra())));
        col_titolo.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTitolo()));
        col_data.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getLinkFeed()));
        col_like.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.valueOf(cellData.getValue().getLike())));
        col_dislike.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.valueOf(cellData.getValue().getDislike())));
        tabellanotizie.setItems(oblista);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();

        //Chiamata della funzione per caricare la tabella notizie
        caricaTabellaNotizie(session, tx);

        tx.commit();
        session.close();
    }
}
