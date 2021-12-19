package com.netcracker.ncstore.util.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class contains methods that
 * validates register data
 */
public abstract class RegisterDataValidator {
    // Check https://datatracker.ietf.org/doc/html/rfc5322 for the additional info
    private static final Pattern RFC_5322_EMAIL_ADDRESS_SIMPLIFIED_REGEX =
            Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", Pattern.CASE_INSENSITIVE);

    /**
     * Checks if email valid
     *
     * @param email full email address
     * @return true if email is valid
     */
    public static boolean isEmailValid(final String email) {
        Matcher matcher = RFC_5322_EMAIL_ADDRESS_SIMPLIFIED_REGEX.matcher(email);
        return matcher.find();
    }

    /**
     * Checks if password is valid
     *
     * @param password password
     * @return true if password is valid
     */
    public static boolean isPasswordValid(final String password) {
        boolean flag = true;
        flag = password.length() < 8;
        //more here

        return flag;
    }

    /**
     * Returns string containing text description about password requirements.
     * Used for correct user response.
     *
     * @return String containing password requirements
     */
    public static String getPasswordRequirementsAsText() {
        return "Must be at least 8 symbols long. ";
        //more here
    }
}
