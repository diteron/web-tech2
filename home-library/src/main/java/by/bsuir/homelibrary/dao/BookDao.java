package by.bsuir.homelibrary.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import by.bsuir.homelibrary.entity.Book;

public class BookDao {
    private static final String BOOKS_FILE_NAME = "books.txt";
    private static final String DELIMITER = ",";

    /*public List<Book> findByTitle(String title) {
        List<Book> foundBooks = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(BOOKS_FILE_NAME))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] bookFields = line.split(DELIMITER);
                if (bookFields[0] == title) {
                    foundBooks.add(new Book.Builder()
                            .title(bookFields[0])
                            .author(bookFields[1])
                            .yearOfPublication(Integer.parseInt(bookFields[2]))
                            .type(Book.Type.valueOf(bookFields[3]))
                            .build());
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return foundBooks;
    }
    */

    public List<Book> findBooks(Book book) {
        List<Book> foundBooks = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(BOOKS_FILE_NAME))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] bookFields = line.split(DELIMITER);
                if (bookFields[0] == book.getTitle()
                        || bookFields[1] == book.getAuthor()
                        || bookFields[2] == book.getYearOfPublication().toString()
                        || bookFields[3] == book.getType().toString()) {

                    foundBooks.add(createBookFromFileFields(bookFields));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        return foundBooks;
    }

    private Book createBookFromFileFields(String[] fields) {
        return new Book.Builder()
                .title(fields[0])
                .author(fields[1])
                .yearOfPublication(Integer.parseInt(fields[2]))
                .type(Book.Type.valueOf(fields[3]))
                .build();
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(BOOKS_FILE_NAME))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] bookFields = line.split(DELIMITER);
                books.add(createBookFromFileFields(bookFields));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        return books;
    }

    /*public List<Book> findByYearOfPublication(Integer yearOfPublication) {
        List<Book> foundBooks = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(BOOKS_FILE_NAME))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] bookFields = line.split(DELIMITER);
                if (bookFields[2] == Integer.toString(yearOfPublication)) {
                    foundBooks.add(new Book.Builder()
                            .title(bookFields[0])
                            .author(bookFields[1])
                            .yearOfPublication(Integer.parseInt(bookFields[2]))
                            .type(Book.Type.valueOf(bookFields[3]))
                            .build());
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return foundBooks;
    }

    public List<Book> findByType(Book.Type type) {
        List<Book> foundBooks = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(BOOKS_FILE_NAME))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] bookFields = line.split(DELIMITER);
                if (bookFields[3] == type.toString()) {
                    foundBooks.add(new Book.Builder()
                            .title(bookFields[0])
                            .author(bookFields[1])
                            .yearOfPublication(Integer.parseInt(bookFields[2]))
                            .type(Book.Type.valueOf(bookFields[3]))
                            .build());
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return foundBooks;
    }
    */

    public void addBook(Book book) {
        if (isBookExists(book)) {
            System.out.println("Book already exists.");
        } 

        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(BOOKS_FILE_NAME, true))) {
            String bookLine = bookToFileLine(book);
            fileWriter.append(bookLine);
            fileWriter.newLine();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
        
    private boolean isBookExists(Book book) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(BOOKS_FILE_NAME))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] bookFields = line.split(DELIMITER);
                if (isFileLineFieldsEqualBook(bookFields, book)) {
                    return true;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private String bookToFileLine(Book book) {
        return book.getTitle() + DELIMITER
                + book.getAuthor() + DELIMITER
                + book.getYearOfPublication() + DELIMITER
                + book.getType();
    }

    private boolean isFileLineFieldsEqualBook(String[] lineFields, Book book) {
        return lineFields[0] == book.getTitle()
                && lineFields[1] == book.getAuthor()
                && lineFields[2] == book.getYearOfPublication().toString()
                && lineFields[3] == book.getType().toString();
    }

    public void deleteBook(Book book) {
        File originalFile = new File(BOOKS_FILE_NAME);
        File tempFile = new File("tempFile.txt");
        boolean isLineDeleted = false;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(originalFile));
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = fileReader.readLine()) != null) {
                if (!isLineDeleted) {
                    String[] bookFields = line.split(DELIMITER);
                    if (isFileLineFieldsEqualBook(bookFields, book)) {
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
            System.out.println("Book deleted successfully");
        }
        else {
            System.out.println("Book does not exist");
        }
    }

    private void saveChanges(File originalFile, File tempFile) {
        if (!originalFile.delete()) {
            System.out.println("Could not delete original file");
            return;
        }

        if (!tempFile.renameTo(originalFile)) {
            System.out.println("Could not rename temp file to original file name");
        }
    }

    public void updateBook(Book originalBook, Book updateBook) {
        File originalFile = new File(BOOKS_FILE_NAME);
        File tempFile = new File("tempFile.txt");
        boolean isLineUpdated = false;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(originalFile));
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = fileReader.readLine()) != null) {
                if (!isLineUpdated) {
                    String[] bookFields = line.split(DELIMITER);
                    if (isFileLineFieldsEqualBook(bookFields, originalBook)) {
                        line = bookToFileLine(updateBook);
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
            System.out.println("Book updated successfully");
        }
        else {
            System.out.println("Book does not exist");
        }
    }

}
