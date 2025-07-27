package com.carserviceapp.util;

import com.carserviceapp.annotations.Auditable;
import com.carserviceapp.annotations.BusinessRule;
import com.carserviceapp.annotations.BusinessValidation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for processing custom annotations with encapsulation.
 * This class provides methods to validate business rules, process
 * audit requirements, and handle business validation annotations.
 */
public class AnnotationProcessor {
    private static final Logger logger = LogManager.getLogger(AnnotationProcessor.class);

    /**
     * Validates an object based on its BusinessValidation annotations
     */
    public static ValidationResult validateObject(Object obj) {
        ValidationResult result = new ValidationResult();

        if (obj == null) {
            result.addError("Object cannot be null");
            return result;
        }

        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            BusinessValidation validation = field.getAnnotation(BusinessValidation.class);
            if (validation != null) {
                validateField(obj, field, validation, result);
            }
        }

        return result;
    }

    /**
     * Validates a specific field based on its BusinessValidation annotation
     */
    private static void validateField(Object obj, Field field, BusinessValidation validation, ValidationResult result) {
        try {
            field.setAccessible(true);
            Object value = field.get(obj);

            // Check if required field is null
            if (validation.required() && value == null) {
                result.addError(field.getName() + " is required but is null");
                return;
            }

            if (value != null) {
                // Validate string fields
                if (value instanceof String) {
                    validateStringField((String) value, field.getName(), validation, result);
                }
                // Validate numeric fields
                else if (value instanceof Number) {
                    validateNumericField((Number) value, field.getName(), validation, result);
                }
            }
        } catch (IllegalAccessException e) {
            result.addError("Cannot access field " + field.getName() + ": " + e.getMessage());
        }
    }

    /**
     * Validates string fields based on BusinessValidation annotation
     */
    private static void validateStringField(String value, String fieldName, BusinessValidation validation, ValidationResult result) {
        if (value.length() < validation.minLength()) {
            result.addError(fieldName + " must be at least " + validation.minLength() + " characters long");
        }

        if (value.length() > validation.maxLength()) {
            result.addError(fieldName + " must be no more than " + validation.maxLength() + " characters long");
        }
    }

    /**
     * Validates numeric fields based on BusinessValidation annotation
     */
    private static void validateNumericField(Number value, String fieldName, BusinessValidation validation, ValidationResult result) {
        double doubleValue = value.doubleValue();

        if (doubleValue < validation.minValue()) {
            result.addError(fieldName + " must be at least " + validation.minValue());
        }

        if (doubleValue > validation.maxValue()) {
            result.addError(fieldName + " must be no more than " + validation.maxValue());
        }
    }

    /**
     * Processes audit requirements for an object
     */
    public static void processAudit(Object obj, String action) {
        if (obj == null) return;

        Auditable auditable = obj.getClass().getAnnotation(Auditable.class);
        if (auditable == null) return;

        String auditMessage = buildAuditMessage(obj, action, auditable);
        logger.info(auditMessage);
    }

    /**
     * Builds an audit message based on the Auditable annotation
     */
    private static String buildAuditMessage(Object obj, String action, Auditable auditable) {
        StringBuilder message = new StringBuilder();

        if (!auditable.auditPrefix().isEmpty()) {
            message.append(auditable.auditPrefix()).append(": ");
        }

        message.append("[").append(auditable.level()).append("] ");
        message.append(action).append(" - ");
        message.append(obj.getClass().getSimpleName());
        message.append(" (ID: ").append(getObjectId(obj)).append(")");
        message.append(" at ").append(LocalDateTime.now());

        return message.toString();
    }

    /**
     * Gets the ID of an object for audit purposes
     */
    private static String getObjectId(Object obj) {
        try {
            Method getIdMethod = obj.getClass().getMethod("getId");
            Object id = getIdMethod.invoke(obj);
            return id != null ? id.toString() : "unknown";
        } catch (Exception e) {
            return "unknown";
        }
    }

    /**
     * Executes business rules for a method
     */
    public static BusinessRuleResult executeBusinessRule(Object obj, String methodName, Object... args) {
        try {
            Method method = obj.getClass().getMethod(methodName);
            BusinessRule businessRule = method.getAnnotation(BusinessRule.class);

            if (businessRule == null) {
                return new BusinessRuleResult(true, "No business rule annotation found");
            }

            if (businessRule.logExecution()) {
                logger.info("Executing business rule: " + businessRule.type() + " (Priority: " + businessRule.priority() + ")");
            }

            // Execute the method
            Object result = method.invoke(obj, args);

            return new BusinessRuleResult(true, "Business rule executed successfully", result);

        } catch (Exception e) {
            String errorMsg = "Failed to execute business rule: " + e.getMessage();
            logger.error(errorMsg, e);
            return new BusinessRuleResult(false, errorMsg);
        }
    }

    /**
     * Gets all business rules for a class
     */
    public static List<BusinessRuleInfo> getBusinessRules(Class<?> clazz) {
        List<BusinessRuleInfo> rules = new ArrayList<>();

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            BusinessRule businessRule = method.getAnnotation(BusinessRule.class);
            if (businessRule != null) {
                rules.add(new BusinessRuleInfo(method.getName(), businessRule));
            }
        }

        return rules;
    }

    /**
     * Result class for validation operations
     */
    public static class ValidationResult {
        private final List<String> errors = new ArrayList<>();
        private final List<String> warnings = new ArrayList<>();

        public void addError(String error) {
            errors.add(error);
        }

        public void addWarning(String warning) {
            warnings.add(warning);
        }

        public boolean isValid() {
            return errors.isEmpty();
        }

        public List<String> getErrors() {
            return new ArrayList<>(errors);
        }

        public List<String> getWarnings() {
            return new ArrayList<>(warnings);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (!errors.isEmpty()) {
                sb.append("Errors: ").append(errors);
            }
            if (!warnings.isEmpty()) {
                if (sb.length() > 0) sb.append("; ");
                sb.append("Warnings: ").append(warnings);
            }
            return sb.toString();
        }
    }

    /**
     * Result class for business rule execution
     */
    public static class BusinessRuleResult {
        private final boolean success;
        private final String message;
        private final Object result;

        public BusinessRuleResult(boolean success, String message) {
            this(success, message, null);
        }

        public BusinessRuleResult(boolean success, String message, Object result) {
            this.success = success;
            this.message = message;
            this.result = result;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public Object getResult() {
            return result;
        }
    }

    /**
     * Information class for business rules
     */
    public static class BusinessRuleInfo {
        private final String methodName;
        private final BusinessRule businessRule;

        public BusinessRuleInfo(String methodName, BusinessRule businessRule) {
            this.methodName = methodName;
            this.businessRule = businessRule;
        }

        public String getMethodName() {
            return methodName;
        }

        public BusinessRule getBusinessRule() {
            return businessRule;
        }

        @Override
        public String toString() {
            return methodName + " -> " + businessRule.type() + " (Priority: " + businessRule.priority() + ")";
        }
    }
} 