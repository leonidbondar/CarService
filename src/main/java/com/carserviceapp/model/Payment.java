package com.carserviceapp.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a payment made towards an invoice.
 */
public class Payment extends AbstractTransaction {
    private Invoice invoice;
    private PaymentMethod paymentMethod;

    public enum PaymentMethod {
        CASH, CREDIT_CARD, BANK_TRANSFER
    }

    public Payment(Invoice invoice, double amount, LocalDate paymentDate, PaymentMethod method) {
        super("PAY", paymentDate, amount);
        this.invoice = invoice;
        this.paymentMethod = method;
        this.invoice.setPaid(true); // Mark the associated invoice as paid
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return super.toString() + ", Method: " + paymentMethod + ", For Invoice ID: " + invoice.getTransactionId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Payment payment = (Payment) o;
        return Objects.equals(getTransactionId(), payment.getTransactionId());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}