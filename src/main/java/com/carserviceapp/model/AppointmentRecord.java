package com.carserviceapp.model;

import com.carserviceapp.annotations.Auditable;
import com.carserviceapp.annotations.BusinessRule;
import com.carserviceapp.annotations.BusinessValidation;
import com.carserviceapp.interfaces.Displayable;
import com.carserviceapp.interfaces.Identifiable;
import com.carserviceapp.util.UniqueIdGenerator;

import java.time.LocalDateTime;

/**
 * Represents a scheduled appointment for a service request as a record.
 */
@Auditable(level = Auditable.AuditLevel.STANDARD, auditPrefix = "APPOINTMENT")
public record AppointmentRecord(
        String appointmentId,
        @BusinessValidation(type = BusinessValidation.ValidationType.CUSTOMER_VALIDATION,
                required = true)
        Customer customer,
        @BusinessValidation(type = BusinessValidation.ValidationType.VEHICLE_VALIDATION,
                required = true)
        AbstractVehicle vehicle,
        ServiceRequest serviceRequest, // Optional, can be linked to an existing request
        @BusinessValidation(type = BusinessValidation.ValidationType.TIME_VALIDATION,
                required = true)
        LocalDateTime scheduledTime,
        @BusinessValidation(type = BusinessValidation.ValidationType.STANDARD,
                required = true, minLength = 3, maxLength = 100)
        String purpose // e.g., "Diagnostic", "Repair Drop-off", "Pickup"
) implements Identifiable, Displayable {

    /**
     * Constructor with auto-generated ID
     */
    public AppointmentRecord(Customer customer, AbstractVehicle vehicle, LocalDateTime scheduledTime, String purpose) {
        this(UniqueIdGenerator.generateId("APPT"), customer, vehicle, null, scheduledTime, purpose);
    }

    /**
     * Constructor with service request
     */
    public AppointmentRecord(Customer customer, AbstractVehicle vehicle, ServiceRequest serviceRequest, LocalDateTime scheduledTime, String purpose) {
        this(UniqueIdGenerator.generateId("APPT"), customer, vehicle, serviceRequest, scheduledTime, purpose);
    }

    @Override
    public String getId() {
        return appointmentId;
    }

    @Override
    @BusinessRule(type = BusinessRule.RuleType.TIME_ESTIMATION, priority = 2)
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
} 