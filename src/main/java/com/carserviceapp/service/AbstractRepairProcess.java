package com.carserviceapp.service;

import com.carserviceapp.model.ServiceRequest;
import com.carserviceapp.model.ServiceStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract class defining a template method for a car repair process.
 * (Template Method Design Pattern)
 */
public abstract class AbstractRepairProcess {
    private static final Logger logger = LogManager.getLogger(AbstractRepairProcess.class);

    /**
     * The template method that defines the skeleton of the repair process.
     * Subclasses implement the specific steps.
     *
     * @param request The ServiceRequest to execute the repair process on.
     */
    public final void executeRepair(ServiceRequest request) {
        logger.info("Initiating repair process for Service Request {}...", request.getId());
        request.setStatus(ServiceStatus.IN_PROGRESS);

        diagnose(request);
        orderParts(request);
        performActualRepair(request);
        qualityCheck(request);

        request.setStatus(ServiceStatus.COMPLETED);
        logger.info("Repair process for Service Request {} completed.", request.getId());
    }

    protected abstract void diagnose(ServiceRequest request);

    protected abstract void orderParts(ServiceRequest request);

    protected abstract void performActualRepair(ServiceRequest request);

    protected abstract void qualityCheck(ServiceRequest request);
}
