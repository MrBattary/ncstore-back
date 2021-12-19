package com.netcracker.ncstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application entry point
 * <p>
 * Suppress because Main class has one 'public static void main' method,
 * so PMD decides its utility class, checks private construction and final modifier and flags the error.
 */
@SuppressWarnings("HideUtilityClassConstructor")
@SpringBootApplication
public class Main {
    /**
     * Main function for app starts
     *
     * @param args - Console arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
