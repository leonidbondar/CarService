package com.carserviceapp.model;

import java.util.Objects;

/**
 * Represents a service operation involving the installation of a part.
 */
public class PartInstallation extends AbstractServiceOperation {
    private Part partUsed;
    private int quantityUsed;

    public PartInstallation(String description, double estimatedDurationHours, Part partUsed, int quantityUsed) {
        super(description, estimatedDurationHours);
        this.partUsed = partUsed;
        this.quantityUsed = quantityUsed;
    }

    public Part getPartUsed() {
        return partUsed;
    }

    public void setPartUsed(Part partUsed) {
        this.partUsed = partUsed;
    }

    public int getQuantityUsed() {
        return quantityUsed;
    }

    public void setQuantityUsed(int quantityUsed) {
        this.quantityUsed = quantityUsed;
    }

    @Override
    public double calculateCost() {
        // Cost of part installation is quantity used multiplied by part's unit price
        return quantityUsed * partUsed.getUnitPrice();
    }

    @Override
    public String getDisplayInfo() {
        return super.getDisplayInfo() + ", Type: Part Installation, Part: " + partUsed.getName() + " (x" + quantityUsed + "), Cost: $" + String.format("%.2f", calculateCost());
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
        PartInstallation that = (PartInstallation) o;
        return Objects.equals(this.operationId, that.operationId);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}