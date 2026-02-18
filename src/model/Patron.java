package model;

public class Patron {

    private String name;
    private int registrationNumber;

    public Patron(String name, int registrationNumber) {
        this.name = name;
        this.registrationNumber = registrationNumber;
    }

    public String getName() {
        return name;
    }

    public int getRegistrationNumber() {
        return registrationNumber;
    }

    @Override
    public String toString() {
        return name;
    }
}
