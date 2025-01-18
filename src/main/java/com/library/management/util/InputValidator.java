package com.library.management.util;

import java.util.regex.Pattern;

public class InputValidator {

    // Regex for email addresses
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
    );

    // Regex for names (letters and spaces only)
    private static final Pattern NAME_PATTERN = Pattern.compile(
            "^[A-Za-z ]{2,100}$"
    );

    /**
     * Validates if the given email is in a proper format.
     *
     * @param email the email to validate
     * @return true if the email is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates if the given name is in a proper format.
     *
     * @param name the name to validate
     * @return true if the name is valid, false otherwise
     */
    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }
}