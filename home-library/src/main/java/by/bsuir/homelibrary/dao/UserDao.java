package by.bsuir.homelibrary.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import by.bsuir.homelibrary.entity.User;
import by.bsuir.homelibrary.entity.serializer.EntitySerializer;;

/**
 * The {@code UserDao} class manages user-related data operations, including retrieving,
 * adding, updating, and deleting user information stored in a file. This class follows
 * a singleton pattern to ensure a single instance for file-based user management.
 */
public class UserDao {
    private static UserDao instance = null;

    private static final String USERS_FILE_NAME = "users.txt";

    /**
     * Static initializer block to ensure the user data file is created if it does not already exist.
     * If the file creation fails, a {@link RuntimeException} is thrown.
     */
    static {
        File file = new File(USERS_FILE_NAME);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create file: " + USERS_FILE_NAME);
        }
    }

    private UserDao() {
        
    }

    public static UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }

        return instance;
    }

    /**
     * Finds a user by their login.
     *
     * @param login the login of the user to find
     * @return an {@code Optional} containing the user if found, otherwise empty
     */
    public Optional<User> findByLogin(String login) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(USERS_FILE_NAME))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                User user = EntitySerializer.deserialize(line, User.class);
                if (user.getLogin().equals(login)) {
                    return Optional.of(user);
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * Retrieves a list of all users.
     *
     * @return a list of all {@code User} objects
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(USERS_FILE_NAME))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                User user = EntitySerializer.deserialize(line, User.class);
                users.add(user);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        return users;
    }

    /**
     * Adds a user to the file.
     *
     * @param user the {@code User} to add
     */    
    public void addUser(User user) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(USERS_FILE_NAME, true))) {
            String userLine = EntitySerializer.serialize(user);
            fileWriter.append(userLine);
            fileWriter.newLine();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if a user exists by login.
     *
     * @param userLogin the login of the user to check
     * @return {@code true} if the user exists, otherwise {@code false}
     */    
    public boolean isUserExists(String userLogin) {
        return findByLogin(userLogin).isPresent();
    }

    /**
     * Deletes a specified user from the file.
     *
     * @param user the {@code User} to delete
     */    
    public void deleteUser(User user) {
        File originalFile = new File(USERS_FILE_NAME);
        File tempFile = new File("tempFile.txt");
        boolean isLineDeleted = false;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(originalFile));
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = fileReader.readLine()) != null) {
                if (!isLineDeleted) {
                    User currentUserInFile = EntitySerializer.deserialize(line, User.class);
                    if (currentUserInFile.equals(user)) {
                        isLineDeleted = true;
                        continue;
                    }
                }

                fileWriter.append(line);
                fileWriter.newLine(); 
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        saveChanges(originalFile, tempFile);

        if (isLineDeleted) {
            System.out.println("User with login '" + user.getLogin() + "' deleted successfully");
        }
        else {
            System.out.println("User with login '" + user.getLogin() + "' does not exist");
        }
    }

    // Saves changes made to the file by renaming the temporary file to the original file name.
    private void saveChanges(File originalFile, File tempFile) {
        if (!originalFile.delete()) {
            System.out.println("Could not delete original users file");
            return;
        }

        if (!tempFile.renameTo(originalFile)) {
            System.out.println("Could not rename temp file to original users file name");
        }
    }

    /**
     * Updates the data of an existing user.
     *
     * @param originalUser the original {@code User} data
     * @param updateUser   the updated {@code User} data
     */    
    public void updateUser(User originalUser, User updateUser) {
        File originalFile = new File(USERS_FILE_NAME);
        File tempFile = new File("tempFile.txt");
        boolean isLineUpdated = false;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(originalFile));
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = fileReader.readLine()) != null) {
                if (!isLineUpdated) {
                    User currentUserInFile = EntitySerializer.deserialize(line, User.class);
                    if (currentUserInFile.equals(originalUser)) {
                        line = EntitySerializer.serialize(updateUser);
                        isLineUpdated = true;
                    }
                }

                fileWriter.append(line);
                fileWriter.newLine();
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        saveChanges(originalFile, tempFile);

        if (isLineUpdated) {
            System.out.println("User with login '" + updateUser.getLogin() + "' updated successfully");
        }
        else {
            System.out.println("User with login '" + updateUser.getLogin() + "' does not exist");
        }
    }
}
