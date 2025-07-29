package com.carserviceapp.model;

import com.carserviceapp.interfaces.Displayable;
import com.carserviceapp.interfaces.Identifiable;
import com.carserviceapp.util.UniqueIdGenerator;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a scheduled appointment for a service request.
 */
public class Appointment implements Identifiable, Displayable {
    private final String appointmentId;
    private Customer customer;
    private AbstractVehicle vehicle; // Vehicle for the appointment
    private ServiceRequest serviceRequest; // Optional, can be linked to an existing request
    private LocalDateTime scheduledTime;
    private String purpose; // e.g., "Diagnostic", "Repair Drop-off", "Pickup"

    public Appointment(Customer customer, AbstractVehicle vehicle, LocalDateTime scheduledTime, String purpose) {
        this.appointmentId = UniqueIdGenerator.getInstance().generateId("APPT");
        this.customer = customer;
        this.vehicle = vehicle;
        this.scheduledTime = scheduledTime;
        this.purpose = purpose;
    }

    public Appointment(Customer customer, AbstractVehicle vehicle, ServiceRequest serviceRequest, LocalDateTime scheduledTime, String purpose) {
        this(customer, vehicle, scheduledTime, purpose);
        this.serviceRequest = serviceRequest;
    }

    @Override
    public String getId() {
        return appointmentId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public AbstractVehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(AbstractVehicle vehicle) {
        this.vehicle = vehicle;
    }

    public ServiceRequest getServiceRequest() {
        return serviceRequest;
    }

    public void setServiceRequest(ServiceRequest serviceRequest) {
        this.serviceRequest = serviceRequest;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    @Override
    public String getDisplayInfo() {
        return "Appointment ID: " + appointmentId +
                "\n  Customer: " + customer.getDisplayInfo() +
                "\n  Vehicle: " + vehicle.getDisplayInfo() +
                "\n  Scheduled Time: " + scheduledTime.toLocalDate() + " at " + scheduledTime.toLocalTime() +
                "\n  Purpose: " + purpose +
                (serviceRequest != null ? "\n  Linked Service Request ID: " + serviceRequest.getId() : "");
    }

    @Override
    public String toString() {
        return getDisplayInfo();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(appointmentId, that.appointmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appointmentId);
    }
}