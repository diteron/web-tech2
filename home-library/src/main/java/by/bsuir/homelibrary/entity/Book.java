package by.bsuir.homelibrary.entity;

public class Book {
    public enum Type {
        EBOOK("e-book"),
        BOOK("book");

        private final String TYPE;

        Type(String string) {
            TYPE = string;
        }

        @Override
        public String toString() {
            return this.TYPE;
        }
    }

    private String title;
    private String author;
    private Integer yearOfPublication;
    private Type type;

    public Book(String title, String author, Integer yearOfPublication, Type type) {
        this.title = title;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(Integer yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }


    public static class Builder {
        private String title;
        private String author;
        private Integer yearOfPublication;
        private Type type;

        public Book build() {
            return new Book(this.title, this.author, this.yearOfPublication, this.type);
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder yearOfPublication(Integer yearOfPublication) {
            this.yearOfPublication = yearOfPublication;
            return this;
        }

        public Builder type(Type type) {
            this.type = type;
            return this;
        }
    }


    @Override
    public String toString() {
        return title + "\n"
                + author + "\n"
                + yearOfPublication + "\n"
                + type;
    }
}
