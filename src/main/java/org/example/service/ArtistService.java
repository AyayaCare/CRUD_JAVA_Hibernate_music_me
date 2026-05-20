package org.example.service;
import org.example.entity.ArtistEntity;
import org.example.entity.SongsEntity;
import org.example.hibernateUtil.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ArtistService {

    public static List<ArtistEntity> searchArtist(String artistName) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            //Los ':' indican que allí se inyectará una variable
            //%: En SQL/HQL, el porcentaje es un comodín (wildcard).
            //Significa "cualquier cosa puede ir aquí".
            String hql = "FROM ArtistEntity a WHERE LOWER(a.artist_name) LIKE :search";
            Query<ArtistEntity> query = session.createQuery(hql, ArtistEntity.class);
            query.setParameter("search", "%" + artistName + "%");
            List<ArtistEntity> resultList = query.getResultList();
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }


        //Ejemplo real donde en db hay:
        //
        //Beach House
        //
        //The Beach Boys
        //
        //Florence + The Machine
        //
        //Si el usuario busca "Beach":
        //
        //Con "%Beach%": Hibernate encontrará los registros 1 y 2.
        //
        //Sin los %: Hibernate solo buscaría el nombre exacto "Beach" (y no encontraría nada).
    }

    public static void addArtist(ArtistEntity artist) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(artist);
        tx.commit();
        session.close();
    }

    public static void deleteData() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Scanner scan = new Scanner(System.in);
        System.out.println("We'll guide you!: ");
        System.out.println("Write the name of the artist you want to delete!: ");
        String artistName = scan.nextLine();
        if(!artistName.trim().isEmpty()) {

            List<ArtistEntity> artistList = searchArtist(artistName);
            for (ArtistEntity l : artistList) {
                System.out.println(l);
            }
        } else {
            System.out.println("Artist have not been found");
        }
        String selection;
        System.out.println("Write the ID of the artist you want to delete: ");
        selection = scan.nextLine();
        if (!selection.trim().isEmpty()) {

            String hql = "DELETE FROM SongsEntity sg WHERE sg.artist.id = :id";
            Query<SongsEntity> query = session.createQuery(hql);
            query.setParameter("id", Long.valueOf(selection));
            query.executeUpdate();
            String hql2 = "DELETE FROM ArtistEntity ar WHERE ar.id = :id";
            Query<ArtistEntity> queryArtist = session.createQuery(hql2);
            queryArtist.setParameter("id", Long.valueOf(selection));
            int deleted = queryArtist.executeUpdate();
            tx.commit();
            if (deleted > 0) {
                System.out.println("Artist and all their songs deleted successfully!");
            } else {
                System.out.println("Artist not found.");
            }
        }

        session.close();
    }
}
