package by.bsuir.xmlparser.common.entity;

import java.io.Serializable;

public class Author implements Serializable {
    private String name;
    private Integer yearOfBirth;

    public Author(String name, Integer yearOfBirth) {
        this.name = name;
        this.yearOfBirth = yearOfBirth;
    }

    @Override
    public String toString() {
        return "  Name: " + name + ",\n" 
                + "  Year of birth: " + yearOfBirth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(Integer yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }
}
