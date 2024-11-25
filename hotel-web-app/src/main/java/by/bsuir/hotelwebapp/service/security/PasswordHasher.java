package by.bsuir.hotelwebapp.service.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordHasher {
    private static final int HASH_ITERATIONS = 600_000;    // OWASP recommends to use 600,000 iterations for PBKDF2-HMAC-SHA256
    private static final int HASH_KEY_LENGTH = 256;
    private static final int SALT_SIZE = 64;
   
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
            throw new RuntimeException("No such hashing algorithm", e);
        }
        catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid key specification", e);
        }
    }
   
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
            throw new RuntimeException("No such hashing algorithm", e);
        }
        catch (InvalidKeySpecException e) {
            throw new RuntimeException("Invalid key specification", e);
        }
    }
}
