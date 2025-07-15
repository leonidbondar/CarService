package com.carserviceapp.model;

import java.util.Objects;

/**
 * Represents a technician (mechanic) who performs service operations.
 */
public class Technician extends Employee {
    private String qualification;
    private double hourlyRate;

    public Technician(String firstName, String lastName, String email, String phone, String qualification, double hourlyRate) {
        super("TECH", firstName, lastName, email, phone, 0.0); // Salary can be managed separately or derived from hourlyRate
        this.qualification = qualification;
        this.hourlyRate = hourlyRate;
    }

    public String getTechnicianId() {
        return this.id;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @Override
    public String getDisplayInfo() {
        return "Technician ID: " + id + ", Name: " + firstName + " " + lastName + ", Qualification: " + qualification + ", Hourly Rate: $" + hourlyRate;
    }

    @Override
    public String toString() {
        return getDisplayInfo();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Technician that = (Technician) o;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}