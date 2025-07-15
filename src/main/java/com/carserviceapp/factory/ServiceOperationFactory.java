package com.carserviceapp.factory;

import com.carserviceapp.model.AbstractServiceOperation;
import com.carserviceapp.model.Part;
import com.carserviceapp.model.Technician;

/**
 * Abstract factory for creating different types of service operations.
 */
public interface ServiceOperationFactory {
    AbstractServiceOperation createServiceOperation(String description, double estimatedDurationHours, Technician technician, double hoursWorked); // For Labor

    AbstractServiceOperation createServiceOperation(String description, double estimatedDurationHours, Part part, int quantityUsed); // For Part Installation
}