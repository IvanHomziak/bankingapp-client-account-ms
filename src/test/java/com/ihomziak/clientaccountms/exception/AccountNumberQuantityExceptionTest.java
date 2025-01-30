package com.ihomziak.clientaccountms.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountNumberQuantityExceptionTest {

    @Test
    void testExceptionMessage() {
        // Arrange
        String errorMessage = "Invalid account number quantity!";

        // Act
        AccountNumberQuantityException exception = assertThrows(AccountNumberQuantityException.class, () -> {
            throw new AccountNumberQuantityException(errorMessage);
        });

        // Assert
        assertEquals(errorMessage, exception.getMessage());
    }
}
