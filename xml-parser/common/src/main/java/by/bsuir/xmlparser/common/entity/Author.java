package by.bsuir.xmlparser.common.entity;

import java.io.Serializable;

public class Author implements Serializable {
    private String name;
    private String birthYear;

    public Author(String name, String birthYear) {
        this.name = name;
        this.birthYear = birthYear;
    }

    @Override
    public String toString() {
        return "  Name: " + name + ",\n" 
                + "  Year of birth: " + birthYear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }
}
