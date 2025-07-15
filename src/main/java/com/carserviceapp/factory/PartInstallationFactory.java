package com.carserviceapp.factory;

import com.carserviceapp.model.AbstractServiceOperation;
import com.carserviceapp.model.Part;
import com.carserviceapp.model.PartInstallation;
import com.carserviceapp.model.Technician;

/**
 * Concrete factory for creating PartInstallation objects.
 */
public class PartInstallationFactory implements ServiceOperationFactory {
    @Override
    public AbstractServiceOperation createServiceOperation(String description, double estimatedDurationHours, Technician technician, double hoursWorked) {
        // Not applicable for PartInstallationFactory, return null or throw exception
        return null;
    }

    @Override
    public AbstractServiceOperation createServiceOperation(String description, double estimatedDurationHours, Part part, int quantityUsed) {
        return new PartInstallation(description, estimatedDurationHours, part, quantityUsed);
    }
}