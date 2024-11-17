package by.bsuir.xmlparser.common.entity;

import java.io.Serializable;
import java.util.List;

public class BooksList implements Serializable {
    private final List<Book> books;

    public BooksList(List<Book> books) {
        this.books = books;
    }

    public void printBooks() {
        for (var book : books) {
            System.out.println(book);
            System.out.println("------------------------------------------");
        }
    }

    public List<Book> getBooks() {
        return books;
    }
}
