package com.carserviceapp.model;

import com.carserviceapp.interfaces.Displayable;
import com.carserviceapp.interfaces.Identifiable;
import com.carserviceapp.util.UniqueIdGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Abstract base class for vehicles serviced by the application.
 */
public abstract class AbstractVehicle implements Identifiable, Displayable {
    protected final String vehicleId;

    public String getVehicleId() {
        return vehicleId;
    }

    public VehicleMake getMake() {
        return make;
    }

    public void setMake(VehicleMake make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    protected VehicleMake make;
    protected String model;
    protected int year;
    protected String licensePlate;
    protected String vin;

    // ← New: shared service-request history
    private final List<ServiceRequest> serviceHistory = new ArrayList<>();

    public AbstractVehicle(VehicleMake make,
                           String model,
                           int year,
                           String licensePlate,
                           String vin) {
        this.vehicleId = UniqueIdGenerator.generateId("VEH");
        this.make = make;
        this.model = model;
        this.year = year;
        this.licensePlate = licensePlate;
        this.vin = vin;
    }

    @Override
    public String getId() {
        return vehicleId;
    }

    // existing getters/setters omitted for brevity…

    @Override
    public String getDisplayInfo() {
        return "Vehicle ID: " + vehicleId
                + ", Make: " + make
                + ", Model: " + model
                + ", Year: " + year
                + ", Plate: " + licensePlate
                + ", VIN: " + vin;
    }

    @Override
    public String toString() {
        return getDisplayInfo();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractVehicle)) return false;
        AbstractVehicle that = (AbstractVehicle) o;
        return Objects.equals(vehicleId, that.vehicleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleId);
    }

    // === NEW: service-request history API ===

    /**
     * Called by CarService when a new request is created.
     */
    public void addServiceRequest(ServiceRequest req) {
        serviceHistory.add(req);
    }

    /**
     * Called by CarService when deleting a request.
     */
    public boolean removeServiceRequest(ServiceRequest req) {
        return serviceHistory.remove(req);
    }

    /**
     * For reporting or UI.
     */
    public List<ServiceRequest> getServiceHistory() {
        return Collections.unmodifiableList(serviceHistory);
    }
}
