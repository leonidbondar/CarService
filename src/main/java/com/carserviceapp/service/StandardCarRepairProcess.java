package com.carserviceapp.service;

import com.carserviceapp.ServiceManager;
import com.carserviceapp.model.ServiceRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Concrete implementation of a standard car repair process.
 */
public class StandardCarRepairProcess extends AbstractRepairProcess {
    private static final Logger logger = LogManager.getLogger(StandardCarRepairProcess.class);

    private final ServiceManager serviceManager;

    public StandardCarRepairProcess(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    @Override
    protected void diagnose(ServiceRequest request) {
        logger.info("[Standard Repair] Performing basic diagnostic checks for {} {}",
                request.getVehicle().getMake(), request.getVehicle().getModel());
        // Example: Add a diagnostic labor operation
        serviceManager.addServiceOperationToRequest(
                request.getId(),
                serviceManager.createLaborOperation("Basic Diagnostic", 0.5, null, 0.5)
        );
    }

    @Override
    protected void orderParts(ServiceRequest request) {
        logger.info("[Standard Repair] Checking for common parts needed for {}",
                request.getProblemDescription().toLowerCase());
        // No specific parts ordered in this basic example
    }

    @Override
    protected void performActualRepair(ServiceRequest request) {
        logger.info("[Standard Repair] Executing basic repairs as described: {}",
                request.getProblemDescription());
        // Example: Add a general repair labor operation
        serviceManager.addServiceOperationToRequest(
                request.getId(),
                serviceManager.createLaborOperation("General Repair", 2.0, null, 2.0)
        );
    }

    @Override
    protected void qualityCheck(ServiceRequest request) {
        logger.info("[Standard Repair] Performing final quality inspection and test drive.");
    }
}
