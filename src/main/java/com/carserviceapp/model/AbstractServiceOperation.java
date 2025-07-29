package com.carserviceapp.model;

import com.carserviceapp.interfaces.CostCalculable;
import com.carserviceapp.interfaces.Displayable;
import com.carserviceapp.interfaces.Identifiable;
import com.carserviceapp.interfaces.TimeEstimable;
import com.carserviceapp.util.UniqueIdGenerator;

import java.util.Objects;

/**
 * Abstract base class for any service operation (e.g., labor, part installation).
 */
public abstract class AbstractServiceOperation implements Identifiable, Displayable, CostCalculable, TimeEstimable {
    protected final String operationId;
    protected String description;
    protected double estimatedDurationHours; // Time estimate

    public AbstractServiceOperation(String description, double estimatedDurationHours) {
        this.operationId = UniqueIdGenerator.getInstance().generateId("OP");
        this.description = description;
        this.estimatedDurationHours = estimatedDurationHours;
    }

    @Override
    public String getId() {
        return operationId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getEstimatedDurationHours() {
        return estimatedDurationHours;
    }

    public void setEstimatedDurationHours(double estimatedDurationHours) {
        this.estimatedDurationHours = estimatedDurationHours;
    }

    @Override
    public double estimateTime() {
        return estimatedDurationHours;
    }

    @Override
    public String getDisplayInfo() {
        return "Operation ID: " + operationId + ", Description: " + description + ", Est. Time: " + String.format("%.1f", estimatedDurationHours) + " hrs";
    }

    @Override
    public String toString() {
        return getDisplayInfo();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractServiceOperation that = (AbstractServiceOperation) o;
        return Objects.equals(operationId, that.operationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationId);
    }
}