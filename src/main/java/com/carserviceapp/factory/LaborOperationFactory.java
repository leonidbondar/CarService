package com.carserviceapp.factory;

import com.carserviceapp.model.AbstractServiceOperation;
import com.carserviceapp.model.LaborOperation;
import com.carserviceapp.model.Part;
import com.carserviceapp.model.Technician;

/**
 * Concrete factory for creating LaborOperation objects.
 */
public class LaborOperationFactory implements ServiceOperationFactory {
    @Override
    public AbstractServiceOperation createServiceOperation(String description, double estimatedDurationHours, Technician technician, double hoursWorked) {
        return new LaborOperation(description, estimatedDurationHours, technician, hoursWorked);
    }

    @Override
    public AbstractServiceOperation createServiceOperation(String description, double estimatedDurationHours, Part part, int quantityUsed) {
        // Not applicable for LaborOperationFactory, return null or throw exception
        return null;
    }
}