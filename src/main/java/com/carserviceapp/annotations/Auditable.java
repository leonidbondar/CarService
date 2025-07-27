package com.carserviceapp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to mark entities that should be audited.
 * This annotation indicates that an entity should be tracked
 * for business audit purposes including creation, modification,
 * and deletion events.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Auditable {
    /**
     * The audit level for this entity
     */
    AuditLevel level() default AuditLevel.STANDARD;

    /**
     * Whether to track field-level changes
     */
    boolean trackFieldChanges() default true;

    /**
     * Whether to track creation events
     */
    boolean trackCreation() default true;

    /**
     * Whether to track modification events
     */
    boolean trackModification() default true;

    /**
     * Whether to track deletion events
     */
    boolean trackDeletion() default true;

    /**
     * Custom audit message prefix
     */
    String auditPrefix() default "";

    /**
     * Audit levels for different business requirements
     */
    enum AuditLevel {
        NONE,           // No auditing
        BASIC,          // Basic creation/deletion tracking
        STANDARD,       // Standard audit with modifications
        DETAILED,       // Detailed audit with field changes
        COMPLIANCE      // Full compliance audit trail
    }
} 