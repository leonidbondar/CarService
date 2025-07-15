package com.carserviceapp.factory;

import com.carserviceapp.model.AbstractVehicle;
import com.carserviceapp.model.Car;
import com.carserviceapp.model.VehicleMake;

/**
 * Concrete factory for creating Car objects.
 */
public class CarFactory implements VehicleFactory {
    @Override
    public AbstractVehicle createVehicle(VehicleMake make, String model, int year, String licensePlate, String vin) {
        return new Car(make, model, year, licensePlate, vin);
    }
}