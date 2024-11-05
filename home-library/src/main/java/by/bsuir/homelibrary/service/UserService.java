package by.bsuir.homelibrary.service;

import java.util.Optional;

import by.bsuir.homelibrary.dao.UserDao;
import by.bsuir.homelibrary.entity.User;

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

    public boolean addUser(String login, String password, String email) {
        if (USER_DAO.isUserExists(login) || !isEmailValid(email)) {
            return false;
        }

        String passwordHash = getPasswordHash(password);
        USER_DAO.addUser(new User(login, passwordHash, email, false));
        return true;
    }

    @Override
    public boolean isLogIn(String login, String password) {
        Optional<User> foundUser = USER_DAO.findByLogin(login);
        if (!foundUser.isPresent()) {
            return false;
        }

        return isUserPasswordCorrect(password, foundUser.get().getPasswordHash());
    }
}
