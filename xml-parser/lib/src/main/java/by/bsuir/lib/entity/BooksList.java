package by.bsuir.lib.entity;

import java.io.Serializable;
import java.util.List;

public class BooksList implements Serializable {
    private final List<Book> BOOKS;

    public BooksList(List<Book> books) {
        BOOKS = books;
    }

    public void printBooks() {
        for (var book : BOOKS) {
            System.out.println(book);
            System.out.println("------------------------------------------");
        }
    }

    public List<Book> getBooks() {
        return BOOKS;
    }
}
