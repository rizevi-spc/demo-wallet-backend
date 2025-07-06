package com.example.demo.enumeration;

import java.util.function.Predicate;

/**
 * Enumeration representing different types of opposite parties.
 * This enum provides a method to validate the format of the destination ID
 */
public enum OppositePartyType {
    IBAN(id -> id != null && id.matches("[A-Z]{2}\\d{2}[A-Z0-9]{1,30}")),
    WALLET(id -> id != null && id.matches("\\d{10,20}"));

    private final Predicate<String> validator;

    OppositePartyType(Predicate<String> validator) {
        this.validator = validator;
    }

    public boolean isValidDestination(String destinationId) {
        return validator.test(destinationId);
    }
}