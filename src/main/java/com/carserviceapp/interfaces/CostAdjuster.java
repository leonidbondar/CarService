package com.carserviceapp.interfaces;

@FunctionalInterface
public interface CostAdjuster {
    double adjust(double originalCost);
} 