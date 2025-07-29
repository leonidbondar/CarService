package com.carserviceapp.service;

import com.carserviceapp.model.*;
import com.carserviceapp.util.AnnotationProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Demonstration service showing how to use custom annotations with encapsulation.
 * This service showcases the business validation, audit tracking, and business rule
 * execution capabilities of the custom annotation system.
 */
public class AnnotationDemoService {
    private static final Logger logger = LogManager.getLogger(AnnotationDemoService.class);

    /**
     * Demonstrates validation of objects using BusinessValidation annotations
     */
    public void demonstrateValidation() {
        logger.info("=== Demonstrating Business Validation ===");

        // Create a valid part
        Part validPart = new Part("Brake Pads", 45.99, 10);
        AnnotationProcessor.ValidationResult validResult = AnnotationProcessor.getInstance().validateObject(validPart);
        logger.info("Valid Part Validation: " + validResult.isValid());
        if (!validResult.isValid()) {
            logger.error("Validation errors: " + validResult.getErrors());
        }

        // Create an invalid part (negative price)
        Part invalidPart = new Part("", -10.0, -5);
        AnnotationProcessor.ValidationResult invalidResult = AnnotationProcessor.getInstance().validateObject(invalidPart);
        logger.info("Invalid Part Validation: " + invalidResult.isValid());
        if (!invalidResult.isValid()) {
            logger.error("Validation errors: " + invalidResult.getErrors());
        }

        // Validate a service request
        Customer customer = new Customer("John", "Doe", "john@example.com", "555-1234");
        Car car = new Car(VehicleMake.TOYOTA, "Camry", 2020, "ABC123", "VIN123456");
        ServiceRequest serviceRequest = new ServiceRequest(customer, car, LocalDate.now(), "Engine noise");
        AnnotationProcessor.ValidationResult srResult = AnnotationProcessor.getInstance().validateObject(serviceRequest);
        logger.info("Service Request Validation: " + srResult.isValid());
    }

    /**
     * Demonstrates audit tracking using Auditable annotations
     */
    public void demonstrateAuditing() {
        logger.info("=== Demonstrating Audit Tracking ===");

        // Create objects with Auditable annotations
        Part part = new Part("Oil Filter", 12.99, 50);
        // Create a service request first for the invoice
        Customer jane = new Customer("Jane", "Smith", "jane@example.com", "555-5678");
        Car janeCar = new Car(VehicleMake.HONDA, "Civic", 2019, "XYZ789", "VIN789012");
        ServiceRequest janeRequest = new ServiceRequest(jane, janeCar, LocalDate.now(), "Oil change");
        Invoice invoice = new Invoice(janeRequest);

        PaymentRecord payment = new PaymentRecord(
                invoice,
                45.99,
                LocalDate.now(),
                PaymentRecord.PaymentMethod.CREDIT_CARD
        );

        // Process audit events
        AnnotationProcessor.getInstance().processAudit(part, "CREATED");
        AnnotationProcessor.getInstance().processAudit(payment, "PROCESSED");
        AnnotationProcessor.getInstance().processAudit(part, "STOCK_REDUCED");
    }

    /**
     * Demonstrates business rule execution using BusinessRule annotations
     */
    public void demonstrateBusinessRules() {
        logger.info("=== Demonstrating Business Rules ===");

        // Create a service request with operations
        Customer customer = new Customer("Bob", "Johnson", "bob@example.com", "555-9999");
        Car car = new Car(VehicleMake.FORD, "Focus", 2021, "DEF456", "VIN456789");
        ServiceRequest serviceRequest = new ServiceRequest(customer, car, LocalDate.now(), "Brake inspection");

        // Execute business rules
        AnnotationProcessor.BusinessRuleResult costResult = AnnotationProcessor.getInstance().executeBusinessRule(serviceRequest, "calculateCost");
        logger.info("Cost calculation result: " + costResult.isSuccess() + " - " + costResult.getMessage());

        AnnotationProcessor.BusinessRuleResult timeResult = AnnotationProcessor.getInstance().executeBusinessRule(serviceRequest, "estimateTime");
        logger.info("Time estimation result: " + timeResult.isSuccess() + " - " + timeResult.getMessage());

        // Get all business rules for a class
        List<AnnotationProcessor.BusinessRuleInfo> rules = AnnotationProcessor.getInstance().getBusinessRules(ServiceRequest.class);
        logger.info("Business rules in ServiceRequest:");
        for (AnnotationProcessor.BusinessRuleInfo rule : rules) {
            logger.info("  " + rule);
        }
    }

    /**
     * Demonstrates record-based classes with annotations
     */
    public void demonstrateRecords() {
        logger.info("=== Demonstrating Record Classes ===");

        // Create record-based objects
        Part part = new Part("Air Filter", 25.50, 15);
        LaborOperationRecord laborOp = new LaborOperationRecord(
                "Replace air filter",
                0.5,
                new Technician("Mike", "Wilson", "mike@example.com", "555-1111", "ASE Certified", 25.0),
                0.5
        );
        PartInstallationRecord partInstall = new PartInstallationRecord(
                "Install new air filter",
                0.25,
                part,
                1
        );
        AppointmentRecord appointment = new AppointmentRecord(
                new Customer("Alice", "Brown", "alice@example.com", "555-2222"),
                new Car(VehicleMake.BMW, "X3", 2022, "GHI789", "VIN789123"),
                LocalDateTime.now().plusDays(1),
                "Air filter replacement"
        );

        // Validate and audit record objects
        AnnotationProcessor.ValidationResult partValidation = AnnotationProcessor.getInstance().validateObject(part);
        AnnotationProcessor.ValidationResult laborValidation = AnnotationProcessor.getInstance().validateObject(laborOp);
        AnnotationProcessor.ValidationResult appointmentValidation = AnnotationProcessor.getInstance().validateObject(appointment);

        logger.info("Part validation: " + partValidation.isValid());
        logger.info("Labor operation validation: " + laborValidation.isValid());
        logger.info("Appointment validation: " + appointmentValidation.isValid());

        // Process audit events for records
        AnnotationProcessor.getInstance().processAudit(part, "CREATED");
        AnnotationProcessor.getInstance().processAudit(laborOp, "COMPLETED");
        AnnotationProcessor.getInstance().processAudit(appointment, "SCHEDULED");

        // Execute business rules on records
        AnnotationProcessor.BusinessRuleResult laborCostResult = AnnotationProcessor.getInstance().executeBusinessRule(laborOp, "calculateCost");
        AnnotationProcessor.BusinessRuleResult partCostResult = AnnotationProcessor.getInstance().executeBusinessRule(partInstall, "calculateCost");

        logger.info("Labor cost calculation: " + laborCostResult.isSuccess() + " - Result: " + laborCostResult.getResult());
        logger.info("Part installation cost calculation: " + partCostResult.isSuccess() + " - Result: " + partCostResult.getResult());
    }

    /**
     * Runs all demonstrations
     */
    public void runAllDemonstrations() {
        logger.info("Starting Annotation Demonstration Service");

        demonstrateValidation();
        demonstrateAuditing();
        demonstrateBusinessRules();
        demonstrateRecords();

        logger.info("Annotation Demonstration Service completed");
    }
} 