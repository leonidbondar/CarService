package com.carserviceapp.model;

import com.carserviceapp.interfaces.Displayable;
import com.carserviceapp.interfaces.Identifiable;
import com.carserviceapp.util.UniqueIdGenerator;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents an insurance claim filed for a service request.
 */
public class InsuranceClaim implements Identifiable, Displayable {
    private final String claimId;
    private ServiceRequest serviceRequest; // Linked to a service request
    private String insuranceCompany;
    private String policyNumber;
    private double claimAmount;
    private LocalDate claimDate;
    private Status status;

    public enum Status {
        SUBMITTED, PENDING_APPROVAL, APPROVED, DENIED, PAID
    }

    public InsuranceClaim(ServiceRequest serviceRequest, String insuranceCompany, String policyNumber, double claimAmount) {
        this.claimId = UniqueIdGenerator.getInstance().generateId("CLAIM");
        this.serviceRequest = serviceRequest;
        this.insuranceCompany = insuranceCompany;
        this.policyNumber = policyNumber;
        this.claimAmount = claimAmount;
        this.claimDate = LocalDate.now();
        this.status = Status.SUBMITTED;
    }

    @Override
    public String getId() {
        return claimId;
    }

    public ServiceRequest getServiceRequest() {
        return serviceRequest;
    }

    public void setServiceRequest(ServiceRequest serviceRequest) {
        this.serviceRequest = serviceRequest;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public double getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(double claimAmount) {
        this.claimAmount = claimAmount;
    }

    public LocalDate getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(LocalDate claimDate) {
        this.claimDate = claimDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String getDisplayInfo() {
        return "Claim ID: " + claimId +
                ", Service Request ID: " + serviceRequest.getId() +
                ", Company: " + insuranceCompany +
                ", Policy: " + policyNumber +
                ", Amount: $" + String.format("%.2f", claimAmount) +
                ", Status: " + status +
                ", Date: " + claimDate;
    }

    @Override
    public String toString() {
        return getDisplayInfo();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsuranceClaim that = (InsuranceClaim) o;
        return Objects.equals(claimId, that.claimId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(claimId);
    }
}