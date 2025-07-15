package com.carserviceapp.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Utility class for generating unique IDs.
 */
public class UniqueIdGenerator {
    private static final AtomicLong counter = new AtomicLong(System.currentTimeMillis());

    /**
     * Generates a unique string ID with a given prefix.
     *
     * @param prefix The prefix for the ID (e.g., "CUST", "VEH").
     * @return A unique string ID.
     */
    public static String generateId(String prefix) {
        return prefix + "-" + counter.getAndIncrement();
    }
}