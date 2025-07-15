package com.carserviceapp.model;

import java.util.Objects;

/**
 * Represents a car, extending AbstractVehicle.
 */
public class Car extends AbstractVehicle {
    private int numDoors;
    private FuelType fuelType;

    public Car(VehicleMake make,
               String model,
               int year,
               String licensePlate,
               String vin) {
        super(make, model, year, licensePlate, vin);
        this.numDoors = 4;
        this.fuelType = FuelType.GASOLINE; // Default to the enum constant
    }

    public int getNumDoors() {
        return numDoors;
    }

    public void setNumDoors(int numDoors) {
        this.numDoors = numDoors;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    @Override
    public String getDisplayInfo() {
        return super.getDisplayInfo()
                + ", Type: Car"
                + ", Doors: " + numDoors
                + ", Fuel: " + fuelType
                + ", Requests: " + getServiceHistory().size();
    }

    @Override
    public String toString() {
        return getDisplayInfo();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}
