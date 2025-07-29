package com.carserviceapp.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Utility class for generating unique IDs.
 */
public class UniqueIdGenerator {
    private static final UniqueIdGenerator INSTANCE = new UniqueIdGenerator();
    private static final AtomicLong counter = new AtomicLong(System.currentTimeMillis());
    private UniqueIdGenerator() {}
    public static UniqueIdGenerator getInstance() {
        return INSTANCE;
    }
    /**
     * Generates a unique string ID with a given prefix.
     *
     * @param prefix The prefix for the ID (e.g., "CUST", "VEH").
     * @return A unique string ID.
     */
    public String generateId(String prefix) {
        return prefix + "-" + counter.getAndIncrement();
    }
}