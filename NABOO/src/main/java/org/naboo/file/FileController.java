package org.naboo.file;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.naboo.database.HibernateUtil;
import org.naboo.notizie.Notizia;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static org.naboo.pannelloFx.MainJFx.loadFXML;

public class FileController implements Initializable {

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
    private TextField label_importa_file;
    @FXML
    private Label label_alert_successo;


    /*METODI PER IL CAMBIO DI PANEL*/
    //Funzione che cambia schermata (Imp./Esp. File -> Aggiorna)
    public void switchToAggiorna(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/aggiorna_notizie"));
        Stage stage = (Stage) aggiorna.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Imp./Esp. File -> Agg./Rim. Feed)
    public void switchToAggRimFeed(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/agg_rim_feed"));
        Stage stage = (Stage) aggrim.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Imp./Esp. File -> Notizie)
    public void switchToRimuoviNotizie(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/rimuovi_notizie"));
        Stage stage = (Stage) rimnotizie.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Imp./Esp. File -> Area Admin)
    public void switchToAdmin(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/admin"));
        Stage stage = (Stage) admin.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Imp./Esp. File -> Gestisci Utenti)
    public void switchToGestisciUtenti(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/gestisci_utenti"));
        Stage stage = (Stage) gestutenti.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Imp./Esp. File -> Rimuovi Commenti)
    public void switchToRimuoviCommenti(ActionEvent event) throws IOException{
        Scene scene = new Scene(loadFXML("/rimuovi_commenti"));
        Stage stage = (Stage) rim_commenti.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

/*-----------------------------------------------------------------------------------*/

    //METODI GESTIONE PAGINA AGG./RIM. FEED
    //Metodo che prende in input il nome del file e lo importa sul DB
    public void importaFile() throws IOException {
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();
        String nome_file = label_importa_file.getText();
        String txt = nome_file + ".txt";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        BufferedReader lineReader = new BufferedReader(new FileReader(txt));
        String line;
        while ((line = lineReader.readLine()) != null) {
            Notizia n1 = new Notizia();
            String[] token = line.split("\\*");
            n1.setAutore(token[5]);
            n1.setSquadra(token[1]);
            n1.setTitolo(token[2]);
            System.out.println(LocalDateTime.parse(token[3], formatter));
            n1.setTimestamp(LocalDateTime.parse(token[3], formatter));
            n1.setDescrizione(token[4]);
            n1.setFonte(token[6]);
            n1.setLink(token[7]);
            session.persist(n1);
        }
        label_alert_successo.setText("Notizie del file importate con successo");
        tx.commit();
        session.close();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
