package com.carserviceapp.interfaces;

@FunctionalInterface
public interface StringFormatter<T> {
    String format(T value);
} 