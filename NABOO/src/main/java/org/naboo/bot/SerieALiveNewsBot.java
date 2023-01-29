//COLLEGAMENTO CON BOT E TUTTI RELATIVI COMANDI
package org.naboo.bot;

import org.naboo.commenti.Commento;
import org.naboo.database.HibernateUtil;
import org.naboo.likedislike.LikeDislike;
import org.naboo.notizie.Notizia;
import org.naboo.utenti.Utente;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class SerieALiveNewsBot extends TelegramLongPollingBot {
    /*----------------------------------------------------------------------------------------------------*/
    //DICHIARAZIONE VARIABILI
    //Dichiarazione variabile risposta (sarà riempita con stringhe di testo che si vorranno mandare in output su Telegram)
    SendMessage risposta = new SendMessage();
    //Dichiarazione oggetto utente
    Utente u = new Utente();
    //Dichiarazione bottono (Commento, Like, Dislike)
    InlineKeyboardButton InlineKeyboardButtonCommento = new InlineKeyboardButton();
    InlineKeyboardButton InlineKeyboardButtonLike = new InlineKeyboardButton();
    InlineKeyboardButton InlineKeyboardButtonDislike = new InlineKeyboardButton();
    //Dichiarazione flag (variabili boolean che identificano l'attivazione o meno dello stato di vari processi
    boolean flagCommento = false;
    boolean flagLike = false;
    boolean flagDislike = false;
    boolean flagFKNotiziaCommento = true;
    boolean flagFKUtenteCommento = false;
    boolean flagMenu = true;
    boolean flagRegistrazione = true;
    //Dichiarazione variabili temporanee per prendere l'id della notizia ed il commento dall'utente via Telegram
    String id_notizia = " ";
    String commento = " ";
    //CREAZIONE DIVERSI ARRAY PER OGNI SQUADRA
    List<Notizia> arrayNotizieInter = new ArrayList<>();
    List<Notizia> arrayNotizieJuventus = new ArrayList<>();
    List<Notizia> arrayNotizieMilan = new ArrayList<>();
    List<Notizia> arrayNotizieRoma = new ArrayList<>();
    List<Notizia> arrayNotizieNapoli = new ArrayList<>();
    List<Notizia> arrayNotizieAtalanta = new ArrayList<>();
    List<Notizia> arrayNotizieLazio = new ArrayList<>();
    List<Notizia> arrayNotizieUdinese = new ArrayList<>();
    List<Notizia> arrayNotizieSampdoria = new ArrayList<>();
    List<Notizia> arrayNotizieMonza = new ArrayList<>();
    List<Notizia> arrayNotizieFiorentina = new ArrayList<>();
    List<Notizia> arrayNotizieTorino = new ArrayList<>();
    List<Notizia> arrayNotizieSalernitana = new ArrayList<>();
    List<Notizia> arrayNotizieEmpoli = new ArrayList<>();
    List<Notizia> arrayNotizieLecce = new ArrayList<>();
    List<Notizia> arrayNotizieVerona = new ArrayList<>();
    List<Notizia> arrayNotizieCremonese = new ArrayList<>();
    List<Notizia> arrayNotizieSassuolo = new ArrayList<>();
    List<Notizia> arrayNotizieBologna = new ArrayList<>();
    List<Notizia> arrayNotizieGenoa = new ArrayList<>();
    List<Notizia> arrayNotizieSpezia = new ArrayList<>();
    List<Notizia> arrayNotizieCagliari = new ArrayList<>();

    //NOME BOT
    public String getBotUsername() {
        return "seriea_live_news_naboo_bot";
    }

    @Override
    // NOME TOKEN
    public String getBotToken() {
        return "5947649235:AAFAkBmv3p5_7OEq0zOh3p7o_EUBOSg5Nio";
    }

    /*----------------------------------------------------------------------------------------------------*/
    //METODI PER GESTIRE LA REGISTRAZIONE ED IL CONTROLLO DEGLI UTENTI
    //Metoto che registra l'utente passato sul DB
    public void registraUtente(Utente u) {
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            String query = "INSERT INTO utenti VALUES (" + u.getChat_id() + ", '" + u.getNickname() + "', 0) ON DUPLICATE KEY UPDATE chat_id = " + u.getChat_id();
            session.createNativeQuery(query).executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
        tx.commit();
        session.close();
    }

    //Metodo che verifica se l'utente che ha fatto l'accesso ha un ban
    public String verificaBan(int chat_id) {
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();
        int ban = 0;
        String risp = "";

        String query = "FROM org.naboo.utenti.Utente where chat_id = " + chat_id;
        Query<Utente> query2 = session.createQuery(query, Utente.class);
        List<Utente> boss = query2.list();
        //ban = session.createQuery(query).executeUpdate();
        ban = Integer.parseInt((boss.get(0).getBan()));
        tx.commit();
        session.close();

        if (ban == 0) {
            risp = "Accesso effettuato con successo " + boss.get(0).getNickname();
        } else {
            risp = "IMPOSSIBILE AVVIARE BOT: causa ban";
        }
        return risp;
    }

    /*----------------------------------------------------------------------------------------------------*/
    //METODO CHE MANDA IN OUTPUT LE NOTIZIE DI OGNI SQUADRA
    public void caricaNotizie(List<Notizia> arrayNotizieSquadra, String squadra) {
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();

        try{
            //Recepisco la data odierna - 3 giorni
            LocalDateTime dataControllo = LocalDateTime.now().minusDays(3);
            String dataPassata = String.valueOf(dataControllo);
            //Effettuo un substring per togliere i millisecondi
            dataPassata = dataPassata.substring(0, 19);
            //Torno a trasformare da String a LocalDateTime
            dataControllo = LocalDateTime.parse(dataPassata);

            Query<Notizia> query = session.createQuery("FROM org.naboo.notizie.Notizia WHERE squadra = '" + squadra + "' AND timestamp >= '" + dataControllo + "' ORDER BY timestamp", Notizia.class);
            List<Notizia> list = query.list();
            //list = ordinaLista(list);

            arrayNotizieSquadra = list;
            if(arrayNotizieSquadra.size() == 0){
                risposta.setReplyMarkup(null);
                risposta.setText("Al momento le notizie selezionate non sono disponibili, riprovare più tardi");
                execute(risposta);
            }
        }catch(Exception e){
            risposta.setText("Al momento le notizie non sono disponibili, riprovare più tardi");
        }


        for (int i = 0; i < arrayNotizieSquadra.size(); i++) {
            String messaggio = arrayNotizieSquadra.get(i).toString();
            risposta.setText(messaggio);
            InlineKeyboardMarkup inlKeyMarkValutazione = setBottoniNotizia();
            risposta.setReplyMarkup(inlKeyMarkValutazione);
            try {
                execute(risposta);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        tx.commit();
        session.close();
    }

    /*----------------------------------------------------------------------------------------------------*/
    //METODO CHE CONTROLLA SE SONO STATI INVIATI MESSAGGI O PREMUTI BOTTONI
    public void onUpdateReceived(Update update) {
        //Controllo se update contiene un messaggio
        if (update.hasMessage()) {
            Message messaggio = update.getMessage();
            if (messaggio.hasText()) {
                String comando = messaggio.getText();

                //Azioni che vengono eseguite quando si digita /start o il flag commento è true
                if (comando.equals("/start") || flagCommento == true || flagLike == true || flagDislike == true) {
                    //Azioni che vengono eseguite quando si digita /start e il flag commento è false
                    if (comando.equals("/start") && flagCommento == false) {
                        flagMenu = true;
                    }
                    //Azioni che vengono eseguite quando il flag registrazione è true
                    if (flagRegistrazione == true) {
                        //Settaggio nome utente o username e chiamata al registraUtente
                        if (messaggio.getFrom().getUserName() == null) {
                            u.setNickname(messaggio.getFrom().getFirstName());
                        } else {
                            u.setNickname(messaggio.getFrom().getUserName());
                        }
                        u.setChat_id(messaggio.getChatId().intValue());
                        registraUtente(u);
                        /*.............................................................................................*/
                        //Chiamata alla funzione che verifica se l'utente ha un ban e output su Telegram con relativo messaggio
                        int chat_id = messaggio.getChatId().intValue();
                        String verBan = verificaBan(chat_id);
                        risposta.setText(verBan);
                        risposta.setChatId(update.getMessage().getChatId().toString());
                        try {
                            execute(risposta);
                            if (verBan == "IMPOSSIBILE AVVIARE BOT: causa ban") {
                                wait(1000000000);
                            }
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        flagRegistrazione = false;
                    }
                    /*...............................................................................................*/
                    //Controllo se è stato premuto il bottono Aggiungi commento
                    if (flagCommento == true) {
                        //Controllo se è attivato il flag FKNotizia o FKUtente
                        if (flagFKNotiziaCommento == true) {
                            id_notizia = update.getMessage().getText();
                            flagFKNotiziaCommento = false;
                            flagFKUtenteCommento = true;
                        } else{
                            commento = update.getMessage().getText();
                            flagFKNotiziaCommento = true;
                            flagFKUtenteCommento = false;
                            //Controllo se sia il testo del commento che l'id associato non siano vuoti, quindi si procede con la query
                        }
                        if (commento != " " && id_notizia != " ") {
                            Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
                            Transaction tx = session.beginTransaction();

                            //Creazione oggetto commento
                            Commento comm = new Commento();
                            comm.setFK_notizia(Integer.parseInt(id_notizia));
                            comm.setFK_utente(u.getChat_id());
                            comm.setTesto(commento);
                            try {
                                //Creazione query ed aggiunta commento al DB
                                String query = "INSERT INTO commenti (FK_utente, FK_notizia, testo) VALUES (" + comm.getFK_utente() + "," + comm.getFK_notizia() + ",'" + comm.getTesto() + "')";
                                session.createNativeQuery(query).executeUpdate();
                                tx.commit();
                                //Output messaggio di conferma aggiunta commento
                                risposta.setText("Commento aggiunto con successo. Grazie della collaborazione.");
                                id_notizia = " ";
                                commento = " ";
                            }catch(Exception e){
                                risposta.setText("NON ESISTE UNA NOTIZIA CON QUESTO NUMERO");
                                tx.rollback();
                            }
                            session.close();

                            flagCommento = false;


                            try {
                                execute(risposta);
                            } catch (TelegramApiException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    //Output del messaggio di richiesta commento
                    if (flagFKUtenteCommento == true) {
                        risposta.setReplyMarkup(null);
                        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
                        Transaction tx = session.beginTransaction();
                        List<Notizia> i = new ArrayList<Notizia>();
                        try {
                            i = session.createQuery("FROM org.naboo.notizie.Notizia WHERE notizie_id = " + update.getMessage().getText(), Notizia.class).list();
                        }catch (Exception e){
                            System.out.println(e);
                        }
                        tx.commit();
                        session.close();
                        if(i.size()>0){
                            risposta.setText("Scrivi qui il tuo commento: ");

                        }else{
                            risposta.setText("NON ESISTE UNA NOTIZIA CON QUESTO NUMERO");
                            flagCommento = false;
                            flagFKUtenteCommento = false;
                            flagFKNotiziaCommento = true;
                        }
                        try {
                            execute(risposta);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    //GESTIONE LIKE
                    if (flagLike == true) {
                        id_notizia = update.getMessage().getText();
                        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
                        Transaction tx = session.beginTransaction();

                        //Creazione oggetto like
                        LikeDislike l1 = new LikeDislike();
                        l1.setLikeDislike(1);
                        l1.setFK_utente(u.getChat_id());
                        l1.setFK_notizie(Integer.parseInt(id_notizia));
                        List<Notizia> i = new ArrayList<Notizia>();
                        try {
                            //Query SQL per controllare se con il notizia_id inserito da utente esiste una notizia
                            i = session.createQuery("FROM org.naboo.notizie.Notizia WHERE notizie_id = " + update.getMessage().getText(), Notizia.class).list();
                        }catch (Exception e){
                            System.out.println(e);
                        }
                        if(i.size()>0){
                            String query = "INSERT INTO likedislike (LikeDislike, FK_utente, FK_notizia) VALUES (" + l1.getLikeDislike() + "," + l1.getFK_utente() + "," + l1.getFK_notizie() + ")";
                            session.createNativeQuery(query).executeUpdate();
                            risposta.setText("Il tuo voto è stato registrato. \t\n"
                                    + "Grazie per avere votato!");
                            tx.commit();
                            session.close();
                        }else{
                            risposta.setText("NON ESISTE UNA NOTIZIA CON QUESTO NUMERO");
                            flagDislike = false;
                        }

                        try {
                            execute(risposta);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        flagLike = false;
                    }
                    //GESTIONE DISLIKE
                    if (flagDislike == true) {
                        id_notizia = update.getMessage().getText();
                        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
                        Transaction tx = session.beginTransaction();

                        //Creazione oggetto like
                        LikeDislike l1 = new LikeDislike();
                        l1.setLikeDislike(0);
                        l1.setFK_utente(u.getChat_id());
                        l1.setFK_notizie(Integer.parseInt(id_notizia));

                        List<Notizia> i = new ArrayList<Notizia>();
                        try {
                            //Query SQL per controllare se con il notizia_id inserito da utente esiste una notizia
                            i = session.createQuery("FROM org.naboo.notizie.Notizia WHERE notizie_id = " + update.getMessage().getText(), Notizia.class).list();
                        }catch (Exception e){
                            System.out.println(e);
                        }
                        if(i.size()>0){
                            String query = "INSERT INTO likedislike (LikeDislike, FK_utente, FK_notizia) VALUES (" + l1.getLikeDislike() + "," + l1.getFK_utente() + "," + l1.getFK_notizie() + ")";
                            session.createNativeQuery(query).executeUpdate();
                            risposta.setText("Il tuo voto è stato registrato. \t\n"
                                    + "Ci dispiace che la notizia non sia di tuo gradimento, lascia un commento su come possiamo migliorare.\r\n"
                                    + "Grazie per avere votato!");
                            tx.commit();
                            session.close();
                        }else{
                            risposta.setText("NON ESISTE UNA NOTIZIA CON QUESTO NUMERO");
                            flagDislike = false;
                        }

                        try {
                            execute(risposta);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        flagDislike = false;
                    }

                    //Output del menu a scelta rapida solo quando flagMenu = true
                    if (flagMenu == true) {
                        risposta.setText("Menu scelta rapida");
                        risposta.setChatId(update.getMessage().getChatId().toString());

                        //Bottone menu Inter
                        InlineKeyboardButton InlineKeyboardButtonInter = new InlineKeyboardButton();
                        InlineKeyboardButtonInter.setText("Inter News");
                        InlineKeyboardButtonInter.setCallbackData("/inter");

                        //Bottone menu roma
                        InlineKeyboardButton InlineKeyboardButtonRoma = new InlineKeyboardButton();
                        InlineKeyboardButtonRoma.setText("Roma News");
                        InlineKeyboardButtonRoma.setCallbackData("/roma");

                        //Bottone menu atalanta
                        InlineKeyboardButton InlineKeyboardButtonAtalanta = new InlineKeyboardButton();
                        InlineKeyboardButtonAtalanta.setText("Atalanta News");
                        InlineKeyboardButtonAtalanta.setCallbackData("/atalanta");

                        //Bottone menu atalanta
                        InlineKeyboardButton InlineKeyboardButtonLazio = new InlineKeyboardButton();
                        InlineKeyboardButtonLazio.setText("Lazio News");
                        InlineKeyboardButtonLazio.setCallbackData("/lazio");

                        //Bottone menu atalanta
                        InlineKeyboardButton InlineKeyboardButtonUdinese = new InlineKeyboardButton();
                        InlineKeyboardButtonUdinese.setText("Udinese News");
                        InlineKeyboardButtonUdinese.setCallbackData("/udinese");

                        //Bottone menu atalanta
                        InlineKeyboardButton InlineKeyboardButtonSampdoria = new InlineKeyboardButton();
                        InlineKeyboardButtonSampdoria.setText("Sampdoria News");
                        InlineKeyboardButtonSampdoria.setCallbackData("/sampdoria");

                        //Bottone menu Juventus
                        InlineKeyboardButton InlineKeyboardButtonJuventus = new InlineKeyboardButton();
                        InlineKeyboardButtonJuventus.setText("Juventus News");
                        InlineKeyboardButtonJuventus.setCallbackData("/juventus");

                        //Bottone menu Milan
                        InlineKeyboardButton InlineKeyboardButtonMilan = new InlineKeyboardButton();
                        InlineKeyboardButtonMilan.setText("Milan News");
                        InlineKeyboardButtonMilan.setCallbackData("/milan");

                        //Bottone menu Napoli
                        InlineKeyboardButton InlineKeyboardButtonNapoli = new InlineKeyboardButton();
                        InlineKeyboardButtonNapoli.setText("Napoli News");
                        InlineKeyboardButtonNapoli.setCallbackData("/napoli");

                        //Bottone menu Fiorentina
                        InlineKeyboardButton InlineKeyboardButtonFiorentina = new InlineKeyboardButton();
                        InlineKeyboardButtonFiorentina.setText("Fiorentina News");
                        InlineKeyboardButtonFiorentina.setCallbackData("/fiorentina");

                        //Bottone menu Monza
                        InlineKeyboardButton InlineKeyboardButtonMonza = new InlineKeyboardButton();
                        InlineKeyboardButtonMonza.setText("Monza News");
                        InlineKeyboardButtonMonza.setCallbackData("/monza");

                        //Bottone menu Torino
                        InlineKeyboardButton InlineKeyboardButtonTorino = new InlineKeyboardButton();
                        InlineKeyboardButtonTorino.setText("Torino News");
                        InlineKeyboardButtonTorino.setCallbackData("/torino");

                        //Bottone menu Salernitana
                        InlineKeyboardButton InlineKeyboardButtonSalernitana = new InlineKeyboardButton();
                        InlineKeyboardButtonSalernitana.setText("Salernitana News");
                        InlineKeyboardButtonSalernitana.setCallbackData("salernitana");

                        //Bottone menu Empoli
                        InlineKeyboardButton InlineKeyboardButtonEmpoli = new InlineKeyboardButton();
                        InlineKeyboardButtonEmpoli.setText("Empoli News");
                        InlineKeyboardButtonEmpoli.setCallbackData("/empoli");

                        //Bottone menu Lecce
                        InlineKeyboardButton InlineKeyboardButtonLecce = new InlineKeyboardButton();
                        InlineKeyboardButtonLecce.setText("Lecce News");
                        InlineKeyboardButtonLecce.setCallbackData("/lecce");

                        //Bottone menu Verona
                        InlineKeyboardButton InlineKeyboardButtonVerona = new InlineKeyboardButton();
                        InlineKeyboardButtonVerona.setText("Verona News");
                        InlineKeyboardButtonVerona.setCallbackData("/verona");

                        //Bottone menu Cremonese
                        InlineKeyboardButton InlineKeyboardButtonCremonese = new InlineKeyboardButton();
                        InlineKeyboardButtonCremonese.setText("Cremonese News");
                        InlineKeyboardButtonCremonese.setCallbackData("/cremonese");

                        //Bottone menu Sassuolo
                        InlineKeyboardButton InlineKeyboardButtonSassuolo = new InlineKeyboardButton();
                        InlineKeyboardButtonSassuolo.setText("Sassuolo News");
                        InlineKeyboardButtonSassuolo.setCallbackData("/sassuolo");

                        //Bottone menu Bologna
                        InlineKeyboardButton InlineKeyboardButtonBologna = new InlineKeyboardButton();
                        InlineKeyboardButtonBologna.setText("Bologna News");
                        InlineKeyboardButtonBologna.setCallbackData("/bologna");

                        //Bottone menu Genoa
                        InlineKeyboardButton InlineKeyboardButtonGenoa = new InlineKeyboardButton();
                        InlineKeyboardButtonGenoa.setText("Genoa News");
                        InlineKeyboardButtonGenoa.setCallbackData("/genoa");

                        //Bottone menu Spezia
                        InlineKeyboardButton InlineKeyboardButtonSpezia = new InlineKeyboardButton();
                        InlineKeyboardButtonSpezia.setText("Spezia News");
                        InlineKeyboardButtonSpezia.setCallbackData("/spezia");

                        //Bottone menu Cagliari
                        InlineKeyboardButton InlineKeyboardButtonCagliari = new InlineKeyboardButton();
                        InlineKeyboardButtonCagliari.setText("Cagliari News");
                        InlineKeyboardButtonCagliari.setCallbackData("/cagliari");
                        //Riga
                        List<InlineKeyboardButton> row = new LinkedList<>();
                        row.add(InlineKeyboardButtonInter);
                        row.add(InlineKeyboardButtonJuventus);
                        row.add(InlineKeyboardButtonRoma);

                        //Seconda riga
                        List<InlineKeyboardButton> row2 = new LinkedList<>();
                        row2.add(InlineKeyboardButtonMilan);
                        row2.add(InlineKeyboardButtonNapoli);
                        row2.add(InlineKeyboardButtonAtalanta);

                        //Terza riga
                        List<InlineKeyboardButton> row3 = new LinkedList<>();
                        row3.add(InlineKeyboardButtonLazio);
                        row3.add(InlineKeyboardButtonUdinese);
                        row3.add(InlineKeyboardButtonSampdoria);

                        //Quarta riga
                        List<InlineKeyboardButton> row4 = new LinkedList<>();
                        row4.add(InlineKeyboardButtonMonza);
                        row4.add(InlineKeyboardButtonFiorentina);
                        row4.add(InlineKeyboardButtonTorino);

                        //Quinta riga
                        List<InlineKeyboardButton> row5 = new LinkedList<>();
                        row5.add(InlineKeyboardButtonSalernitana);
                        row5.add(InlineKeyboardButtonEmpoli);
                        row5.add(InlineKeyboardButtonLecce);

                        //Sesta riga
                        List<InlineKeyboardButton> row6 = new LinkedList<>();
                        row6.add(InlineKeyboardButtonVerona);
                        row6.add(InlineKeyboardButtonCremonese);
                        row6.add(InlineKeyboardButtonSassuolo);

                        //Settima riga
                        List<InlineKeyboardButton> row7 = new LinkedList<>();
                        row7.add(InlineKeyboardButtonBologna);
                        row7.add(InlineKeyboardButtonGenoa);
                        row7.add(InlineKeyboardButtonSpezia);

                        //Settima riga
                        List<InlineKeyboardButton> row8 = new LinkedList<>();
                        row8.add(InlineKeyboardButtonCagliari);

                        //Row collection
                        List<List<InlineKeyboardButton>> rowCollection = new LinkedList<>();
                        rowCollection.add(row);
                        rowCollection.add(row2);
                        rowCollection.add(row3);
                        rowCollection.add(row4);
                        rowCollection.add(row5);
                        rowCollection.add(row6);
                        rowCollection.add(row7);
                        rowCollection.add(row8);

                        //Keyboard
                        InlineKeyboardMarkup inlKeyMark = new InlineKeyboardMarkup();
                        inlKeyMark.setKeyboard(rowCollection);

                        //Settaggio della risposta a /start con il menu di bottoni
                        risposta.setReplyMarkup(inlKeyMark);
                        try {
                            execute(risposta);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (comando.equals("/stop")) {
                    risposta.setReplyMarkup(null);
                    risposta.setText("Bot sospeso: per riavviare digitare - /start");
                    risposta.setChatId(update.getMessage().getChatId().toString());

                    try {
                        execute(risposta);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //Controllo se update contiene una chiamata dovuta ad una premuta di un bottone
        else if (update.hasCallbackQuery()) {
            Message mex = update.getCallbackQuery().getMessage();
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String id_bottone = callbackQuery.getData();
            //SendMessage messaggioCommento = new SendMessage();
            risposta.setChatId(mex.getChatId());
            Session session;
            Transaction tx;
            Query<Notizia> query;
            List<Notizia> list;
            //SendMessage risposta = new SendMessage();
            //Switch in base all'id del bottone premuto
            switch (id_bottone) {
                //CASI IN CUI VIENE PREMUTO UN BOTTONE PER VEDERE LE NOTIZIE RELATIVE AD UNA SQUADRA
                case "/inter":
                    caricaNotizie(arrayNotizieInter, "Inter");
                    break;
                case "/juventus":
                    caricaNotizie(arrayNotizieJuventus, "Juventus");
                    break;
                case "/milan":
                    caricaNotizie(arrayNotizieMilan, "Milan");
                    break;
                case "/roma":
                    caricaNotizie(arrayNotizieRoma, "Roma");
                    break;
                case "/napoli":
                    caricaNotizie(arrayNotizieNapoli, "Napoli");
                    break;
                case "/atalanta":
                    caricaNotizie(arrayNotizieAtalanta, "Atalanta");
                    break;
                case "/lazio":
                    caricaNotizie(arrayNotizieLazio, "Lazio");
                    break;
                case "/udinese":
                    caricaNotizie(arrayNotizieUdinese, "Udinese");
                    break;
                case "/sampdoria":
                    caricaNotizie(arrayNotizieSampdoria, "Sampdoria");
                    break;
                case "/monza":
                    caricaNotizie(arrayNotizieMonza, "Monza");
                    break;
                case "/fiorentina":
                    caricaNotizie(arrayNotizieFiorentina, "Fiorentina");
                    break;
                case "/torino":
                    caricaNotizie(arrayNotizieTorino, "Torino");
                    break;
                case "/salernitana":
                    caricaNotizie(arrayNotizieSalernitana, "Salernitana");
                    break;
                case "/empoli":
                    caricaNotizie(arrayNotizieEmpoli, "Empoli");
                    break;
                case "/lecce":
                    caricaNotizie(arrayNotizieLecce, "Lecce");
                    break;
                case "/verona":
                    caricaNotizie(arrayNotizieVerona, "Verona");
                    break;
                case "/cremonese":
                    caricaNotizie(arrayNotizieCremonese, "Cremonese");
                    break;
                case "/sassuolo":
                    caricaNotizie(arrayNotizieSassuolo, "Sassuolo");
                    break;
                case "/bologna":
                    caricaNotizie(arrayNotizieBologna, "Bologna");
                    break;
                case "/genoa":
                    caricaNotizie(arrayNotizieGenoa, "Genoa");
                    break;
                case "/spezia":
                    caricaNotizie(arrayNotizieSpezia, "Spezia");
                    break;
                case "/cagliari":
                    caricaNotizie(arrayNotizieCagliari, "Cagliari");
                    break;
                //CASI IN CUI SI PREMA UN BOTTONE PER COMMENTARE O VOTARE UNA NOTIZIA
                case "Aggiungi_commento":
                    risposta.setChatId(mex.getChatId());
                    risposta.setReplyMarkup(null);
                    risposta.setText("Scrivi il numero della notizia da commentare: ");
                    try {
                        execute(risposta);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    flagCommento = true;
                    flagMenu = false;
                    break;
                case "like":
                    risposta.setChatId(mex.getChatId());
                    risposta.setReplyMarkup(null);
                    risposta.setText("Scrivi il numero della notizia a cui mettere Like: ");
                    try {
                        execute(risposta);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    flagLike = true;
                    flagMenu = false;
                    break;
                case "dislike":
                    risposta.setChatId(mex.getChatId());
                    risposta.setReplyMarkup(null);
                    risposta.setText("Scrivi il numero della notizia a cui mettere Dislike: ");
                    try {
                        execute(risposta);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    flagDislike = true;
                    flagMenu = false;
                    break;
            }
        }
    }

    public InlineKeyboardMarkup setBottoniNotizia() {

        //Bottone commento
        InlineKeyboardButtonCommento.setText("Aggiungi commento");
        InlineKeyboardButtonCommento.setCallbackData("Aggiungi_commento");

        //Bottone Like
        InlineKeyboardButtonLike.setText("Like");
        InlineKeyboardButtonLike.setCallbackData("like");

        //Bottone dislike
        InlineKeyboardButtonDislike.setText("Dislike");
        InlineKeyboardButtonDislike.setCallbackData("dislike");

        //Prima riga
        List<InlineKeyboardButton> row1 = new LinkedList<>();
        row1.add(InlineKeyboardButtonLike);
        row1.add(InlineKeyboardButtonDislike);

        //Seconda riga
        List<InlineKeyboardButton> row2 = new LinkedList<>();
        row2.add(InlineKeyboardButtonCommento);
        row2.size();

        //Row collection
        List<List<InlineKeyboardButton>> rowCollectionValutazione = new LinkedList<>();
        rowCollectionValutazione.add(row1);
        rowCollectionValutazione.add(row2);

        //Keyboard
        InlineKeyboardMarkup inlKeyMarkValutazione = new InlineKeyboardMarkup();
        inlKeyMarkValutazione.setKeyboard(rowCollectionValutazione);
        return inlKeyMarkValutazione;
    }
}

