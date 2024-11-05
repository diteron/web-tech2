package by.bsuir.homelibrary.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public abstract class AbstractUserService {
    private static final int HASH_ITERATIONS = 128;
    private static final int HASH_KEY_LENGTH = 128;

    public abstract boolean isLogIn(String login, String password);

    protected boolean isEmailValid(String email) {
        String emaiRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.compile(emaiRegex)
                .matcher(email)
                .matches();
    }

    protected String getPasswordHash(String password) {
        byte[] salt = new byte[16];
        salt = "ggg".getBytes();
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, HASH_ITERATIONS, HASH_KEY_LENGTH);
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("No such hashing algorithm");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid key specification");
        }
    }

    protected boolean isUserPasswordCorrect(String enteredPassword, String passwordHash) {
        String enteredPasswordHash = getPasswordHash(enteredPassword);
        return enteredPasswordHash.equals(passwordHash);
    }
}
