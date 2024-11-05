package by.bsuir.homelibrary.service;

import java.util.Optional;

import by.bsuir.homelibrary.dao.AdminDao;
import by.bsuir.homelibrary.entity.User;

/**
 * The {@code AdminService} class provides services specific to administrators,
 * including adding new administrators and verifying administrator login.
 * This class follows the singleton pattern, ensuring a single instance.
 */
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

    /**
     * Adds a new administrator if the login does not already exist and the email is valid.
     *
     * @param login    the login of the new administrator
     * @param password the password of the new administrator
     * @param email    the email of the new administrator
     * @return {@code true} if the administrator was successfully added, otherwise {@code false}
     */    
    public boolean addAdmin(String login, String password, String email) {
        if (ADMIN_DAO.isAdminExists(login) || !isEmailValid(email)) {
            return false;
        }

        String passwordHash = createPasswordHash(password);
        ADMIN_DAO.addAdmin(new User(login, passwordHash, email, true));
        return true;
    }

    /**
     * Verifies administrator login by checking if the provided login and password are correct.
     *
     * @param login    the administrator's login
     * @param password the administrator's password
     * @return {@code true} if the login credentials are correct, otherwise {@code false}
     */    
    @Override
    public boolean isLogIn(String login, String password) {
        Optional<User> foundAdmin = ADMIN_DAO.findByLogin(login);
        if (!foundAdmin.isPresent()) {
            return false;
        }

        return isUserPasswordCorrect(password, foundAdmin.get().getPasswordHash());
    }
}
