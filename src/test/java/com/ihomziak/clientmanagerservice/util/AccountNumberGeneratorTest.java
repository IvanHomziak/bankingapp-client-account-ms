package com.ihomziak.clientmanagerservice.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountNumberGeneratorTest {

    @Test
    void testGenerateBankAccountNumber_Length() {
        // Generate account number
        String accountNumber = AccountNumberGenerator.generateBankAccountNumber();

        // Verify the length is exactly 18
        assertEquals(18, accountNumber.length(), "Account number should be 18 digits long.");
    }

    @Test
    void testGenerateBankAccountNumber_IsNumeric() {
        // Generate account number
        String accountNumber = AccountNumberGenerator.generateBankAccountNumber();

        // Verify all characters are digits
        assertTrue(accountNumber.matches("\\d+"), "Account number should contain only numeric characters.");
    }

    @Test
    void testGenerateBankAccountNumber_IsUnique() {
        // Generate two account numbers
        String accountNumber1 = AccountNumberGenerator.generateBankAccountNumber();
        String accountNumber2 = AccountNumberGenerator.generateBankAccountNumber();

        // Verify the generated numbers are unique
        assertNotEquals(accountNumber1, accountNumber2, "Generated account numbers should be unique.");
    }
}