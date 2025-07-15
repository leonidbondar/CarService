package com.carserviceapp.service;

import com.carserviceapp.ServiceManager;
import com.carserviceapp.exceptions.DuplicateVehicleException;
import com.carserviceapp.exceptions.InvalidServiceRequestException;
import com.carserviceapp.exceptions.ServiceRequestNotFoundException;
import com.carserviceapp.exceptions.VehicleNotFoundException;
import com.carserviceapp.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * Core operations: manage cars and service requests.
 * Now delegates to ServiceManager for full domain logic.
 */
public class CarService {
    private static final Logger log = LogManager.getLogger(CarService.class);

    private final ServiceManager serviceManager = new ServiceManager();

    public void addCar(VehicleMake make,
                       String model,
                       int year,
                       String licensePlate,
                       String vin) {
        // Delegate vehicle creation + addition to ServiceManager
        if (serviceManager.getVehicles().values().stream()
                .anyMatch(v -> licensePlate.equalsIgnoreCase(v.getLicensePlate()))) {
            throw new DuplicateVehicleException(licensePlate);
        }
        AbstractVehicle car = serviceManager.createCar(make, model, year, licensePlate, vin);
        serviceManager.addVehicle(car, null); // null owner, or extend to accept owner param
        log.info("Added car: {}", car);
    }

    public void removeCar(String licensePlate) {
        AbstractVehicle vehicle = serviceManager.getVehicles().values().stream()
                .filter(v -> licensePlate.equalsIgnoreCase(v.getLicensePlate()))
                .findFirst()
                .orElse(null);
        if (vehicle == null) {
            throw new VehicleNotFoundException(licensePlate);
        }
        serviceManager.getVehicles().remove(vehicle.getId());
        // Also remove associated requests from ServiceManager's requests map
        serviceManager.getServiceRequests().values().removeIf(req -> req.getVehicle().equals(vehicle));
        log.info("Removed car: {}", vehicle);
    }

    public ServiceRequest createServiceRequest(Customer customer,
                                               String licensePlate,
                                               LocalDate date,
                                               String problemDesc) {
        if (customer == null) {
            throw new InvalidServiceRequestException("customer is null");
        }
        if (date == null || date.isAfter(LocalDate.now())) {
            throw new InvalidServiceRequestException("invalid date: " + date);
        }
        if (problemDesc == null || problemDesc.isBlank()) {
            throw new InvalidServiceRequestException("empty problem description");
        }

        AbstractVehicle vehicle = serviceManager.getVehicles().values().stream()
                .filter(v -> licensePlate.equalsIgnoreCase(v.getLicensePlate()))
                .findFirst()
                .orElseThrow(() -> new VehicleNotFoundException(licensePlate));

        ServiceRequest req = serviceManager.createServiceRequest(customer, vehicle, problemDesc);
        log.info("Created service request {} for {}", req.getId(), licensePlate);
        return req;
    }

    public List<ServiceRequest> listServiceRequests(String licensePlate) {
        AbstractVehicle vehicle = serviceManager.getVehicles().values().stream()
                .filter(v -> licensePlate.equalsIgnoreCase(v.getLicensePlate()))
                .findFirst()
                .orElseThrow(() -> new VehicleNotFoundException(licensePlate));

        return serviceManager.getServiceRequests().values().stream()
                .filter(req -> req.getVehicle().equals(vehicle))
                .toList();
    }

    public void updateServiceRequestStatus(String requestId,
                                           ServiceStatus newStatus) {
        ServiceRequest req = serviceManager.getServiceRequests().get(requestId);
        if (req == null) {
            throw new ServiceRequestNotFoundException(requestId);
        }
        serviceManager.updateServiceRequestStatus(requestId, newStatus);
        log.info("Request {} status updated to {}", requestId, newStatus);
    }

    public void removeServiceRequest(String requestId) {
        ServiceRequest req = serviceManager.getServiceRequests().remove(requestId);
        if (req == null) {
            throw new ServiceRequestNotFoundException(requestId);
        }
        req.getVehicle().removeServiceRequest(req);
        log.info("Removed service request {}", requestId);
    }

    public Collection<AbstractVehicle> listAllCars() {
        return serviceManager.getVehicles().values();
    }
}
