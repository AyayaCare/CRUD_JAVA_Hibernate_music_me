package org.example.service;

import org.example.entity.ArtistEntity;
import org.example.entity.SongsEntity;
import org.example.hibernateUtil.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

//get / find
//
//save / persist
//
//update / merge
//
//delete / remove

public class SongService {

    public static void save(SongsEntity song) {
        //abre sesion con la db
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        //seguimiento
        //Añade el objeto al contexto actual, lo que significa que Hibernate
        // monitoriza cualquier cambio realizado
        // en el objeto posteriormente y lo sincronizará automáticamente
        // al confirmar la transacción.
        session.persist(song);

        tx.commit();
        session.close();

    }

    public static SongsEntity findByID(Long id) {

        //1.argument: qué tabla? @Entity
        //@Table(name = "songs")
        //public class SongsEntity { ... }
        //2.argument: qué fila?
        //@Id
        //private Long id;

        //Y se generan las consultas SQL
        Session session = HibernateUtil.getSessionFactory().openSession();
        SongsEntity finded = session.get(SongsEntity.class, id);
        session.close();
        return finded;
    }


    public static List<SongsEntity> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<SongsEntity> listOfSongs =

                //Aquí se crea la query, pero para ejecutarla requiere:
                //getResultList()
                //
                //getSingleResult()
                //
                //uniqueResult()

                //Cuando quieres la entidad entera, no pones select, ya hibernate lo hace
                session.createQuery("from SongsEntity", SongsEntity.class)
                        .getResultList();
        session.close();
        return listOfSongs;
    }

    public static void updateData(Long id) {
        boolean check = true;
        Scanner scan = new Scanner(System.in);
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        SongsEntity song = session.get(SongsEntity.class, id);
        System.out.println("Write the song name: ");
        String songName = scan.nextLine();

        if (song != null) {
            song.setSong_name(songName.toLowerCase(Locale.ROOT));
            while (check) {
                AtomicInteger number = new AtomicInteger(1);
                System.out.println("Look for the artist by the name: ");
                String name = scan.nextLine();
                List<ArtistEntity> listOfArtist = ArtistService.searchArtist(name);
                listOfArtist.stream().map((ArtistEntity::toString))
                        .forEach((result) ->
                                System.out.println(number.getAndIncrement() + "." + result));


                System.out.println("\n");
                System.out.println("Is there the artist you searched for? yes/no ");
                String confirmation = scan.nextLine();
                if ("yes".equalsIgnoreCase(confirmation)) {
                    System.out.println("Write the index of the artist: ");
                    int indexSelected = scan.nextInt() - 1;
                    scan.nextLine();
                    ArtistEntity artist = listOfArtist.get(indexSelected);
                    song.setArtist(artist);
                    check = false;
                    System.out.println("Write the duration: ");
                    if(scan.hasNextInt()) {
                       int duration= scan.nextInt();
                        scan.nextLine();
                       song.setDuration(Long.valueOf(duration));
                    } else {
                        System.out.println("Invalid");
                        return;
                    }
                    System.out.println("Write the year: ");
                    if (scan.hasNextInt()) {
                        int year = scan.nextInt();
                        scan.nextLine();
                        song.setYear(year);
                    } else {
                        System.out.println("Invalid");
                        return;
                    }
                    tx.commit();
                    session.close();
                    System.out.println("Done! Data updated");

                } else if ("no".equalsIgnoreCase(confirmation)) {
                    System.out.println("This artist doesn't exist on database. If you like, you" +
                            "can try to create new data " + "\n");
                    return;

                } else {
                    System.out.println("Please, only 'yes' or 'no'");

                }


            }


        }
    }

    public static void findSongByName (String name) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String hql = "SELECT sg FROM SongsEntity sg WHERE LOWER(sg.song_name) LIKE :name";
        Query<SongsEntity> query = session.createQuery(hql, SongsEntity.class);
        query.setParameter("name", "%" + name.toLowerCase(Locale.ROOT) + "%");
        List<SongsEntity> Songlist = query.getResultList();
        for (SongsEntity s : Songlist) {
            System.out.println(s);
            System.out.println();
        }
        session.close();
    }



}

