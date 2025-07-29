package com.carserviceapp.model;

import com.carserviceapp.annotations.Auditable;
import com.carserviceapp.annotations.BusinessRule;
import com.carserviceapp.annotations.BusinessValidation;
import com.carserviceapp.interfaces.CostCalculable;
import com.carserviceapp.interfaces.Displayable;
import com.carserviceapp.interfaces.Identifiable;
import com.carserviceapp.util.UniqueIdGenerator;

/**
 * Represents a physical part used in repairs.
 */
@Auditable(level = Auditable.AuditLevel.STANDARD, auditPrefix = "INVENTORY")
public record Part(
        String partId,
        @BusinessValidation(type = BusinessValidation.ValidationType.INVENTORY_VALIDATION,
                required = true, minLength = 1, maxLength = 100)
        String name,
        @BusinessValidation(type = BusinessValidation.ValidationType.COST_VALIDATION,
                required = true, minValue = 0.0, maxValue = 100000.0)
        double unitPrice,
        @BusinessValidation(type = BusinessValidation.ValidationType.INVENTORY_VALIDATION,
                required = true, minValue = 0, maxValue = 10000)
        int stockQuantity
) implements Identifiable, Displayable, CostCalculable {

    /**
     * Constructor with auto-generated ID
     */
    public Part(String name, double unitPrice, int stockQuantity) {
        this(UniqueIdGenerator.getInstance().generateId("PART"), name, unitPrice, stockQuantity);
    }

    @Override
    public String getId() {
        return partId;
    }

    /**
     * Reduces the stock quantity by a given amount.
     *
     * @param quantity The amount to reduce.
     * @return true if stock was successfully reduced, false otherwise.
     */
    @BusinessRule(type = BusinessRule.RuleType.INVENTORY_MANAGEMENT, priority = 2, critical = true)
    public boolean reduceStock(int quantity) {
        // Note: Since this is a record, we can't modify the stockQuantity directly
        // In a real application, you might want to use a mutable wrapper or
        // return a new Part instance with updated stock
        return this.stockQuantity >= quantity;
    }

    /**
     * Creates a new Part instance with reduced stock
     */
    public Part withReducedStock(int quantity) {
        if (this.stockQuantity >= quantity) {
            return new Part(this.partId, this.name, this.unitPrice, this.stockQuantity - quantity);
        }
        throw new IllegalArgumentException("Insufficient stock");
    }

    @Override
    @BusinessRule(type = BusinessRule.RuleType.COST_CALCULATION, priority = 1)
    public double calculateCost() {
        return unitPrice; // Cost of one unit
    }

    @Override
    public String getDisplayInfo() {
        return "Part ID: " + partId + ", Name: " + name + ", Unit Price: $" + String.format("%.2f", unitPrice) + ", Stock: " + stockQuantity;
    }

    @Override
    public String toString() {
        return getDisplayInfo();
    }
}