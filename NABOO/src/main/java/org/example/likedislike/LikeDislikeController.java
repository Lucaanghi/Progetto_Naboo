package org.example.likedislike;

import org.example.commenti.Commento;
import org.example.database.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class LikeDislikeController {
    public void caricaCommentoDB(Commento c){
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();
        //session.createQuery("INSERT INTO org.example.likedislike (LikeDislike, FK_utente, FK_notizia) VALUES(1,2,'"+ c.getTesto() + "'").executeUpdate();
        tx.commit();
        session.close();
    }

    public int contaLike(int id_notizia){
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();
        //capisce in automatico il tipo di oggetto che gli sto dando
            String s = "FROM org.example.likedislike.LikeDislike WHERE FK_notizie = " + id_notizia + " AND likeDislike = 1 ";
            List<LikeDislike> list = session.createQuery(s, LikeDislike.class).list();
            int valore = list.size();
        tx.commit();
        session.close();
        return valore;
    }
    public int contaDislike(int id_notizia){
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();
        //capisce in automatico il tipo di oggetto che gli sto dando
        List<LikeDislike> list = session.createQuery("FROM org.example.likedislike.LikeDislike WHERE FK_notizie = " + id_notizia + "AND likeDislike = 0", LikeDislike.class).list();
        int valore = list.size();
        System.out.println(valore);
        tx.commit();
        session.close();
        return valore;
    }
}
