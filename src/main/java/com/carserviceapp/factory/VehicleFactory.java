package com.carserviceapp.factory;

import com.carserviceapp.model.AbstractVehicle;
import com.carserviceapp.model.VehicleMake;

/**
 * Abstract factory for creating different types of vehicles.
 */
public interface VehicleFactory {
    AbstractVehicle createVehicle(VehicleMake make, String model, int year, String licensePlate, String vin);
}