package by.bsuir.xmlclient.entity;

import java.io.Serializable;

public class Book implements Serializable {
    private String title;
    private Integer year;
    private String genre;
    private Author author;
    private Publisher publisher;

    public Book(String title, Integer year, String genre, Author author, Publisher publisher) {
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.author = author;
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }
}
