package by.bsuir.hotelwebapp.config;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import by.bsuir.hotelwebapp.entity.Booking;
import by.bsuir.hotelwebapp.entity.RoomType;
import by.bsuir.hotelwebapp.entity.User;

public class HibernateSession {
    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Booking.class)
                    .addAnnotatedClass(RoomType.class)
                    .buildSessionFactory();
        }
        catch (HibernateException e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("Failed to create Hibernate session factory");
        }
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }

    public static void shutdown() {
        sessionFactory.close();
    }
}
