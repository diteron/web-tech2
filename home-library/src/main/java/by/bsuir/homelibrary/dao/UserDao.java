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

public class UserDao {
    private static final String USERS_FILE_NAME = "users.txt";
    private static final String DELIMITER = ",";

    public Optional<User> findByLogin(String login) {
        return Optional.of(findByLoginInFile(login));
    }

    private User findByLoginInFile(String login) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(USERS_FILE_NAME))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] userFields = line.split(DELIMITER);
                if (userFields[0] == login) {
                    return createUserFromFileFields(userFields);
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private User createUserFromFileFields(String[] fields) {
        return new User(fields[0], fields[1],
                fields[2], false);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(USERS_FILE_NAME))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] userFields = line.split(DELIMITER);
                users.add(createUserFromFileFields(userFields));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        return users;
    }

    public void addUser(User user) {
        if (isUserExists(user.getLogin())) {
            System.out.println("User with login '" + user.getLogin() + "' already exists.");
        } 

        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(USERS_FILE_NAME, true))) {
            String userLine = createUserFileLine(user);
            fileWriter.append(userLine);
            fileWriter.newLine();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    private String createUserFileLine(User user) {
        return user.getLogin() + DELIMITER
                + user.getPasswordHash() + DELIMITER
                + user.getEmail();
    }

    public boolean isUserExists(String userLogin) {
        return findByLogin(userLogin).isPresent();
    }

    public void deleteUser(User user) {
        File originalFile = new File(USERS_FILE_NAME);
        File tempFile = new File("tempFile.txt");
        boolean isLineDeleted = false;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(originalFile));
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = fileReader.readLine()) != null) {
                if (!isLineDeleted) {
                    String[] userFields = line.split(DELIMITER);
                    if (userFields[0] == user.getLogin()) {
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

    private void saveChanges(File originalFile, File tempFile) {
        if (!originalFile.delete()) {
            System.out.println("Could not delete original users file");
            return;
        }

        if (!tempFile.renameTo(originalFile)) {
            System.out.println("Could not rename temp file to original users file name");
        }
    }

    public void updateUser(User user) {
        File originalFile = new File(USERS_FILE_NAME);
        File tempFile = new File("tempFile.txt");
        boolean isLineUpdated = false;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(originalFile));
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = fileReader.readLine()) != null) {
                if (!isLineUpdated) {
                    String[] userFields = line.split(DELIMITER);
                    if (userFields[0] == user.getLogin()) {
                        line = createUserFileLine(user);
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
            System.out.println("User with login '" + user.getLogin() + "' updated successfully");
        }
        else {
            System.out.println("User with login '" + user.getLogin() + "' does not exist");
        }
    }
}
