package by.bsuir.hotelwebapp.dao;

import java.util.Optional;

import org.hibernate.Session;

import by.bsuir.hotelwebapp.config.HibernateSession;
import by.bsuir.hotelwebapp.entity.User;

public class UserDAO{
    private final Session session;
    private static UserDAO instance = null;

    private UserDAO() {
        session = HibernateSession.getSession();
    }

    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }

        return instance;
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(session.get(User.class, id));
    }

    public Optional<User> findByUsername(String username) {
        return null;
    }

    public void saveUser(User user) {

    }

    public void deleteUser(User user) {

    }
}
