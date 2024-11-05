package by.bsuir.homelibrary.service;

import java.util.ArrayList;
import java.util.List;

import by.bsuir.homelibrary.dao.BookDao;
import by.bsuir.homelibrary.entity.Book;

/**
 * The {@code BookService} class provides methods to manage books in the catalog, including
 * adding, updating, deleting, and searching for books. This class follows the singleton pattern
 * to ensure only one instance exists.
 */
public class BookService {
    private static BookService instance = null;

    private static final BookDao BOOK_DAO = BookDao.getInstance();

    private BookService() {
        
    }

    public static BookService getInstance() {
        if (instance == null) {
            instance = new BookService();
        }

        return instance;
    }

    /**
     * Adds a book to the catalog if it contains all required data and does not already exist.
     *
     * @param book the {@code Book} object to add
     */    
    public void addBook(Book book) {
        if (!book.isContainsAllData()) {
            System.out.println("Invalid book:\n"
                    + book);
            return;
        }
        else if (BOOK_DAO.isBookExists(book)) {
            System.out.println("Book already exists:\n"
                    + book);
            return;
        }

        BOOK_DAO.addBook(book);
    }

    /**
     * Adds a list of books to the catalog. Invalid books in the list are skipped.
     *
     * @param books a list of {@code Book} objects to add
     */    
    public void addBooks(List<Book> books) {
        for (var book : books) {
            if (!book.isContainsAllData()) {
                System.out.println("Invalid book in list:\n"
                        + book);
                continue;
            }
            BOOK_DAO.addBook(book);
        }
    }

    /**
     * Finds books in the catalog based on specified search filters.
     *
     * @param bookWithSearchFilters a {@code Book} object containing search filters
     * @return a list of {@code Book} objects matching the specified filters
     */    
    public List<Book> findBooksByFilters(Book bookWithSearchFilters) {
        if (!bookWithSearchFilters.isContainsAnyData()) {
            return new ArrayList<Book>();
        }

        return BOOK_DAO.findBooks(bookWithSearchFilters);
    }

    public List<Book> getAllBooks() {
        return BOOK_DAO.getAllBooks();
    }

    public void deleteBook(Book book) {
        if (!book.isContainsAllData()) {
            System.out.println("Invalid book.");
            return;
        }

        BOOK_DAO.deleteBook(book);
    }

    /**
     * Updates an existing book in the catalog with specified fields to update.
     *
     * @param originalBook          the original {@code Book} object to update
     * @param bookWithFieldsToUpdate a {@code Book} object containing fields to update
     */    
    public void updateBook(Book originalBook, Book bookWithFieldsToUpdate) {
        if (!bookWithFieldsToUpdate.isContainsAnyData()) {
            System.out.println("Invalid book.");
            return;
        }

        BOOK_DAO.updateBook(originalBook, createBookForUpdate(originalBook, bookWithFieldsToUpdate));
    }

    // Creates a new Book object with updated fields.
    private Book createBookForUpdate(Book originalBook, Book bookWithFieldsToUpdate) {
        String updateTitle = bookWithFieldsToUpdate.getTitle() != null
                ? bookWithFieldsToUpdate.getTitle()
                : originalBook.getTitle();

        String updateAuthor = bookWithFieldsToUpdate.getAuthor() != null
                ? bookWithFieldsToUpdate.getAuthor() 
                : originalBook.getAuthor();

        Integer updateYearOfPublication = bookWithFieldsToUpdate.getYearOfPublication() != null
                ? bookWithFieldsToUpdate.getYearOfPublication() 
                : originalBook.getYearOfPublication();

        Book.Type updateType = bookWithFieldsToUpdate.getType() != null
                ? bookWithFieldsToUpdate.getType()
                : originalBook.getType();

        return new Book(updateTitle, updateAuthor, updateYearOfPublication, updateType);
    }
}
