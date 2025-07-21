package com.carserviceapp.model;

import com.carserviceapp.interfaces.*;
import com.carserviceapp.util.UniqueIdGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a service request submitted by a customer for a vehicle.
 * This class uses a list of AbstractServiceOperation to represent the work to be done.
 */
public class ServiceRequest implements Identifiable, Displayable, CostCalculable, TimeEstimable {
    private final String requestId;
    private Customer customer;
    private AbstractVehicle vehicle;
    private LocalDate requestDate;
    private String problemDescription;
    private ServiceStatus status;
    private List<AbstractServiceOperation> operations;
    private double estimatedTotalCost;
    private double estimatedTotalTime;

    public ServiceRequest(Customer customer, AbstractVehicle vehicle, LocalDate requestDate, String problemDescription) {
        this.requestId = UniqueIdGenerator.generateId("REQ");
        this.customer = customer;
        this.vehicle = vehicle;
        this.requestDate = requestDate;
        this.problemDescription = problemDescription;
        this.status = ServiceStatus.PENDING;
        this.operations = new ArrayList<>();
        this.estimatedTotalCost = 0.0;
        this.estimatedTotalTime = 0.0;
    }

    @Override
    public String getId() {
        return requestId;
    }

    public String getRequestId() {
        return requestId;
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

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public ServiceStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceStatus status) {
        this.status = status;
    }

    public List<AbstractServiceOperation> getOperations() {
        return new ArrayList<>(operations); // Return a copy to prevent external modification
    }

    /**
     * Returns a list of operations filtered by the given OperationFilter.
     */
    public List<AbstractServiceOperation> getFilteredOperations(OperationFilter filter) {
        return operations.stream().filter(filter::filter).toList();
    }

    public void addOperation(AbstractServiceOperation operation) {
        this.operations.add(operation);
        recalculateEstimates();
    }

    public void removeOperation(AbstractServiceOperation operation) {
        if (this.operations.remove(operation)) {
            recalculateEstimates();
        }
    }

    // Recalculate estimates whenever operations change
    private void recalculateEstimates() {
        this.estimatedTotalCost = operations.stream()
                .mapToDouble(AbstractServiceOperation::calculateCost)
                .sum();
        this.estimatedTotalTime = operations.stream()
                .mapToDouble(AbstractServiceOperation::estimateTime)
                .sum();
    }

    @Override
    public double calculateCost() {
        return estimatedTotalCost;
    }

    @Override
    public double estimateTime() {
        return estimatedTotalTime;
    }

    @Override
    public String getDisplayInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Service Request ID: ").append(requestId).append("\n");
        sb.append("  Customer: ").append(customer.getDisplayInfo()).append("\n");
        sb.append("  Vehicle: ").append(vehicle.getDisplayInfo()).append("\n");
        sb.append("  Request Date: ").append(requestDate).append("\n");
        sb.append("  Problem: ").append(problemDescription).append("\n");
        sb.append("  Status: ").append(status).append("\n");
        sb.append("  Estimated Total Cost: $").append(String.format("%.2f", estimatedTotalCost)).append("\n");
        sb.append("  Estimated Total Time: ").append(String.format("%.1f", estimatedTotalTime)).append(" hours\n");
        if (!operations.isEmpty()) {
            sb.append("  Operations:\n");
            StringFormatter<AbstractServiceOperation> formatter =
                    op -> "    - " + op.getDisplayInfo() + "\n";
            operations.stream().map(formatter::format).forEach(sb::append);
        } else {
            sb.append("  No operations added yet.\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getDisplayInfo();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceRequest that = (ServiceRequest) o;
        return Objects.equals(requestId, that.requestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId);
    }
}