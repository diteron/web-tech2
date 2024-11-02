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

public class AdminDao {
    private static final String ADMINS_FILE_NAME = "admins.txt";
    private static final String DELIMITER = ",";

    public Optional<User> findByLogin(String login) {
        
        return Optional.of(findByLoginInFile(login));
    }

    private User findByLoginInFile(String login) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(ADMINS_FILE_NAME))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] adminFields = line.split(DELIMITER);
                if (adminFields[0] == login) {
                    return createAdminFromFileFields(adminFields);
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private User createAdminFromFileFields(String[] fields) {
        return new User(fields[0], fields[1],
                fields[2], true);
    }

    public List<User> getAllAdmins() {
        List<User> users = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(ADMINS_FILE_NAME))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] adminFields = line.split(DELIMITER);
                users.add(createAdminFromFileFields(adminFields));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        return users;
    }

    public void addAdmin(User admin) {
        if (isAdminExists(admin.getLogin())) {
            System.out.println("Admin with login '" + admin.getLogin() + "' already exists.");
        } 

        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(ADMINS_FILE_NAME, true))) {
            String adminLine = createAdminFileLine(admin);
            fileWriter.append(adminLine);
            fileWriter.newLine();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    private String createAdminFileLine(User admin) {
        return admin.getLogin() + DELIMITER
                + admin.getPasswordHash() + DELIMITER
                + admin.getEmail();
    }

    public void deleteAdmin(User admin) {
        File originalFile = new File(ADMINS_FILE_NAME);
        File tempFile = new File("tempFile.txt");
        boolean isLineDeleted = false;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(originalFile));
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = fileReader.readLine()) != null) {
                if (!isLineDeleted) {
                    String[] adminFields = line.split(DELIMITER);
                    if (adminFields[0] == admin.getLogin()) {
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
            System.out.println("Admin with login '" + admin.getLogin() + "' deleted successfully");
        }
        else {
            System.out.println("Admin with login '" + admin.getLogin() + "' does not exist");
        }
    }

    private void saveChanges(File originalFile, File tempFile) {
        if (!originalFile.delete()) {
            System.out.println("Could not delete original admins file");
            return;
        }

        if (!tempFile.renameTo(originalFile)) {
            System.out.println("Could not rename temp file to original admins file name");
        }
    }

    public boolean isAdminExists(String adminLogin) {
        return findByLogin(adminLogin).isPresent();
    }

    public void updateAdmin(User admin) {
        File originalFile = new File(ADMINS_FILE_NAME);
        File tempFile = new File("tempFile.txt");
        boolean isLineUpdated = false;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(originalFile));
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = fileReader.readLine()) != null) {
                if (!isLineUpdated) {
                    String[] adminFields = line.split(DELIMITER);
                    if (adminFields[0] == admin.getLogin()) {
                        line = createAdminFileLine(admin);;
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
            System.out.println("Admin with login '" + admin.getLogin() + "' updated successfully");
        }
        else {
            System.out.println("Admin with login '" + admin.getLogin() + "' does not exist");
        }
    }
}
