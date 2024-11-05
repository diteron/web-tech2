package by.bsuir.homelibrary.service;

import java.util.ArrayList;
import java.util.List;

import by.bsuir.homelibrary.dao.BookDao;
import by.bsuir.homelibrary.entity.Book;

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

    public List<Book> findBooksByFields(Book bookWithSearchFilters) {
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

    public void updateBook(Book originalBook, Book bookWithFieldsToUpdate) {
        if (!bookWithFieldsToUpdate.isContainsAnyData()) {
            System.out.println("Invalid book.");
            return;
        }

        BOOK_DAO.updateBook(originalBook, createBookForUpdate(originalBook, bookWithFieldsToUpdate));
    }

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
