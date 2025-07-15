package com.carserviceapp.model;

import com.carserviceapp.interfaces.Displayable;
import com.carserviceapp.interfaces.Identifiable;
import com.carserviceapp.util.UniqueIdGenerator;

import java.util.Objects;

/**
 * Abstract base class for any person interacting with the system.
 */
public abstract class AbstractPerson implements Identifiable, Displayable {
    protected final String id;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phone;

    public AbstractPerson(String idPrefix, String firstName, String lastName, String email, String phone) {
        this.id = UniqueIdGenerator.generateId(idPrefix);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getDisplayInfo() {
        return "ID: " + id + ", Name: " + firstName + " " + lastName + ", Email: " + email + ", Phone: " + phone;
    }

    @Override
    public String toString() {
        return getDisplayInfo();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractPerson that = (AbstractPerson) o;
        return Objects.equals(id, that.id); // ID is sufficient for equality
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Hash code based on ID
    }
}