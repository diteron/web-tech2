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

    public boolean isContainsAnyData() {
        return title != null || author != null || yearOfPublication != null
                || type != null;
    }

    public boolean isContainsAllData() {
        return title != null && author != null && yearOfPublication != null
                && type != null;
    }

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
