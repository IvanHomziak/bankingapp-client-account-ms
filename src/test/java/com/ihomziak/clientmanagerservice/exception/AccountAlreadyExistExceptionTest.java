package com.ihomziak.clientmanagerservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountAlreadyExistExceptionTest {

    @Test
    void testExceptionMessage() {
        // Arrange
        String errorMessage = "Account already exists";

        // Act
        AccountAlreadyExistException exception = new AccountAlreadyExistException(errorMessage);

        // Assert
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testExceptionWithoutMessage() {
        // Act
        AccountAlreadyExistException exception = new AccountAlreadyExistException(null);

        // Assert
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }
}
