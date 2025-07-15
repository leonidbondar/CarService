package com.carserviceapp.service;

import com.carserviceapp.model.ServiceRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Concrete implementation of a more complex engine repair process.
 */
public class ComplexEngineRepairProcess extends AbstractRepairProcess {
    private static final Logger logger = LogManager.getLogger(ComplexEngineRepairProcess.class);

    private final Deque<String> repairSteps = new ArrayDeque<>();

    @Override
    protected void diagnose(ServiceRequest request) {
        String msg = String.format("Conducting diagnostics for %s %s", request.getVehicle().getMake(), request.getVehicle().getModel());
        repairSteps.push("diagnose");
        logger.info("[Complex Engine Repair] {}", msg);
        logger.info("    - Running ECU diagnostics, compression test, and visual inspection.");
    }

    @Override
    protected void orderParts(ServiceRequest request) {
        repairSteps.push("orderParts");
        logger.info("[Complex Engine Repair] Ordering specialized engine parts based on diagnostics.");
        logger.info("    - Checking availability of pistons, valves, and gaskets.");
    }

    @Override
    protected void performActualRepair(ServiceRequest request) {
        repairSteps.push("performRepair");
        logger.info("[Complex Engine Repair] Disassembling and repairing the engine.");
        logger.info("    - Performing cylinder head work, block repairs, and component replacement.");
    }

    @Override
    protected void qualityCheck(ServiceRequest request) {
        repairSteps.push("qualityCheck");
        logger.info("[Complex Engine Repair] Performing post-repair performance tests and road tests.");
        logger.info("    - Monitoring engine parameters for stability and efficiency.");
    }

    public Deque<String> getRepairStepsHistory() {
        return repairSteps;
    }

    /**
     * Exposes the full execution as a callable step
     */
    public void runFullProcess(ServiceRequest request) {
        diagnose(request);
        orderParts(request);
        performActualRepair(request);
        qualityCheck(request);
    }
}
