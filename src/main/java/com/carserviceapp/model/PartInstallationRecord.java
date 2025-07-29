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
 * Represents a service operation involving the installation of a part as a record.
 */
@Auditable(level = Auditable.AuditLevel.STANDARD, auditPrefix = "PART_INSTALL")
public record PartInstallationRecord(
        String operationId,
        @BusinessValidation(type = BusinessValidation.ValidationType.STANDARD,
                required = true, minLength = 5, maxLength = 500)
        String description,
        @BusinessValidation(type = BusinessValidation.ValidationType.TIME_VALIDATION,
                required = true, minValue = 0.1, maxValue = 100.0)
        double estimatedDurationHours,
        @BusinessValidation(type = BusinessValidation.ValidationType.INVENTORY_VALIDATION,
                required = true)
        Part partUsed,
        @BusinessValidation(type = BusinessValidation.ValidationType.INVENTORY_VALIDATION,
                required = true, minValue = 1, maxValue = 100)
        int quantityUsed
) implements Identifiable, Displayable, CostCalculable, TimeEstimable {

    /**
     * Constructor with auto-generated ID
     */
    public PartInstallationRecord(String description, double estimatedDurationHours, Part partUsed, int quantityUsed) {
        this(UniqueIdGenerator.getInstance().generateId("OP"), description, estimatedDurationHours, partUsed, quantityUsed);
    }

    @Override
    public String getId() {
        return operationId;
    }

    @Override
    @BusinessRule(type = BusinessRule.RuleType.COST_CALCULATION, priority = 1)
    public double calculateCost() {
        // Cost of part installation is quantity used multiplied by part's unit price
        return quantityUsed * partUsed.unitPrice();
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
                ", Type: Part Installation, Part: " + partUsed.name() + " (x" + quantityUsed + ")" +
                ", Cost: $" + String.format("%.2f", calculateCost());
    }

    @Override
    public String toString() {
        return getDisplayInfo();
    }
} 