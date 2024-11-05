package by.bsuir.homelibrary.service;

import java.util.regex.Pattern;

import by.bsuir.homelibrary.service.hashing.PasswordHasher;

/**
 * The {@code AbstractUserService} class provides core functionality for user services, 
 * including email validation, password hashing, and password verification. This class 
 * serves as an abstract base for specific implementations of user services.
 */
public abstract class AbstractUserService {
    /**
     * Checks if a user login is valid by verifying the provided login and password.
     *
     * @param login    the user's login
     * @param password the user's password
     * @return {@code true} if the login is valid, otherwise {@code false}
     */
    public abstract boolean isLogIn(String login, String password);
   
    protected boolean isEmailValid(String email) {
        String emaiRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.compile(emaiRegex)
                .matcher(email)
                .matches();
    }
  
    protected String createPasswordHash(String password) {
        return PasswordHasher.getPasswordHash(password);
    }
  
    protected boolean isUserPasswordCorrect(String enteredPassword, String storedPasswordHash) {
        return PasswordHasher.checkPasswordAgainstHash(enteredPassword, storedPasswordHash);
    }
}
