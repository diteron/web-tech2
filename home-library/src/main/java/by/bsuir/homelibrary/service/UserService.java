package by.bsuir.homelibrary.service;

import java.util.Optional;

import by.bsuir.homelibrary.dao.UserDao;
import by.bsuir.homelibrary.entity.User;

/**
 * The {@code UserService} class provides services specific to regular users,
 * including adding new users and verifying user login credentials.
 * This class follows the singleton pattern, ensuring a single instance.
 */
public class UserService extends AbstractUserService {
    private static UserService instance = null;

    private static final UserDao USER_DAO = UserDao.getInstance();

    private UserService() {
        
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }

        return instance;
    }

    /**
     * Adds a new user if the login does not already exist and the email is valid.
     *
     * @param login    the login of the new user
     * @param password the password of the new user
     * @param email    the email of the new user
     * @return {@code true} if the user was successfully added, otherwise {@code false}
     */    
    public boolean addUser(String login, String password, String email) {
        if (USER_DAO.isUserExists(login) || !isEmailValid(email)) {
            return false;
        }

        String passwordHash = getPasswordHash(password);
        USER_DAO.addUser(new User(login, passwordHash, email, false));
        return true;
    }

    /**
     * Verifies user login by checking if the provided login and password are correct.
     *
     * @param login    the user's login
     * @param password the user's password
     * @return {@code true} if the login credentials are correct, otherwise {@code false}
     */    
    @Override
    public boolean isLogIn(String login, String password) {
        Optional<User> foundUser = USER_DAO.findByLogin(login);
        if (!foundUser.isPresent()) {
            return false;
        }

        return isUserPasswordCorrect(password, foundUser.get().getPasswordHash());
    }
}
