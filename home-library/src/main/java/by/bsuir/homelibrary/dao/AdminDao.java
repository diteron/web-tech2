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
import by.bsuir.homelibrary.entity.serializer.EntitySerializer;

/**
 * The {@code AdminDao} class is a singleton data access object (DAO) for managing admin users stored in a text file.
 * This class handles CRUD operations on admin users, which are stored as serialized data in the file specified by {@link #ADMINS_FILE_NAME}.
 * <p>
 * This class uses a lazy initialization approach to create its singleton instance.
 * </p>
 */
public class AdminDao {
    private static AdminDao instance = null;

    private static final String ADMINS_FILE_NAME = "admins.txt";

    /**
     * Static initializer block to ensure the admin data file is created if it does not already exist.
     * If the file creation fails, a {@link RuntimeException} is thrown.
     */
    static {
        File file = new File(ADMINS_FILE_NAME);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create file: " + ADMINS_FILE_NAME);
        }
    }
    
    private AdminDao() {
        
    }

    public static AdminDao getInstance() {
        if (instance == null) {
            instance = new AdminDao();
        }

        return instance;
    }

    /**
     * Finds an admin by login name.
     *
     * @param login the login name of the admin to find
     * @return an {@link Optional} containing the {@code User} if found, or an empty {@link Optional} if not found
     */
    public Optional<User> findByLogin(String login) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(ADMINS_FILE_NAME))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                User admin = EntitySerializer.deserialize(line, User.class);
                if (admin.getLogin().equals(login)) {
                    return Optional.of(admin);
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        
        return Optional.empty();
    }

    /**
     * Retrieves a list of all admin users.
     *
     * @return a {@link List} of all admin {@code User} objects
     */
    public List<User> getAllAdmins() {
        List<User> admins = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(ADMINS_FILE_NAME))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                User admin = EntitySerializer.deserialize(line, User.class);
                admins.add(admin);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        return admins;
    }

    /**
     * Adds a new admin to the file.
     *
     * @param admin the {@code User} to add
     */
    public void addAdmin(User admin) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(ADMINS_FILE_NAME, true))) {
            String adminLine = EntitySerializer.serialize(admin);
            fileWriter.append(adminLine);
            fileWriter.newLine();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the specified admin from the file.
     *
     * @param admin the {@code User} to delete
     */
    public void deleteAdmin(User admin) {
        File originalFile = new File(ADMINS_FILE_NAME);
        File tempFile = new File("tempFile.txt");
        boolean isLineDeleted = false;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(originalFile));
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                if (!isLineDeleted) {
                    User currentAdminInFile = EntitySerializer.deserialize(line, User.class);
                    if (currentAdminInFile.equals(admin)) {
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

    // Saves changes made to the file by renaming the temporary file to the original file name.
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

    /**
     * Updates the data of an existing admin.
     *
     * @param originalAdmin the original {@code User} data
     * @param updateAdmin   the updated {@code User} data
     */  
    public void updateAdmin(User originalAdmin, User updateAdmin) {
        File originalFile = new File(ADMINS_FILE_NAME);
        File tempFile = new File("tempFile.txt");
        boolean isLineUpdated = false;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(originalFile));
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = fileReader.readLine()) != null) {
                if (!isLineUpdated) {
                    User currentAdminInFile = EntitySerializer.deserialize(line, User.class);
                    if (currentAdminInFile.equals(originalAdmin)) {
                        line = EntitySerializer.serialize(updateAdmin);
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
            System.out.println("Admin with login '" + updateAdmin.getLogin() + "' updated successfully");
        }
        else {
            System.out.println("Admin with login '" + updateAdmin.getLogin() + "' does not exist");
        }
    }
}
