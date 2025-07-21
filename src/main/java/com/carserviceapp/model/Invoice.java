package com.carserviceapp.model;

import com.carserviceapp.interfaces.CostAdjuster;
import com.carserviceapp.interfaces.Displayable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents an invoice for a completed service request.
 */
public class Invoice extends AbstractTransaction implements Displayable {
    private static final Logger logger = LogManager.getLogger(Invoice.class);

    private ServiceRequest serviceRequest;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private boolean isPaid;

    public Invoice(ServiceRequest serviceRequest) {
        super("INV", LocalDate.now(), serviceRequest.calculateCost());
        this.serviceRequest = serviceRequest;
        this.issueDate = LocalDate.now();
        this.dueDate = issueDate.plusDays(30); // Default 30 days due
        this.isPaid = false;
        logger.info("Invoice created: {} for service request {} with total amount ${}", getTransactionId(), serviceRequest.getId(), getAmount());
    }

    public ServiceRequest getServiceRequest() {
        return serviceRequest;
    }

    public void setServiceRequest(ServiceRequest serviceRequest) {
        this.serviceRequest = serviceRequest;
        this.setAmount(serviceRequest.calculateCost()); // Update amount if service request changes
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String generateBill() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- INVOICE ---").append("\n");
        sb.append("Invoice ID: ").append(getTransactionId()).append("\n");
        sb.append("Issue Date: ").append(issueDate).append("\n");
        sb.append("Due Date: ").append(dueDate).append("\n");
        sb.append("Status: ").append(isPaid ? "PAID" : "UNPAID").append("\n");
        sb.append("---------------------\n");
        sb.append("Customer: ").append(serviceRequest.getCustomer().getDisplayInfo()).append("\n");
        sb.append("Vehicle: ").append(serviceRequest.getVehicle().getDisplayInfo()).append("\n");
        sb.append("Problem Description: ").append(serviceRequest.getProblemDescription()).append("\n");
        sb.append("---------------------\n");
        sb.append("Service Operations:\n");
        com.carserviceapp.interfaces.StringFormatter<com.carserviceapp.model.AbstractServiceOperation> formatter =
                op -> "  - " + op.getDisplayInfo() + "\n";
        serviceRequest.getOperations().stream().map(formatter::format).forEach(sb::append);
        sb.append("---------------------\n");
        sb.append("TOTAL AMOUNT DUE: $").append(String.format("%.2f", getAmount())).append("\n");
        sb.append("---------------------\n");
        return sb.toString();
    }

    // Method to simulate processing the invoice after payment
    public void processTransaction() {
        if (isPaid) {
            logger.info("Invoice {} has been processed and marked as paid.", getTransactionId());
            // Additional logic like sending confirmation, updating financial records, etc.
        } else {
            logger.warn("Invoice {} is still unpaid. Cannot fully process yet.", getTransactionId());
        }
    }

    /**
     * Adjusts the invoice amount using the provided CostAdjuster.
     */
    public void applyCostAdjuster(CostAdjuster adjuster) {
        double adjusted = adjuster.adjust(getAmount());
        setAmount(adjusted);
    }

    @Override
    public String getDisplayInfo() {
        return "Invoice ID: " + getTransactionId() + ", Request ID: " + serviceRequest.getId() + ", Amount: $" + String.format("%.2f", getAmount()) + ", Status: " + (isPaid ? "PAID" : "UNPAID");
    }

    @Override
    public String toString() {
        return getDisplayInfo();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(getTransactionId(), invoice.getTransactionId());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}