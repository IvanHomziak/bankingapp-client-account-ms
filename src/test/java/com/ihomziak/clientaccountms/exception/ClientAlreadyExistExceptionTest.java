package com.ihomziak.clientaccountms.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClientAlreadyExistExceptionTest {

    @Test
    void testExceptionMessage() {
        // Arrange
        String errorMessage = "Client already exists!";

        // Act
        ClientAlreadyExistException exception = assertThrows(ClientAlreadyExistException.class, () -> {
            throw new ClientAlreadyExistException(errorMessage);
        });

        // Assert
        assertEquals(errorMessage, exception.getMessage());
    }
}
