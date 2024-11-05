package by.bsuir.homelibrary.service.hashing;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordHasher {
    private static final int HASH_ITERATIONS = 8192;    // OWASP recommends to use 600,000 iterations for PBKDF2-HMAC-SHA256
    private static final int HASH_KEY_LENGTH = 128;
    private static final int SALT_SIZE = 16;

    /**
     * Generates a hash for a given password using a PBKDF2 algorithm with HMAC SHA-256.
     *
     * @param password the password to hash
     * @return the hashed password as a base64 encoded string
     * @throws RuntimeException if the hashing algorithm is not available or the key specification is invalid
     */    
    public static String getPasswordHash(String password) {
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
     * Verifies that a password matches a stored password hash.
     *
     * @param password password
     * @param storedPasswordHash    the stored password hash
     * @return {@code true} if the password is correct, otherwise {@code false}
     * @throws RuntimeException if the hashing algorithm is not available or the key specification is invalid
     */    
    public static boolean checkPasswordAgainstHash(String password, String storedPasswordHash) {
        String[] hashParts = storedPasswordHash.split(":");
        byte[] salt = Base64.getDecoder().decode(hashParts[0]);
        byte[] storedHash = Base64.getDecoder().decode(hashParts[1]);;

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, HASH_ITERATIONS, HASH_KEY_LENGTH);
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
