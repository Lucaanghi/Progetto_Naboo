package org.naboo.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

public class AdminController implements Initializable {
    @FXML
    private Button aggiorna;
    @FXML
    private Button aggrim;
    @FXML
    private Button rimnotizie;
    @FXML
    private Button gestutenti;
    @FXML
    private Button rim_commenti;
    @FXML
    private TextField email_admin;
    @FXML
    private TextField password_admin;
    @FXML
    private Button btn_aggiorna_dati_admin;
    @FXML
    private Button btn_logout;
    @FXML
    private Button imp_esp_file;
    @FXML
    private Label lable_modifica_email;
    @FXML
    private Label lable_modifica_password;
    @FXML
    private Label lable_modifica_mail_pw;
    @FXML
    private Label lable_errore_uguali;

/*-----------------------------------------------------------------------------------*/

    /*METODI PER IL CAMBIO DI PANEL*/
    //Funzione che cambia schermata (Area Admin -> Aggiorna)
    public void switchToAggiorna(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/aggiorna_notizie"));
        Stage stage = (Stage) aggiorna.getScene().getWindow();
        stage.setScene(scene);

        stage.show();
    }

    //Funzione che cambia schermata (Area Admin -> Agg./Rim. Feed)
    public void switchToAggRimFeed(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/agg_rim_feed"));
        Stage stage = (Stage) aggrim.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Area Admin -> Notizie)
    public void switchToRimuoviNotizie(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/rimuovi_notizie"));
        Stage stage = (Stage) rimnotizie.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Area Admin -> Gestisci Utenti)
    public void switchToGestisciUtenti(ActionEvent event) throws IOException {
        Scene scene = new Scene(loadFXML("/gestisci_utenti"));
        Stage stage = (Stage) gestutenti.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Area Admin -> Rimuovi Commenti)
    public void switchToRimuoviCommenti(ActionEvent event) throws IOException{
        Scene scene = new Scene(loadFXML("/rimuovi_commenti"));
        Stage stage = (Stage) rim_commenti.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //Funzione che cambia schermata (Area Admin -> Imp./Esp. File)
    public void switchToImpEspFile(ActionEvent event) throws  IOException{
        Scene scene = new Scene(loadFXML("/imp_esp_file"));
        Stage stage = (Stage) imp_esp_file.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

/*-----------------------------------------------------------------------------------*/

    /*METODI GESTIONE PAGINA AMMINISTRATORI*/
    //Funzione che riconosce che tipo di modifica viene apportata all'amministratore ed aggiorna i relativi dati e lable di messaggio
    public void aggiornaDatiAdmin(){
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();

        String email = email_admin.getText();
        String password = password_admin.getText();
        if(email.isBlank()  && password.isBlank()){
            lable_modifica_email.setText(" ");
            lable_modifica_password.setText(" ");
            lable_modifica_mail_pw.setText(" ");
            lable_errore_uguali.setText("ERRORE: inserire nuova email o nuova password");
        }else if(email.isBlank()){
            session.createQuery("UPDATE org.naboo.admin.Admin SET password = '" + password_admin.getText() + "' WHERE login = true").executeUpdate();
            String nuova_password = setPromptTextPassword(session, tx);
            password_admin.setPromptText(nuova_password);
            lable_modifica_email.setText(" ");
            lable_modifica_mail_pw.setText(" ");
            lable_errore_uguali.setText(" ");
            lable_modifica_password.setText("Password modificata con successo");
        }else if(password.isBlank()){
            session.createQuery("UPDATE org.naboo.admin.Admin SET email = '" + email_admin.getText() + "' WHERE login = true").executeUpdate();
            String nuova_email = setPromptTextEmail(session, tx);
            email_admin.setPromptText(nuova_email);
            lable_modifica_password.setText(" ");
            lable_modifica_mail_pw.setText(" ");
            lable_errore_uguali.setText(" ");
            lable_modifica_email.setText("Email modificata con successo");
        }else {
            session.createQuery("UPDATE org.naboo.admin.Admin SET email = '" + email_admin.getText() + "' WHERE login = true").executeUpdate();
            session.createQuery("UPDATE org.naboo.admin.Admin SET password = '" + password_admin.getText() + "' WHERE login = true").executeUpdate();
            String nuova_password = setPromptTextPassword(session, tx);
            password_admin.setPromptText(nuova_password);
            String nuova_email = setPromptTextEmail(session, tx);
            email_admin.setPromptText(nuova_email);
            lable_modifica_email.setText(" ");
            lable_modifica_password.setText(" ");
            lable_errore_uguali.setText(" ");
            lable_modifica_mail_pw.setText("Email e password modificate con successo");
        }
        tx.commit();
        session.close();
    }

    //Funzione che effettua il logout dell'amministratore e riporta il relativo campo 'login' a false
    public void logoutAdmin(ActionEvent event){
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery(" UPDATE org.naboo.admin.Admin SET login = false WHERE login = true").executeUpdate();
        tx.commit();
        session.close();
        System. exit(0);
    }
    
    //Prendo dal DB il campo email con il valore login = true (ovvero l'amministratore loggato)
    public String setPromptTextEmail(Session session, Transaction tx){
        Query queryemail = session.createQuery("SELECT email FROM org.naboo.admin.Admin WHERE login = true");
        List resultemail = queryemail.list();
        String email = (String) resultemail.get(0);
        return email;
    }

    //Prendo dal DB il campo password con il valore login = true (ovvero l'amministratore loggato)
    public String setPromptTextPassword(Session session, Transaction tx){
        Query querypassword = session.createQuery("SELECT password FROM org.naboo.admin.Admin WHERE login = true");
        List resultpassword = querypassword.list();
        String password = (String) resultpassword.get(0);
        return password;
    }

/*-----------------------------------------------------------------------------------*/
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();

        String email = setPromptTextEmail(session, tx);
        String password = setPromptTextPassword(session, tx);

        //Setto il PromptText delle lable email e password con i rispettivi valori dell'amministratore loggato
        email_admin.setPromptText(email);
        password_admin.setPromptText(password);

        tx.commit();
        session.close();
    }
}