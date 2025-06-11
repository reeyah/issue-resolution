package main.java.com.cxresolution.entity;

public class Customer {
    private final String email;
    private final String name; // Could be extracted from other systems

    public Customer(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() { return email; }
}