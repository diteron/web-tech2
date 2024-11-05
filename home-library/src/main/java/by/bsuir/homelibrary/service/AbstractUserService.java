package by.bsuir.homelibrary.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * The {@code AbstractUserService} class provides core functionality for user services, 
 * including email validation, password hashing, and password verification. This class 
 * serves as an abstract base for specific implementations of user services.
 */
public abstract class AbstractUserService {
    private static final int HASH_ITERATIONS = 8192;    // OWASP recommends to use 600,000 iterations for PBKDF2-HMAC-SHA256
    private static final int HASH_KEY_LENGTH = 128;
    private static final int SALT_SIZE = 16;

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

    /**
     * Generates a hash for a given password using a PBKDF2 algorithm with HMAC SHA-256.
     *
     * @param password the password to hash
     * @return the hashed password as a base64 encoded string
     * @throws RuntimeException if the hashing algorithm is not available or the key specification is invalid
     */    
    protected String getPasswordHash(String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_SIZE];
        random.nextBytes(salt);

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, HASH_ITERATIONS, HASH_KEY_LENGTH);
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();

            return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("No such hashing algorithm");
        }
        catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid key specification");
        }
    }

    /**
     * Verifies that an entered password matches a stored password hash.
     *
     * @param enteredPassword the entered password
     * @param storedPasswordHash    the stored password hash
     * @return {@code true} if the password is correct, otherwise {@code false}
     */    
    protected boolean isUserPasswordCorrect(String enteredPassword, String storedPasswordHash) {
        String[] hashParts = storedPasswordHash.split(":");
        byte[] salt = Base64.getDecoder().decode(hashParts[0]);
        byte[] storedHash = Base64.getDecoder().decode(hashParts[1]);;

        PBEKeySpec spec = new PBEKeySpec(enteredPassword.toCharArray(), salt, HASH_ITERATIONS, HASH_KEY_LENGTH);
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] testHash = factory.generateSecret(spec).getEncoded();

            return Arrays.equals(storedHash, testHash);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("No such hashing algorithm");
        }
        catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid key specification");
        }
    }
}
