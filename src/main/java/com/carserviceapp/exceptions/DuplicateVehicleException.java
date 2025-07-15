package com.carserviceapp.exceptions;

public class DuplicateVehicleException extends RuntimeException {
    public DuplicateVehicleException(String licensePlate) {
        super("Vehicle with license plate '" + licensePlate + "' already exists.");
    }
}