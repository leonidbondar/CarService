package com.carserviceapp.model;

import java.util.Objects;

/**
 * Represents a customer of the car service.
 */
public class Customer extends AbstractPerson {
    // Customer specific attributes can be added here if needed

    public Customer(String firstName, String lastName, String email, String phone) {
        super("CUST", firstName, lastName, email, phone);
    }

    public String getCustomerId() {
        return this.id;
    }

    @Override
    public String getDisplayInfo() {
        return "Customer ID: " + id + ", Name: " + firstName + " " + lastName + ", Email: " + email + ", Phone: " + phone;
    }

    @Override
    public String toString() {
        return getDisplayInfo();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Use superclass equals for ID check
        Customer customer = (Customer) o;
        return Objects.equals(this.id, customer.id); // Redundant but explicit
    }

    @Override
    public int hashCode() {
        return super.hashCode(); // Use superclass hash code for ID check
    }
}