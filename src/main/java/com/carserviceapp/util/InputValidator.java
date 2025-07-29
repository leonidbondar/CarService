package com.carserviceapp.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Utility class for robust user input validation.
 */
public class InputValidator {
    private static final InputValidator INSTANCE = new InputValidator();
    private static final Logger logger = LogManager.getLogger(InputValidator.class);
    private static final Scanner scanner = new Scanner(System.in);

    private static final Pattern POSITIVE_INTEGER_PATTERN = Pattern.compile("^\\d+$");
    private static final Pattern DECIMAL_PATTERN = Pattern.compile("^-?\\d+(\\.\\d+)?$");
    private static final Pattern NON_EMPTY_STRING_PATTERN = Pattern.compile("^\\s*\\S+.*$");

    private InputValidator() {}

    public static InputValidator getInstance() {
        return INSTANCE;
    }

    /**
     * Gets a non-empty string input from the user.
     *
     * @param prompt The message to display to the user (logged, not printed).
     * @return The validated string input.
     */
    public String getStringInput(String prompt) {
        String input;
        while (true) {
            logger.info(prompt);
            input = scanner.nextLine();
            if (NON_EMPTY_STRING_PATTERN.matcher(input).matches()) {
                return input.trim();
            } else {
                logger.warn("Input cannot be empty. Please try again.");
            }
        }
    }

    /**
     * Gets a positive integer input from the user without throwing exceptions.
     * Uses regex for validation.
     *
     * @param prompt The message to display to the user (logged, not printed).
     * @return The validated positive integer input.
     */
    public int getPositiveIntegerInput(String prompt) {
        String input;
        while (true) {
            logger.info(prompt);
            input = scanner.nextLine();
            if (POSITIVE_INTEGER_PATTERN.matcher(input).matches()) {
                try {
                    int value = Integer.parseInt(input);
                    if (value >= 0) {
                        return value;
                    } else {
                        logger.warn("Input must be a non-negative integer. Please try again.");
                    }
                } catch (NumberFormatException e) {
                    logger.error("Invalid numeric input: {}", input, e);
                }
            } else {
                logger.warn("Invalid input. Please enter a valid non-negative whole number.");
            }
        }
    }

    /**
     * Gets a decimal number input from the user without throwing exceptions.
     * Uses regex for validation.
     *
     * @param prompt The message to display to the user (logged, not printed).
     * @return The validated decimal input.
     */
    public double getDecimalInput(String prompt) {
        String input;
        while (true) {
            logger.info(prompt);
            input = scanner.nextLine();
            if (DECIMAL_PATTERN.matcher(input).matches()) {
                try {
                    return Double.parseDouble(input);
                } catch (NumberFormatException e) {
                    logger.error("Invalid decimal input: {}", input, e);
                }
            } else {
                logger.warn("Invalid input. Please enter a valid number (e.g., 123, 12.34).");
            }
        }
    }

    /**
     * Gets a yes/no input from the user.
     *
     * @param prompt The message to display to the user (logged, not printed).
     * @return true if 'yes' or 'y', false if 'no' or 'n'.
     */
    public boolean getYesNoInput(String prompt) {
        String input;
        while (true) {
            logger.info("{} (yes/no):", prompt);
            input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("yes") || input.equals("y")) {
                return true;
            } else if (input.equals("no") || input.equals("n")) {
                return false;
            } else {
                logger.warn("Invalid input. Please enter 'yes' or 'no'.");
            }
        }
    }

    /**
     * Closes the scanner. Should be called when the application exits.
     */
    public static void closeScanner() {
        scanner.close();
        logger.info("Scanner closed.");
    }
}
