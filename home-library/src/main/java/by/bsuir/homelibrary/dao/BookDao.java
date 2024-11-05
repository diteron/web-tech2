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
import by.bsuir.homelibrary.entity.serializer.EntitySerializer;

public class BookDao {
    private static BookDao instance = null;

    private static final String BOOKS_FILE_NAME = "books.txt";

    static {
        File file = new File(BOOKS_FILE_NAME);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create file: " + BOOKS_FILE_NAME);
        }
    }

    private BookDao() {
        
    }

    public static BookDao getInstance() {
        if (instance == null) {
            instance = new BookDao();
        }

        return instance;
    }

    public List<Book> findBooks(Book bookWithSearchFilters) {
        List<Book> foundBooks = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(BOOKS_FILE_NAME))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                Book bookFromFile = EntitySerializer.deserialize(line, Book.class);
                Book bookForComparison = createBookForComparison(bookFromFile, bookWithSearchFilters);
                if (bookFromFile.equals(bookForComparison)) {
                    foundBooks.add(bookFromFile);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        return foundBooks;
    }

    private Book createBookForComparison(Book bookFromFile, Book bookWithSearchFilters) {
        String title = bookWithSearchFilters.getTitle() != null
                ? bookWithSearchFilters.getTitle()
                : bookFromFile.getTitle();
        String author = bookWithSearchFilters.getAuthor() != null
                ? bookWithSearchFilters.getAuthor()
                : bookFromFile.getAuthor();
        Integer yearOfPublication = bookWithSearchFilters.getYearOfPublication() != null
                ? bookWithSearchFilters.getYearOfPublication()
                : bookFromFile.getYearOfPublication();
        Book.Type type = bookWithSearchFilters.getType() != null
                ? bookWithSearchFilters.getType()
                : bookFromFile.getType();
                
        return new Book(title, author, yearOfPublication, type);
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(BOOKS_FILE_NAME))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                Book book = EntitySerializer.deserialize(line, Book.class);
                books.add(book);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        return books;
    }

    public void addBook(Book book) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(BOOKS_FILE_NAME, true))) {
            String bookLine = EntitySerializer.serialize(book);
            fileWriter.append(bookLine);
            fileWriter.newLine();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
        
    public boolean isBookExists(Book book) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(BOOKS_FILE_NAME))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                Book currentBookInFile = EntitySerializer.deserialize(line, Book.class);
                if (currentBookInFile.equals(book)) {
                    return true;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return false;
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
                    Book currentBookInFile = EntitySerializer.deserialize(line, Book.class);
                    if (currentBookInFile.equals(book)) {
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
                    Book currentBookInFile = EntitySerializer.deserialize(line, Book.class);
                    if (currentBookInFile.equals(originalBook)) {
                        line = EntitySerializer.serialize(updateBook);
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
