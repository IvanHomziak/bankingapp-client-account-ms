package com.ihomziak.clientmanagerservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountHolderDTOTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO();
        String uuid = "123e4567-e89b-12d3-a456-426614174000";
        String firstName = "John";
        String lastName = "Doe";

        // Act
        accountHolderDTO.setUUID(uuid);
        accountHolderDTO.setFirstName(firstName);
        accountHolderDTO.setLastName(lastName);

        // Assert
        assertEquals(uuid, accountHolderDTO.getUUID());
        assertEquals(firstName, accountHolderDTO.getFirstName());
        assertEquals(lastName, accountHolderDTO.getLastName());
    }

    @Test
    void testDefaultConstructor() {
        // Arrange & Act
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO();

        // Assert
        assertNotNull(accountHolderDTO);
        assertNull(accountHolderDTO.getUUID());
        assertNull(accountHolderDTO.getFirstName());
        assertNull(accountHolderDTO.getLastName());
    }

    @Test
    void testToString() {
        // Arrange
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO();
        accountHolderDTO.setUUID("123e4567-e89b-12d3-a456-426614174000");
        accountHolderDTO.setFirstName("John");
        accountHolderDTO.setLastName("Doe");

        // Act
        String result = accountHolderDTO.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("UUID='123e4567-e89b-12d3-a456-426614174000'"));
        assertTrue(result.contains("firstName='John'"));
        assertTrue(result.contains("lastName='Doe'"));
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        AccountHolderDTO dto1 = new AccountHolderDTO();
        dto1.setUUID("123e4567-e89b-12d3-a456-426614174000");
        dto1.setFirstName("John");
        dto1.setLastName("Doe");

        AccountHolderDTO dto2 = new AccountHolderDTO();
        dto2.setUUID("123e4567-e89b-12d3-a456-426614174000");
        dto2.setFirstName("John");
        dto2.setLastName("Doe");

        AccountHolderDTO dto3 = new AccountHolderDTO();
        dto3.setUUID("987e6543-e21b-12d3-a456-426614174001");
        dto3.setFirstName("Jane");
        dto3.setLastName("Smith");

        // Act & Assert
        assertEquals(dto1, dto2); // dto1 and dto2 have the same data
        assertNotEquals(dto1, dto3); // dto1 and dto3 have different data
        assertEquals(dto1.hashCode(), dto2.hashCode()); // dto1 and dto2 should have the same hashCode
        assertNotEquals(dto1.hashCode(), dto3.hashCode()); // dto1 and dto3 should have different hashCodes
    }
}
