package org.naboo.likedislike;

import org.naboo.commenti.Commento;
import org.naboo.database.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class LikeDislikeController {
    //Metodo che conta i like di ciascuna notizia
    public int contaLike(int id_notizia){
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();
        //capisce in automatico il tipo di oggetto che gli sto dando
            String s = "FROM org.naboo.likedislike.LikeDislike WHERE FK_notizie = " + id_notizia + " AND likeDislike = 1 ";
            List<LikeDislike> list = session.createQuery(s, LikeDislike.class).list();
            int valore = list.size();
        tx.commit();
        session.close();
        return valore;
    }
    //Metodo che conta i dislike di ciascuna notizia
    public int contaDislike(int id_notizia){
        Session session = HibernateUtil.getSessionAnnotationFactory().openSession();
        Transaction tx = session.beginTransaction();
        //capisce in automatico il tipo di oggetto che gli sto dando
        List<LikeDislike> list = session.createQuery("FROM org.naboo.likedislike.LikeDislike WHERE FK_notizie = " + id_notizia + "AND likeDislike = 0", LikeDislike.class).list();
        int valore = list.size();
        System.out.println(valore);
        tx.commit();
        session.close();
        return valore;
    }
}
