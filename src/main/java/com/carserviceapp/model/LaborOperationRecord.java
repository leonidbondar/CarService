package com.carserviceapp.model;

import com.carserviceapp.annotations.Auditable;
import com.carserviceapp.annotations.BusinessRule;
import com.carserviceapp.annotations.BusinessValidation;
import com.carserviceapp.interfaces.CostCalculable;
import com.carserviceapp.interfaces.Displayable;
import com.carserviceapp.interfaces.Identifiable;
import com.carserviceapp.interfaces.TimeEstimable;
import com.carserviceapp.util.UniqueIdGenerator;

/**
 * Represents a labor-based service operation as a record.
 */
@Auditable(level = Auditable.AuditLevel.STANDARD, auditPrefix = "LABOR")
public record LaborOperationRecord(
        String operationId,
        @BusinessValidation(type = BusinessValidation.ValidationType.STANDARD,
                required = true, minLength = 5, maxLength = 500)
        String description,
        @BusinessValidation(type = BusinessValidation.ValidationType.TIME_VALIDATION,
                required = true, minValue = 0.1, maxValue = 100.0)
        double estimatedDurationHours,
        @BusinessValidation(type = BusinessValidation.ValidationType.STANDARD,
                required = true)
        Technician assignedTechnician,
        @BusinessValidation(type = BusinessValidation.ValidationType.TIME_VALIDATION,
                required = true, minValue = 0.0, maxValue = 200.0)
        double hoursWorked
) implements Identifiable, Displayable, CostCalculable, TimeEstimable {

    /**
     * Constructor with auto-generated ID
     */
    public LaborOperationRecord(String description, double estimatedDurationHours, Technician assignedTechnician, double hoursWorked) {
        this(UniqueIdGenerator.generateId("OP"), description, estimatedDurationHours, assignedTechnician, hoursWorked);
    }

    @Override
    public String getId() {
        return operationId;
    }

    @Override
    @BusinessRule(type = BusinessRule.RuleType.COST_CALCULATION, priority = 1)
    public double calculateCost() {
        // Cost of labor is hours worked multiplied by technician's hourly rate
        return hoursWorked * assignedTechnician.getHourlyRate();
    }

    @Override
    @BusinessRule(type = BusinessRule.RuleType.TIME_ESTIMATION, priority = 2)
    public double estimateTime() {
        return estimatedDurationHours;
    }

    @Override
    public String getDisplayInfo() {
        return "Operation ID: " + operationId +
                ", Description: " + description +
                ", Est. Time: " + String.format("%.1f", estimatedDurationHours) + " hrs" +
                ", Type: Labor, Technician: " + assignedTechnician.getFirstName() + " " + assignedTechnician.getLastName() +
                ", Hours: " + String.format("%.1f", hoursWorked) +
                ", Cost: $" + String.format("%.2f", calculateCost());
    }

    @Override
    public String toString() {
        return getDisplayInfo();
    }
} 