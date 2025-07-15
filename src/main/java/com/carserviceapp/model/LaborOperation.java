package com.carserviceapp.model;

import java.util.Objects;

/**
 * Represents a labor-based service operation.
 */
public class LaborOperation extends AbstractServiceOperation {
    private Technician assignedTechnician;
    private double hoursWorked;

    public LaborOperation(String description, double estimatedDurationHours, Technician assignedTechnician, double hoursWorked) {
        super(description, estimatedDurationHours);
        this.assignedTechnician = assignedTechnician;
        this.hoursWorked = hoursWorked;
    }

    public Technician getAssignedTechnician() {
        return assignedTechnician;
    }

    public void setAssignedTechnician(Technician assignedTechnician) {
        this.assignedTechnician = assignedTechnician;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    @Override
    public double calculateCost() {
        // Cost of labor is hours worked multiplied by technician's hourly rate
        return hoursWorked * assignedTechnician.getHourlyRate();
    }

    @Override
    public String getDisplayInfo() {
        return super.getDisplayInfo() + ", Type: Labor, Technician: " + assignedTechnician.getFirstName() + " " + assignedTechnician.getLastName() + ", Hours: " + String.format("%.1f", hoursWorked) + ", Cost: $" + String.format("%.2f", calculateCost());
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
        LaborOperation that = (LaborOperation) o;
        return Objects.equals(this.operationId, that.operationId);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}