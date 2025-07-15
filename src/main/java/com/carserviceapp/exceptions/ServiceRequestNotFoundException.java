package com.carserviceapp.exceptions;

public class ServiceRequestNotFoundException extends RuntimeException {
    public ServiceRequestNotFoundException(String requestId) {
        super("No service request found with ID '" + requestId + "'.");
    }
}
