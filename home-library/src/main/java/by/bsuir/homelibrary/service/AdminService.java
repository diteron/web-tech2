package by.bsuir.homelibrary.service;

import java.util.Optional;

import by.bsuir.homelibrary.dao.AdminDao;
import by.bsuir.homelibrary.entity.User;

public class AdminService extends AbstractUserService {
    private static AdminService instance = null;

    private static final AdminDao ADMIN_DAO = AdminDao.getInstance();

    private AdminService() {
        
    }

    public static AdminService getInstance() {
        if (instance == null) {
            instance = new AdminService();
        }

        return instance;
    }

    public boolean addAdmin(String login, String password, String email) {
        if (ADMIN_DAO.isAdminExists(login) || !isEmailValid(email)) {
            return false;
        }

        String passwordHash = getPasswordHash(password);
        ADMIN_DAO.addAdmin(new User(login, passwordHash, email, true));
        return true;
    }

    @Override
    public boolean isLogIn(String login, String password) {
        Optional<User> foundAdmin = ADMIN_DAO.findByLogin(login);
        if (!foundAdmin.isPresent()) {
            return false;
        }

        return isUserPasswordCorrect(password, foundAdmin.get().getPasswordHash());
    }
}
