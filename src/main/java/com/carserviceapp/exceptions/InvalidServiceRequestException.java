package com.carserviceapp.exceptions;

public class InvalidServiceRequestException extends RuntimeException {
    public InvalidServiceRequestException(String msg) {
        super("Invalid service request: " + msg);
    }
}