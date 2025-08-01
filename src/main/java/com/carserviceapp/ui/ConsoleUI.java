package com.carserviceapp.ui;

import com.carserviceapp.ServiceManager;
import com.carserviceapp.model.Customer;
import com.carserviceapp.model.ServiceRequest;
import com.carserviceapp.model.ServiceStatus;
import com.carserviceapp.model.VehicleMake;
import com.carserviceapp.service.CarService;
import com.carserviceapp.util.InputValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * All Scanner I/O here; delegates to CarService.
 */
public class ConsoleUI {
    private static final Logger log = LogManager.getLogger(ConsoleUI.class);
    private final CarService svc = new CarService();

    public void run() {
        log.info("=== CarServiceApp started ===");
        boolean exit = false;
        while (!exit) {
            showMenu();
            int option = InputValidator.getInstance().getPositiveIntegerInput("Option: ");
            switch (option) {
                case 1 -> addCar();
                case 2 -> removeCar();
                case 3 -> createRequest();
                case 4 -> listCars();
                case 5 -> listRequests();
                case 6 -> updateRequestStatus();
                case 7 -> removeRequest();
                case 8 -> demonstrateAnnotations();
                case 9 -> singletonMultithreadedDemo();
                case 10 -> exit = true;
                default -> log.warn("Unknown option");
            }
        }
        InputValidator.closeScanner();
        log.info("=== Goodbye ===");
    }

    private void showMenu() {
        log.info("""
                1) Add Car
                2) Remove Car
                3) Create Service Request
                4) List All Cars
                5) List Requests for Car
                6) Update Request Status
                7) Remove Service Request
                8) Demonstrate Annotations & Records
                9) Singleton/Multithreaded Demo
                10) Exit
                """);
    }

    private void addCar() {
        try {
            VehicleMake make = VehicleMake.valueOf(InputValidator.getInstance().getStringInput("Make: "));
            String model = InputValidator.getInstance().getStringInput("Model: ");
            int year = InputValidator.getInstance().getPositiveIntegerInput("Year: ");
            String plate = InputValidator.getInstance().getStringInput("License Plate: ");
            String vin = InputValidator.getInstance().getStringInput("VIN: ");
            svc.addCar(make, model, year, plate, vin);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }
    }

    private void removeCar() {
        try {
            String plate = InputValidator.getInstance().getStringInput("License Plate: ");
            svc.removeCar(plate);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }
    }

    private void createRequest() {
        try {
            String fn = InputValidator.getInstance().getStringInput("Customer first name: ");
            String ln = InputValidator.getInstance().getStringInput("Customer last name: ");
            String email = InputValidator.getInstance().getStringInput("Customer email: ");
            String phone = InputValidator.getInstance().getStringInput("Customer phone: ");
            Customer cust = new Customer(fn, ln, email, phone);

            String plate = InputValidator.getInstance().getStringInput("License Plate: ");
            LocalDate date = LocalDate.parse(
                    InputValidator.getInstance().getStringInput("Request Date (YYYY-MM-DD): ")
            );
            String problem = InputValidator.getInstance().getStringInput("Problem description: ");
            svc.createServiceRequest(cust, plate, date, problem);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }
    }

    private void listCars() {
        Collection<?> cars = svc.listAllCars();
        if (cars.isEmpty()) {
            log.info("No cars registered.");
        } else {
            cars.forEach(c -> log.info("{}", c));
        }
    }

    private void listRequests() {
        try {
            String plate = InputValidator.getInstance().getStringInput("License Plate: ");
            List<ServiceRequest> reqs = svc.listServiceRequests(plate);
            if (reqs.isEmpty()) {
                log.info("No requests for {}", plate);
            } else {
                reqs.forEach(r -> log.info("{}", r.getDisplayInfo()));
            }
        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }
    }

    private void updateRequestStatus() {
        try {
            String id = InputValidator.getInstance().getStringInput("Request ID: ");
            String st = InputValidator.getInstance().getStringInput("New status (PENDING|IN_PROGRESS|COMPLETED|CANCELLED): ");
            svc.updateServiceRequestStatus(id, ServiceStatus.valueOf(st));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }
    }

    private void removeRequest() {
        try {
            String id = InputValidator.getInstance().getStringInput("Request ID: ");
            svc.removeServiceRequest(id);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }
    }

    private void demonstrateAnnotations() {
        log.info("=== Demonstrating Custom Annotations and Records ===");
        try {
            // Create a ServiceManager instance to access the demonstration
            ServiceManager serviceManager = new ServiceManager();
            serviceManager.demonstrateAnnotationFeatures();
            log.info("Annotation demonstration completed successfully!");
        } catch (Exception e) {
            log.error("Error during annotation demonstration: {}", e.getMessage());
        }
    }

    private void singletonMultithreadedDemo() {
        log.info("=== Singleton/Multithreaded Demo ===");
        ServiceManager serviceManager = new ServiceManager();
        serviceManager.demoSingletonsWithThreads();
        log.info("Singleton/Multithreaded demo completed!");
    }
}
