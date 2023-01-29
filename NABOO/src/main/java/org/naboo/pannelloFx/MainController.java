package org.naboo.pannelloFx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.naboo.admin.Admin;
import org.naboo.database.HibernateUtil;
import org.naboo.notizie.NewsCollector;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Label messaggioLogin;
    @FXML
    private Label label_notizie_aggiornate;
    @FXML
    private Label label_errore_aggiornamento;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private Button entra;
    @FXML
    private Button aggiorna;
    @FXML
    private Button aggrim;
    @FXML
    private Button rimnotizie;
    @FXML
    private Button gestutenti;
    @FXML
    private Button admin;
    @FXML
    private Button rim_commenti;
    @FXML
    private Button imp_esp_file;

/*----------------------------------------------------------------------------------------------------*/

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

    /*METODI PER IL CAMBIO DI PANEL*/
    //Funzione che cambia schermata (Login -> Aggiorna)
    public void switchToAggiornaLogin(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/aggiorna_notizie"));
        Stage stage = (Stage) entra.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Aggiorna -> Agg./Rim. Feed)
    public void switchToAggRimFeed(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/agg_rim_feed"));
        Stage stage = (Stage) aggrim.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Aggiorna -> Notizie)
    public void switchToRimuoviNotizie(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/rimuovi_notizie"));
        Stage stage = (Stage) rimnotizie.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Aggiorna -> Area Admin)
    public void switchToAdmin(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/admin"));
        Stage stage = (Stage) admin.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Aggiorna -> Gestisci Utenti)
    public void switchToGestisciUtenti(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/gestisci_utenti"));
        Stage stage = (Stage) gestutenti.getScene().getWindow();
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

    //Funzione che cambia schermata (Aggiorna -> Imp./Esp. File)
    public void switchToImpEspFile(ActionEvent event) throws  IOException{
        Scene scene = new Scene(loadFXML("/imp_esp_file"));
        Stage stage = (Stage) imp_esp_file.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

/*-----------------------------------------------------------------------------------*/

    //Metodo che carica la pagina fxml secondo il nome passato aggiungendo ".fxml"
    public Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainJFx.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

/*-----------------------------------------------------------------------------------*/
    /*METODI GESTIONE PAGINA DI LOGIN*/
    //Metodo per la gestione del bottone login
    public void loginButtonAction(ActionEvent event) {
        if (!email.getText().isBlank() && !password.getText().isBlank()) {
            //messaggioLogin.setText("Effettua il login");
            validaLogin(event);
        } else {
            messaggioLogin.setText("Inserire e-mail e password");
        }
    }

    //Metodo che convalida i dati inseriti con quelli presenti sul database
    public void validaLogin(ActionEvent event) {
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();
        Query<Admin> query = session.createQuery("FROM org.naboo.admin.Admin WHERE email = '" + email.getText() + "' AND password='" + password.getText() + "'", Admin.class);
        List<Admin> boss = query.list();
        try {
            if (boss.size() == 1) {
                messaggioLogin.setText("Login effettuato con successo");
                session.createQuery(" UPDATE org.naboo.admin.Admin SET login = true WHERE email = '" + email.getText() + "'").executeUpdate();
                switchToAggiornaLogin(event);
            } else {
                messaggioLogin.setText("Credenziali errate, reinserire");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        tx.commit();
        session.close();
    }

/*-----------------------------------------------------------------------------------*/

    /*METODI GESTIONE PAGINA AGGIORNA*/
    //Metodo che aggiorna il DB con le nuove notizie presenti sui FEED
    public void aggiornaFeed(ActionEvent event) throws ParseException, IOException {
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();

        NewsCollector n1 = new NewsCollector();
        if(n1.chiamaLeggiFeedRSS().size() == 0){
            label_errore_aggiornamento.setText("ERRORE: aggiornamento non riuscito");
        }else{
            n1.chiamaLeggiFeedRSS();
            n1.scriviNotizieSuFile();
            n1.importaFileSuDb(session, tx);
            label_notizie_aggiornate.setText("Notizie aggiornate con successo");
        }
        tx.commit();
        session.close();
    }
}

