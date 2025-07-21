package com.carserviceapp.interfaces;

@FunctionalInterface
public interface OperationFilter {
    boolean filter(com.carserviceapp.model.AbstractServiceOperation operation);
} 