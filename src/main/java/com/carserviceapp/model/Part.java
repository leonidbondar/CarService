package com.carserviceapp.model;

import com.carserviceapp.interfaces.CostCalculable;
import com.carserviceapp.interfaces.Displayable;
import com.carserviceapp.interfaces.Identifiable;
import com.carserviceapp.util.UniqueIdGenerator;

import java.util.Objects;

/**
 * Represents a physical part used in repairs.
 */
public class Part implements Identifiable, Displayable, CostCalculable {
    private final String partId;
    private String name;
    private double unitPrice;
    private int stockQuantity;

    public Part(String name, double unitPrice, int stockQuantity) {
        this.partId = UniqueIdGenerator.generateId("PART");
        this.name = name;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
    }

    @Override
    public String getId() {
        return partId;
    }

    public String getPartId() {
        return partId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    /**
     * Reduces the stock quantity by a given amount.
     *
     * @param quantity The amount to reduce.
     * @return true if stock was successfully reduced, false otherwise.
     */
    public boolean reduceStock(int quantity) {
        if (this.stockQuantity >= quantity) {
            this.stockQuantity -= quantity;
            return true;
        }
        return false;
    }

    @Override
    public double calculateCost() {
        return unitPrice; // Cost of one unit
    }

    @Override
    public String getDisplayInfo() {
        return "Part ID: " + partId + ", Name: " + name + ", Unit Price: $" + String.format("%.2f", unitPrice) + ", Stock: " + stockQuantity;
    }

    @Override
    public String toString() {
        return getDisplayInfo();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Part part = (Part) o;
        return Objects.equals(partId, part.partId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partId);
    }
}