package com.carserviceapp.model;

import com.carserviceapp.interfaces.Identifiable;
import com.carserviceapp.util.UniqueIdGenerator;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Abstract base class for financial transactions.
 */
public abstract class AbstractTransaction implements Identifiable {
    protected final String transactionId;
    protected LocalDate transactionDate;
    protected double amount;

    public AbstractTransaction(String idPrefix, LocalDate transactionDate, double amount) {
        this.transactionId = UniqueIdGenerator.generateId(idPrefix);
        this.transactionDate = transactionDate;
        this.amount = amount;
    }

    @Override
    public String getId() {
        return transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction ID: " + transactionId + ", Date: " + transactionDate + ", Amount: $" + String.format("%.2f", amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractTransaction that = (AbstractTransaction) o;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }
}