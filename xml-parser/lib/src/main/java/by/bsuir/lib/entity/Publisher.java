package by.bsuir.lib.entity;

import java.io.Serializable;

public class Publisher implements Serializable {
    private String name;
    private Address address;

    public Publisher(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    @Override
    public String toString() {
        return "  Name: " + name + ",\n" 
                + "  Address: " + address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public static class Address implements Serializable {
        private String city;
        private String country;
        
        public Address(String city, String county) {
            this.city = city;
            this.country = county;
        }

        @Override
        public String toString() {
            return city + ", " + country;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String county) {
            this.country = county;
        }
    }
}
