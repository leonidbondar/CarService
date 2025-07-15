package com.carserviceapp.exceptions;

public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(String licensePlate) {
        super("No vehicle found with license plate '" + licensePlate + "'.");
    }
}