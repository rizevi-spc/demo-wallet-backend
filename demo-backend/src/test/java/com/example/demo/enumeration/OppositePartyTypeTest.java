package com.example.demo.enumeration;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OppositePartyTypeTest {

    @Test
    void testIsValidDestination_IBAN_Valid() {
        assertTrue(OppositePartyType.IBAN.isValidDestination("TR330006100519786457841326"));
        assertTrue(OppositePartyType.IBAN.isValidDestination("DE89370400440532013000"));
    }

    @Test
    void testIsValidDestination_IBAN_Invalid() {
        assertFalse(OppositePartyType.IBAN.isValidDestination(null));
        assertFalse(OppositePartyType.IBAN.isValidDestination("1234"));
        assertFalse(OppositePartyType.IBAN.isValidDestination("TR33$006100519786457841326"));
    }

    @Test
    void testIsValidDestination_WALLET_Valid() {
        assertTrue(OppositePartyType.WALLET.isValidDestination("1234567890"));
        assertTrue(OppositePartyType.WALLET.isValidDestination("12345678901234567890"));
    }

    @Test
    void testIsValidDestination_WALLET_Invalid() {
        assertFalse(OppositePartyType.WALLET.isValidDestination(null));
        assertFalse(OppositePartyType.WALLET.isValidDestination("abc123"));
        assertFalse(OppositePartyType.WALLET.isValidDestination("123456789")); // 10’dan kısa
    }
}
