package com.ihomziak.clientmanagerservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountNotFoundExceptionTest {

    @Test
    void testExceptionMessage() {
        // Arrange
        String errorMessage = "Account not found!";

        // Act
        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            throw new AccountNotFoundException(errorMessage);
        });

        // Assert
        assertEquals(errorMessage, exception.getMessage());
    }
}
