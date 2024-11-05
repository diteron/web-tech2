package by.bsuir.homelibrary.entity;

/**
 * Represents a book with a title, author, year of publication, and type.
 * <p>
 * The {@code Book} class provides basic functionality to get and set the
 * properties of a book, as well as methods to check if any or all data fields are set.
 * It also overrides {@code toString()}, {@code hashCode()}, and {@code equals()} methods.
 * </p>
 */
public class Book {
    /**
     * Enum representing the types of books.
     * <p>
     * The {@code Type} enum includes options for an e-book and a physical book.
     * Each type has a string representation for easier display.
     * </p>
     */
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

        /**
         * Returns the {@code Type} that matches the specified string, ignoring case.
         *
         * @param type the string representation of the type
         * @return the matching {@code Type} enum constant
         * @throws IllegalArgumentException if no matching type is found
         */
        public static Type fromString(String type) {
            for (Type t : Type.values()) {
                if (t.TYPE.equalsIgnoreCase(type)) {
                    return t;
                }
            }
            throw new IllegalArgumentException("No enum constant with type: " + type);
        }
    }

    private String title;
    private String author;
    private Integer yearOfPublication;
    private Type type;

    public Book() {

    }

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

    /**
     * Checks if any data is set for this book.
     *
     * @return {@code true} if any field is non-null; {@code false} otherwise
     */
    public boolean isContainsAnyData() {
        return title != null || author != null || yearOfPublication != null
                || type != null;
    }

    /**
     * Checks if all data fields are set for this book.
     *
     * @return {@code true} if all fields are non-null; {@code false} otherwise
     */
    public boolean isContainsAllData() {
        return title != null && author != null && yearOfPublication != null
                && type != null;
    }

    /**
     * Returns a string representation of the book, with each field on a new line.
     *
     * @return a formatted string representation of the book
     */
    @Override
    public String toString() {
        return title + "\n"
                + author + "\n"
                + yearOfPublication + "\n"
                + type;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;

        result = PRIME * result + ((title == null) ? 0 : title.hashCode());
        result = PRIME * result + ((author == null) ? 0 : author.hashCode());
        result = PRIME * result + ((yearOfPublication == null) ? 0 : yearOfPublication.hashCode());
        result = PRIME * result + ((type == null) ? 0 : type.hashCode());
        
        return result;
    }

    /**
     * Checks if this book is equal to another object.
     * <p>
     * Two books are considered equal if they have the same title, author,
     * year of publication, and type.
     * </p>
     *
     * @param obj the object to compare with this book
     * @return {@code true} if the objects are equal; {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;

        Book other = (Book) obj;

        if (this.title == null && other.title != null) 
            return false;
        else if (!this.title.equals(other.title))
            return false;

        if (this.author == null && other.author != null)
            return false;
        else if (!this.author.equals(other.author))
            return false;
            
        if (this.yearOfPublication == null && other.yearOfPublication != null)
            return false;
        else if (!this.yearOfPublication.equals(other.yearOfPublication))
            return false;

        if (this.type == null && other.type != null)
            return false;
        else if (!this.type.equals(other.type))
            return false;

        return true;
    }
}
