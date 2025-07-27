package com.carserviceapp.model;

import com.carserviceapp.annotations.Auditable;
import com.carserviceapp.annotations.BusinessRule;
import com.carserviceapp.annotations.BusinessValidation;
import com.carserviceapp.interfaces.Identifiable;
import com.carserviceapp.util.UniqueIdGenerator;

import java.time.LocalDate;

/**
 * Represents a payment made towards an invoice as a record.
 */
@Auditable(level = Auditable.AuditLevel.COMPLIANCE, auditPrefix = "PAYMENT")
public record PaymentRecord(
        String transactionId,
        @BusinessValidation(type = BusinessValidation.ValidationType.COST_VALIDATION,
                required = true, minValue = 0.01, maxValue = 1000000.0)
        double amount,
        @BusinessValidation(type = BusinessValidation.ValidationType.STANDARD,
                required = true)
        LocalDate transactionDate,
        @BusinessValidation(type = BusinessValidation.ValidationType.STANDARD,
                required = true)
        Invoice invoice,
        @BusinessValidation(type = BusinessValidation.ValidationType.STANDARD,
                required = true)
        PaymentMethod paymentMethod
) implements Identifiable {

    public enum PaymentMethod {
        CASH, CREDIT_CARD, BANK_TRANSFER
    }

    /**
     * Constructor with auto-generated ID
     */
    public PaymentRecord(Invoice invoice, double amount, LocalDate paymentDate, PaymentMethod method) {
        this(UniqueIdGenerator.generateId("PAY"), amount, paymentDate, invoice, method);
    }

    @Override
    public String getId() {
        return transactionId;
    }

    /**
     * Gets the transaction ID for backward compatibility
     */
    public String getTransactionId() {
        return transactionId;
    }

    @Override
    @BusinessRule(type = BusinessRule.RuleType.PAYMENT_VALIDATION, priority = 1, critical = true)
    public String toString() {
        return "Transaction ID: " + transactionId +
                ", Date: " + transactionDate +
                ", Amount: $" + String.format("%.2f", amount) +
                ", Method: " + paymentMethod +
                ", For Invoice ID: " + invoice.getTransactionId();
    }
} 