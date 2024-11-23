package com.ihomziak.clientmanagerservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClientNotFoundExceptionTest {

    @Test
    void testExceptionMessage() {
        // Arrange
        String errorMessage = "Client not found.";

        // Act
        ClientNotFoundException exception = assertThrows(ClientNotFoundException.class, () -> {
            throw new ClientNotFoundException(errorMessage);
        });

        // Assert
        assertEquals(errorMessage, exception.getMessage());
    }
}
