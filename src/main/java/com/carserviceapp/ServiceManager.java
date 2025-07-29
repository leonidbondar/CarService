package com.carserviceapp;

import com.carserviceapp.factory.*;
import com.carserviceapp.interfaces.ReportGenerator;
import com.carserviceapp.model.*;
import com.carserviceapp.service.ComplexEngineRepairProcess;
import com.carserviceapp.service.FinancialReport;
import com.carserviceapp.service.ServicePerformanceReport;
import com.carserviceapp.service.StandardCarRepairProcess;
import com.carserviceapp.util.AnnotationProcessor;
import com.carserviceapp.util.InputValidator;
import com.carserviceapp.util.ReportWrapper;
import com.carserviceapp.util.UniqueIdGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manages all aspects of the car service application,
 * including customers, vehicles, service requests, invoices, payments,
 * appointments, and insurance claims.
 */
public class ServiceManager {
    private static final Logger logger = LogManager.getLogger(ServiceManager.class);

    private final Map<String, Customer> customers = new HashMap<>();

    public Map<String, AbstractVehicle> getVehicles() {
        return vehicles;
    }

    private final Map<String, AbstractVehicle> vehicles = new HashMap<>();
    private final Map<String, Technician> technicians = new HashMap<>();
    private final Map<String, Part> parts = new HashMap<>();

    public Map<String, ServiceRequest> getServiceRequests() {
        return serviceRequests;
    }

    private final Map<String, ServiceRequest> serviceRequests = new HashMap<>();
    private final Map<String, Invoice> invoices = new HashMap<>();
    private final Map<String, Payment> payments = new HashMap<>();
    private final Map<String, Appointment> appointments = new HashMap<>();
    private final Map<String, InsuranceClaim> insuranceClaims = new HashMap<>();
    private final Map<String, List<AbstractVehicle>> customerVehicles = new HashMap<>();

    private final VehicleFactory carFactory = new CarFactory();
    private final ServiceOperationFactory laborFactory = new LaborOperationFactory();
    private final ServiceOperationFactory partInstallationFactory = new PartInstallationFactory();

    public ServiceManager() {
        seedData();
    }

    private void seedData() {
        Customer cust1 = new Customer("Alice", "Smith", "alice@example.com", "123-456-7890");
        Customer cust2 = new Customer("Bob", "Johnson", "bob@example.com", "098-765-4321");
        addCustomer(cust1);
        addCustomer(cust2);

        AbstractVehicle car1 = carFactory.createVehicle(VehicleMake.TOYOTA, "Camry", 2018, "ABC-123", "VIN1234567890");
        AbstractVehicle car2 = carFactory.createVehicle(VehicleMake.HONDA, "Civic", 2020, "XYZ-789", "VIN0987654321");
        AbstractVehicle car3 = carFactory.createVehicle(VehicleMake.FORD, "Focus", 2015, "DEF-456", "VIN9876543210");

        addVehicle(car1, cust1);
        addVehicle(car2, cust2);
        addVehicle(car3, cust1);

        Technician tech1 = new Technician("Jane", "Doe", "jane@example.com", "555-1111", "Certified Mechanic", 50.0);
        Technician tech2 = new Technician("Mike", "Brown", "mike@example.com", "555-2222", "Master Technician", 75.0);
        addTechnician(tech1);
        addTechnician(tech2);

        Part oilFilter = new Part("Oil Filter", 15.0, 50);
        Part brakePads = new Part("Brake Pads", 75.0, 20);
        Part sparkPlug = new Part("Spark Plug", 10.0, 100);
        addPart(oilFilter);
        addPart(brakePads);
        addPart(sparkPlug);

        ServiceRequest req1 = createServiceRequest(cust1, car1, "Engine noise and oil change");
        addServiceOperationToRequest(req1.getId(), createLaborOperation("Diagnostic", 1.0, tech1, 1.0));
        addServiceOperationToRequest(req1.getId(), createPartInstallation("Oil Filter Replacement", 0.5, oilFilter, 1));
        addServiceOperationToRequest(req1.getId(), createLaborOperation("Oil Change Labor", 0.5, tech1, 0.5));
        updateServiceRequestStatus(req1.getId(), ServiceStatus.COMPLETED);

        // Execute a repair process for this service
        ComplexEngineRepairProcess repairProcess = new ComplexEngineRepairProcess();
        repairProcess.runFullProcess(req1);

        StandardCarRepairProcess standardRepair = new StandardCarRepairProcess(this);
        standardRepair.executeRepair(req1);

        Invoice inv1 = generateInvoice(req1);
        recordPayment(inv1, inv1.getAmount(), Payment.PaymentMethod.CASH);

        scheduleAppointment(cust1, car1, req1, LocalDateTime.now().plusDays(2).withHour(10).withMinute(0), "Post-service checkup");
        fileInsuranceClaim(req1, "AutoInsure Inc.", "POL987654", inv1.getAmount() * 0.8);

        // Demonstrate custom annotations and record features
        demonstrateAnnotationFeatures();
    }

    public void addCustomer(Customer customer) {
        customers.put(customer.getId(), customer);
        customerVehicles.putIfAbsent(customer.getId(), new ArrayList<>());
        logger.info("Added customer: {}", customer.getDisplayInfo());
    }

    public void addVehicle(AbstractVehicle vehicle, Customer owner) {
        vehicles.put(vehicle.getId(), vehicle);
        if (owner != null) {
            customerVehicles.computeIfAbsent(owner.getId(), k -> new ArrayList<>()).add(vehicle);
        }
        assert owner != null;
        logger.info("Added vehicle: {} for customer: {}", vehicle.getDisplayInfo(), owner.getDisplayInfo());
    }

    public void addTechnician(Technician technician) {
        technicians.put(technician.getId(), technician);
        logger.info("Added technician: {}", technician.getDisplayInfo());
    }

    public void addPart(Part part) {
        parts.put(part.getId(), part);
        logger.info("Added part: {}", part.getDisplayInfo());
    }

    public ServiceRequest createServiceRequest(Customer customer, AbstractVehicle vehicle, String problemDescription) {
        ServiceRequest request = new ServiceRequest(customer, vehicle, LocalDate.now(), problemDescription);
        serviceRequests.put(request.getId(), request);
        logger.info("Created service request: {}", request.getId());
        return request;
    }

    public void updateServiceRequestStatus(String requestId, ServiceStatus newStatus) {
        ServiceRequest request = serviceRequests.get(requestId);
        if (request != null) {
            request.setStatus(newStatus);
            logger.info("Updated service request {} status to: {}", requestId, newStatus);
        } else {
            logger.error("Service request not found: {}", requestId);
        }
    }

    public void addServiceOperationToRequest(String requestId, AbstractServiceOperation operation) {
        ServiceRequest request = serviceRequests.get(requestId);
        if (request != null) {
            request.addOperation(operation);
            logger.info("Added operation to request {}: {}", requestId, operation.getDescription());
        } else {
            logger.warn("Request not found: {}", requestId);
        }
    }

    public Invoice generateInvoice(ServiceRequest request) {
        Invoice invoice = new Invoice(request);
        invoices.put(invoice.getId(), invoice);
        logger.info("Generated invoice {} for request {}", invoice.getId(), request.getId());
        return invoice;
    }

    public Payment recordPayment(Invoice invoice, double amount, Payment.PaymentMethod method) {
        Payment payment = new Payment(invoice, amount, LocalDate.now(), method);
        payments.put(payment.getId(), payment);
        logger.info("Recorded payment {} for invoice {}", payment.getId(), invoice.getId());
        return payment;
    }

    public Appointment scheduleAppointment(Customer customer, AbstractVehicle vehicle, ServiceRequest serviceRequest, LocalDateTime scheduledTime, String purpose) {
        Appointment appointment = new Appointment(customer, vehicle, serviceRequest, scheduledTime, purpose);
        appointments.put(appointment.getId(), appointment);
        logger.info("Scheduled appointment {} for request {}", appointment.getId(), serviceRequest.getId());
        return appointment;
    }

    public InsuranceClaim fileInsuranceClaim(ServiceRequest request, String insuranceCompany, String policyNumber, double claimAmount) {
        InsuranceClaim claim = new InsuranceClaim(request, insuranceCompany, policyNumber, claimAmount);
        insuranceClaims.put(claim.getId(), claim);
        logger.info("Filed insurance claim {} for request {}", claim.getId(), request.getId());
        return claim;
    }

    public ReportGenerator generateFinancialReport() {
        ReportWrapper<Invoice> invoiceReport = new ReportWrapper<>(new ArrayList<>(invoices.values()));
        ReportWrapper<Payment> paymentReport = new ReportWrapper<>(new ArrayList<>(payments.values()));
        return new FinancialReport(invoiceReport.getItems(), paymentReport.getItems());
    }

    public ReportGenerator generateServicePerformanceReport() {
        List<ServiceRequest> completed = serviceRequests.values().stream()
                .filter(req -> req.getStatus() == ServiceStatus.COMPLETED)
                .collect(Collectors.toList());
        return new ServicePerformanceReport(completed);
    }

    public AbstractVehicle createCar(VehicleMake make, String model, int year, String licensePlate, String vin) {
        return carFactory.createVehicle(make, model, year, licensePlate, vin);
    }

    public AbstractServiceOperation createLaborOperation(String description, double estimatedDurationHours, Technician technician, double hoursWorked) {
        return laborFactory.createServiceOperation(description, estimatedDurationHours, technician, hoursWorked);
    }

    public AbstractServiceOperation createPartInstallation(String description, double estimatedDurationHours, Part part, int quantityUsed) {
        return partInstallationFactory.createServiceOperation(description, estimatedDurationHours, part, quantityUsed);
    }

    public List<AbstractServiceOperation> getLaborOperationsForRequest(String requestId) {
        ServiceRequest request = serviceRequests.get(requestId);
        if (request == null) return List.of();
        return request.getFilteredOperations(op -> op instanceof com.carserviceapp.model.LaborOperation);
    }

    public void applyDiscountToInvoice(String invoiceId, double discountPercent) {
        Invoice invoice = invoices.get(invoiceId);
        if (invoice != null) {
            invoice.applyCostAdjuster(cost -> cost * (1 - discountPercent / 100.0));
        }
    }

    /**
     * Demonstrates the custom annotation features and record conversions.
     * This method showcases business validation, audit tracking, and business rule execution.
     */
    public void demonstrateAnnotationFeatures() {
        logger.info("=== Demonstrating Custom Annotations and Records ===");

        // 1. Demonstrate record-based classes
        demonstrateRecordClasses();

        // 2. Demonstrate business validation
        demonstrateBusinessValidation();

        // 3. Demonstrate audit processing
        demonstrateAuditProcessing();

        // 4. Demonstrate business rule execution
        demonstrateBusinessRules();

        logger.info("=== Annotation Demonstration Completed ===");
    }

    /**
     * Demonstrates the new record-based classes
     */
    private void demonstrateRecordClasses() {
        logger.info("--- Record Classes Demonstration ---");

        // Create record-based objects
        Part part = new Part("Air Filter", 25.50, 15);
        Technician tech = new Technician("John", "Doe", "john@example.com", "555-1234", "ASE Certified", 25.0);
        LaborOperationRecord laborOp = new LaborOperationRecord("Replace air filter", 0.5, tech, 0.5);
        PartInstallationRecord partInstall = new PartInstallationRecord("Install new air filter", 0.25, part, 1);

        Customer customer = new Customer("Alice", "Brown", "alice@example.com", "555-2222");
        Car car = new Car(VehicleMake.BMW, "X3", 2022, "GHI789", "VIN789123");
        AppointmentRecord appointment = new AppointmentRecord(customer, car,
                LocalDateTime.now().plusDays(1), "Air filter replacement");

        // Create a service request for payment demonstration
        ServiceRequest serviceRequest = new ServiceRequest(customer, car, LocalDate.now(), "Air filter replacement");
        Invoice invoice = new Invoice(serviceRequest);
        PaymentRecord payment = new PaymentRecord(invoice, 45.99, LocalDate.now(), PaymentRecord.PaymentMethod.CREDIT_CARD);

        // Display record information
        logger.info("Part Record: {}", part);
        logger.info("Labor Operation Record: {}", laborOp);
        logger.info("Part Installation Record: {}", partInstall);
        logger.info("Appointment Record: {}", appointment);
        logger.info("Payment Record: {}", payment);

        // Show record accessors
        logger.info("Part ID: {}, Name: {}, Price: ${}, Stock: {}",
                part.partId(), part.name(), part.unitPrice(), part.stockQuantity());
        logger.info("Labor Cost: ${}", laborOp.calculateCost());
        logger.info("Installation Cost: ${}", partInstall.calculateCost());
    }

    /**
     * Demonstrates business validation using custom annotations
     */
    private void demonstrateBusinessValidation() {
        logger.info("--- Business Validation Demonstration ---");

        // Test valid part
        Part validPart = new Part("Brake Pads", 45.99, 10);
        AnnotationProcessor.ValidationResult validResult = AnnotationProcessor.getInstance().validateObject(validPart);
        logger.info("Valid Part Validation: {}", validResult.isValid());

        // Test invalid part (empty name, negative price)
        Part invalidPart = new Part("", -10.0, -5);
        AnnotationProcessor.ValidationResult invalidResult = AnnotationProcessor.getInstance().validateObject(invalidPart);
        logger.info("Invalid Part Validation: {}", invalidResult.isValid());
        if (!invalidResult.isValid()) {
            logger.error("Validation errors: {}", invalidResult.getErrors());
        }

        // Test ServiceRequest validation
        Customer customer = new Customer("Bob", "Johnson", "bob@example.com", "555-9999");
        Car car = new Car(VehicleMake.FORD, "Focus", 2021, "DEF456", "VIN456789");
        ServiceRequest serviceRequest = new ServiceRequest(customer, car, LocalDate.now(), "Brake inspection");
        AnnotationProcessor.ValidationResult srResult = AnnotationProcessor.getInstance().validateObject(serviceRequest);
        logger.info("Service Request Validation: {}", srResult.isValid());
    }

    /**
     * Demonstrates audit processing using custom annotations
     */
    private void demonstrateAuditProcessing() {
        logger.info("--- Audit Processing Demonstration ---");

        // Create objects with Auditable annotations
        Part part = new Part("Oil Filter", 12.99, 50);
        Customer customer = new Customer("Jane", "Smith", "jane@example.com", "555-5678");
        Car car = new Car(VehicleMake.HONDA, "Civic", 2019, "XYZ789", "VIN789012");
        ServiceRequest serviceRequest = new ServiceRequest(customer, car, LocalDate.now(), "Oil change");
        Invoice invoice = new Invoice(serviceRequest);
        PaymentRecord payment = new PaymentRecord(invoice, 45.99, LocalDate.now(), PaymentRecord.PaymentMethod.CREDIT_CARD);

        // Process audit events
        AnnotationProcessor.getInstance().processAudit(part, "CREATED");
        AnnotationProcessor.getInstance().processAudit(serviceRequest, "CREATED");
        AnnotationProcessor.getInstance().processAudit(payment, "PROCESSED");
        AnnotationProcessor.getInstance().processAudit(part, "STOCK_REDUCED");
    }

    /**
     * Demonstrates business rule execution using custom annotations
     */
    private void demonstrateBusinessRules() {
        logger.info("--- Business Rules Demonstration ---");

        // Create a service request with operations
        Customer customer = new Customer("Bob", "Johnson", "bob@example.com", "555-9999");
        Car car = new Car(VehicleMake.FORD, "Focus", 2021, "DEF456", "VIN456789");
        ServiceRequest serviceRequest = new ServiceRequest(customer, car, LocalDate.now(), "Brake inspection");

        // Execute business rules
        AnnotationProcessor.BusinessRuleResult costResult = AnnotationProcessor.getInstance().executeBusinessRule(serviceRequest, "calculateCost");
        logger.info("Cost calculation result: {} - {}", costResult.isSuccess(), costResult.getMessage());

        AnnotationProcessor.BusinessRuleResult timeResult = AnnotationProcessor.getInstance().executeBusinessRule(serviceRequest, "estimateTime");
        logger.info("Time estimation result: {} - {}", timeResult.isSuccess(), timeResult.getMessage());

        // Get all business rules for classes
        var partRules = AnnotationProcessor.getInstance().getBusinessRules(Part.class);
        var srRules = AnnotationProcessor.getInstance().getBusinessRules(ServiceRequest.class);

        logger.info("Business rules in Part class:");
        for (var rule : partRules) {
            logger.info("  {}", rule);
        }

        logger.info("Business rules in ServiceRequest class:");
        for (var rule : srRules) {
            logger.info("  {}", rule);
        }
    }

    /**
     * Demonstrates singleton usage in a multithreaded context.
     */
    public void demoSingletonsWithThreads() {
        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            String id = UniqueIdGenerator.getInstance().generateId("THR");
            boolean valid = InputValidator.getInstance().getStringInput("Thread " + threadName + " enter any string (demo): ").isEmpty();
            AnnotationProcessor.getInstance().processAudit(id, "GENERATED");
            logger.info("[{}] Generated ID: {}, Input valid: {}", threadName, id, valid);
        };
        Thread[] threads = new Thread[3];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(task, "DemoThread-" + (i + 1));
            threads[i].start();
        }
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                logger.error("Thread interrupted", e);
            }
        }
        logger.info("All singleton demo threads completed.");
    }
}
