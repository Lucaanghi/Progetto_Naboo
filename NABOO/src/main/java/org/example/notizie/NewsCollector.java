package org.example.notizie;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import org.example.database.HibernateUtil;
import org.example.feed.Feed;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.xml.sax.InputSource;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jakarta.persistence.*;

public class NewsCollector {
    private final ArrayList<Notizia> listaNotizie;
    private File fileNotizie;
    public NewsCollector() throws MalformedURLException, ParseException {
        this.listaNotizie = chiamaLeggiFeedRSS();
        this.fileNotizie = scriviNotizieSuFile();
    }

    //Metodo che chiama il leggiFeedRSS per ogni squadra (quindi per ogni FEED RSS) e forma un arraylist completo di tutte le notizie
    public ArrayList<Notizia> chiamaLeggiFeedRSS() throws MalformedURLException, ParseException {
        int i = 0;
        ArrayList<Feed> listaFeed = new ArrayList<Feed>();
        ArrayList<Notizia> listaNotizie = new ArrayList<Notizia>();
        listaFeed = prelevaFeed();
        for(int j = 0; j < listaFeed.size(); j++) {
            listaNotizie = leggiFeedRSS(listaFeed.get(j).getNomefeed(), listaNotizie, i, listaFeed.get(j).getSquadra());
            i = listaNotizie.size();
        }
        return listaNotizie;
    }

    //Metodo che crea una lista dei FEED presenti sul DB
    public ArrayList<Feed> prelevaFeed() {
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();
        Feed feed = new Feed();
        Query<Feed> query = session.createQuery("FROM org.example.feed.Feed", Feed.class);
        List<Feed> listaFeed = query.list();
        tx.commit();
        session.close();
        return (ArrayList<Feed>) listaFeed;
    }

    //Funzione per la lettura dei FEED RSS
    public static ArrayList<Notizia> leggiFeedRSS(String rssUrl, List<Notizia> listaNotizie, int i, String nomeSquadra) throws MalformedURLException {
        URL feedUrl = new URL(rssUrl);
        SyndFeedInput input = new SyndFeedInput();
        try {
            SyndFeed feed = input.build(new InputSource(feedUrl.openStream()));
            List<SyndEntry> entries = feed.getEntries();
            Iterator<SyndEntry> itEntries = entries.iterator();

            while (itEntries.hasNext()) {
                SyndEntry entry = itEntries.next();

                Notizia n = new Notizia();

                //Settaggio squadra
                n.setSquadra(nomeSquadra);

                //Estrazione titolo
                n.setTitolo(entry.getTitle());

                //Estrazione data
                n.setTimestamp(entry.getPublishedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

                //Estrazione descrizione e substring per selezionare solo la descrizione
                String stringaDescrizione = entry.getDescription().getValue();
                boolean containsStr = stringaDescrizione.indexOf("iframe") >= 0;
                if (containsStr) {
                    stringaDescrizione = "Nessuna descrizione";
                } else {
                    for (int j = 0; j < 10; j++) {
                        stringaDescrizione = stringaDescrizione.substring(0, stringaDescrizione.length() - 1);
                    }
                }
                stringaDescrizione = stringaDescrizione.replaceFirst("<br/>", ".");
                stringaDescrizione = stringaDescrizione.replaceFirst("<br/>", "");
                stringaDescrizione = stringaDescrizione.replaceFirst("<b>", "");
                stringaDescrizione = stringaDescrizione.replaceFirst("</b>", "");
                stringaDescrizione = stringaDescrizione.replaceFirst("\n", "");

                String descrizione = prendiDescrizione(stringaDescrizione);
                n.setDescrizione(descrizione);

                //Estrazione autore
                n.setAutore(entry.getAuthor());

                //Settaggio fonte
                n.setFonte("Gazzetta dello Sport");

                //Estrazione link notizia
                n.setLink(entry.getLink());

                //Aggiunta della notizia a listaNotizie
                listaNotizie.add(n);
            }
        } catch (IllegalArgumentException | FeedException | IOException e) {
            // Errore lettura feed
            e.printStackTrace();
        }
        listaNotizie.remove(listaNotizie.size()-1);
        return (ArrayList<Notizia>) listaNotizie;
    }

    //Funzione che preleva la stringa relativa alla descrizione eliminando le parti indesiderate
    private static String prendiDescrizione(String stringaDescrizione) {
        int startPos = 0;
        for (int i = 0; i < stringaDescrizione.length(); i++) {
            if (stringaDescrizione.charAt(i) == '/') {
                if (stringaDescrizione.charAt(i + 1) == '>') {
                    if (stringaDescrizione.charAt(i + 2) == '<') {
                        if (stringaDescrizione.charAt(i + 3) == 'd') {
                            if (stringaDescrizione.charAt(i + 4) == 'i') {
                                if (stringaDescrizione.charAt(i + 5) == 'v') {
                                    if (stringaDescrizione.charAt(i + 6) == '>') {
                                        startPos = i + 7;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            stringaDescrizione = stringaDescrizione.substring(startPos);
        }
        return stringaDescrizione;
    }

    //Metodo per scrivere su file tutte le notizie salvate sul'arraylist listaNotizie
    public File scriviNotizieSuFile() {
        File f1 = new File("FileNotizieRSS.txt");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(f1);

            for (Notizia n : listaNotizie) {
                fileWriter.append(String.valueOf(n.getNotizie_id()));
                fileWriter.append("*");
                fileWriter.append(n.getSquadra());
                fileWriter.append("*");
                fileWriter.append(n.getTitolo());
                fileWriter.append("*");
                fileWriter.append(n.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                fileWriter.append("*");
                fileWriter.append(n.getDescrizione());
                fileWriter.append("*");
                fileWriter.append(n.getAutore());
                fileWriter.append("*");
                fileWriter.append(n.getFonte());
                fileWriter.append("*");
                fileWriter.append(n.getLink());
                fileWriter.append("\n");
            }
            fileWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return f1;
    }

    //Metodo per trascrivere i dati da file sul DB
    public void importaFileSuDb(Session session, Transaction transaction) throws IOException, ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        BufferedReader lineReader = new BufferedReader(new FileReader("FileNotizieRSS.txt"));
        String line;

        while ((line = lineReader.readLine()) != null) {
            line = line.replaceAll("'", "");
            line = line.replaceAll("’", "");
            Notizia n1 = new Notizia();
            String[] token = line.split("\\*");

            n1.setSquadra(token[1]);
            n1.setTitolo(token[2]);
            n1.setTimestamp(LocalDateTime.parse(token[3], formatter));
            n1.setDescrizione(token[4]);
            if(token[5] == ""){
                n1.setAutore("Autore non specificato");
            }else{
                n1.setAutore(token[5]);
            }
            n1.setFonte(token[6]);
            n1.setLink(token[7]);
            try{
                String query = "INSERT INTO notizie VALUES (default,'" + n1.getSquadra() + "', '" + n1.getTitolo() + "', '" + n1.getTimestamp() + "', '" + n1.getDescrizione() + "', '" + n1.getAutore() + "', '" + n1.getFonte() + "', '" + n1.getLink() +"') ON DUPLICATE KEY UPDATE titolo = '" + n1.getTitolo() + "'";
                session.createNativeQuery(query).executeUpdate();
            }catch(Exception e){
                System.out.println(e);
            }

        }
    }

    //Metodi Get & Set
    public File getFileNotizie() {
        return fileNotizie;
    }

    public void setFileNotizie(File fileNotizie) {
        this.fileNotizie = fileNotizie;
    }

}
