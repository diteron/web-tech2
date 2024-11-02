package by.bsuir.homelibrary.service;

import java.util.List;

import by.bsuir.homelibrary.dao.BookDao;
import by.bsuir.homelibrary.entity.Book;

public class BookService {
    private static final BookDao BOOK_DAO = new BookDao();

    public void addBook(String title, String author, Integer yearOfPublication, Book.Type type) {
        BOOK_DAO.addBook(new Book(title, author, yearOfPublication, type));
    }

    public List<Book> findBooksByTitle(String title) {
        return BOOK_DAO.findBooks(new Book.Builder().title(title).build());
    }

    public List<Book> findBooksByAuthor(String author) {
        return BOOK_DAO.findBooks(new Book.Builder().author(author).build());
    }

    public List<Book> findBooksByYearOfPublication(Integer yearOfPublication) {
        return BOOK_DAO.findBooks(new Book.Builder().yearOfPublication(yearOfPublication).build());
    }

    public List<Book> findBooksByType(Book.Type type) {
        return BOOK_DAO.findBooks(new Book.Builder().type(type).build());
    }

    public List<Book> getAllBooks() {
        return BOOK_DAO.getAllBooks();
    }

    public void deleteBook(Book book) {
        BOOK_DAO.deleteBook(book);
    }

    public void updateBook(Book originalBook,
            String newTitle, String newAuthor, Integer newYearOfPublication, Book.Type newType) {

        BOOK_DAO.updateBook(originalBook,
                createBookForUpdate(originalBook, newTitle, newAuthor, newYearOfPublication, newType));
    }

    private Book createBookForUpdate(Book originalBook, 
            String title, String author, Integer yearOfPublication, Book.Type type) {

        String updatedTitle = title != null ? title : originalBook.getTitle();
        String updatedAuthor = author != null ? author : originalBook.getAuthor();
        Integer updatedYearOfPublication = yearOfPublication != null ? yearOfPublication : originalBook.getYearOfPublication();
        Book.Type updatedType = type != null ? type : originalBook.getType();

        return new Book(updatedTitle, updatedAuthor, updatedYearOfPublication, updatedType);
    }
}
