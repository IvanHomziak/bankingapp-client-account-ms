package com.ihomziak.clientmanagerservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NonSufficientFundsExceptionTest {

    @Test
    void testExceptionMessage() {
        // Arrange
        String errorMessage = "Insufficient funds.";

        // Act
        NonSufficientFundsException exception = assertThrows(NonSufficientFundsException.class, () -> {
            throw new NonSufficientFundsException(errorMessage);
        });

        // Assert
        assertEquals(errorMessage, exception.getMessage());
    }
}
