package com.carserviceapp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to mark fields that require business validation.
 * This annotation can be used to identify fields that need special
 * business logic validation beyond standard Java validation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface BusinessValidation {
    /**
     * The type of business validation to apply
     */
    ValidationType type() default ValidationType.STANDARD;

    /**
     * Custom validation message
     */
    String message() default "";

    /**
     * Whether this field is required for business operations
     */
    boolean required() default true;

    /**
     * Minimum value for numeric fields
     */
    double minValue() default Double.MIN_VALUE;

    /**
     * Maximum value for numeric fields
     */
    double maxValue() default Double.MAX_VALUE;

    /**
     * Minimum length for string fields
     */
    int minLength() default 0;

    /**
     * Maximum length for string fields
     */
    int maxLength() default Integer.MAX_VALUE;

    /**
     * Validation types for different business rules
     */
    enum ValidationType {
        STANDARD,
        COST_VALIDATION,
        TIME_VALIDATION,
        INVENTORY_VALIDATION,
        CUSTOMER_VALIDATION,
        VEHICLE_VALIDATION
    }
} 