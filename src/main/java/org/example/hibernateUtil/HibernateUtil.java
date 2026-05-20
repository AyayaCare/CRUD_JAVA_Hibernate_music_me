package org.example.hibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Slf4j
public class HibernateUtil {


    //Verifica que toda la configuracion sea correcta, como el dialecto, usuario sql, etc
    private static final SessionFactory sessionFactory = buildMySessionFactory();

    private static  SessionFactory buildMySessionFactory() {
        try {
            //Configuration tiene su propio buildSessionFactory, y es el que usa al final
            //Es distinto al mío, al que estoy definiendo aquí arriba como metodo
            SessionFactory sf = new Configuration()
                    //busca hibernate.cfg.xml
                    .configure()
                    .buildSessionFactory();
            log.info("DONE! The database has been connected");
            return sf;
        } catch (Throwable ex) {
            log.error("Something went wrong with the connection!");
            throw new ExceptionInInitializerError(ex);

        }


        }
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
        }


}
