package com.netcracker.ncstore.util.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class contains methods that
 * validates email standard
 */
public abstract class EmailValidator {
    // Check https://datatracker.ietf.org/doc/html/rfc5322 for the additional info
    private static final Pattern RFC_5322_EMAIL_ADDRESS_SIMPLIFIED_REGEX =
            Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", Pattern.CASE_INSENSITIVE);

    /**
     * Checks if email valid
     * @param email - full email address
     * @return - true: may exist, false: otherwise
     */
    public static boolean isEmailValid(final String email) {
        Matcher matcher = RFC_5322_EMAIL_ADDRESS_SIMPLIFIED_REGEX.matcher(email);
        return matcher.find();
    }
}
