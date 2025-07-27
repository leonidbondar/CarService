package com.carserviceapp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to mark methods that implement business rules.
 * This annotation indicates that a method contains specific business
 * logic that should be validated and potentially logged.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BusinessRule {
    /**
     * The type of business rule being implemented
     */
    RuleType type();

    /**
     * Priority of this business rule (1 = highest, 5 = lowest)
     */
    int priority() default 3;

    /**
     * Whether this rule is critical for business operations
     */
    boolean critical() default false;

    /**
     * Custom error message for rule violations
     */
    String errorMessage() default "";

    /**
     * Whether to log rule execution
     */
    boolean logExecution() default true;

    /**
     * Business rule types for different domains
     */
    enum RuleType {
        COST_CALCULATION,      // Cost calculation rules
        INVENTORY_MANAGEMENT,  // Inventory control rules
        CUSTOMER_VALIDATION,   // Customer data validation
        VEHICLE_VALIDATION,    // Vehicle data validation
        SERVICE_VALIDATION,    // Service request validation
        PAYMENT_VALIDATION,    // Payment processing rules
        TIME_ESTIMATION,       // Time estimation rules
        QUALITY_ASSURANCE,     // Quality control rules
        COMPLIANCE_CHECK,      // Regulatory compliance
        CUSTOM                 // Custom business rule
    }
} 