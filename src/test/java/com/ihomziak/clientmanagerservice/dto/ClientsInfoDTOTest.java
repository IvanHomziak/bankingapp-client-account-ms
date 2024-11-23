package com.ihomziak.clientmanagerservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientsInfoDTOTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        ClientsInfoDTO clientsInfoDTO = new ClientsInfoDTO();
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String phoneNumber = "123-456-7890";

        // Act
        clientsInfoDTO.setFirstName(firstName);
        clientsInfoDTO.setLastName(lastName);
        clientsInfoDTO.setEmail(email);
        clientsInfoDTO.setPhoneNumber(phoneNumber);

        // Assert
        assertEquals(firstName, clientsInfoDTO.getFirstName());
        assertEquals(lastName, clientsInfoDTO.getLastName());
        assertEquals(email, clientsInfoDTO.getEmail());
        assertEquals(phoneNumber, clientsInfoDTO.getPhoneNumber());
    }

    @Test
    void testToString() {
        // Arrange
        ClientsInfoDTO clientsInfoDTO = new ClientsInfoDTO();
        clientsInfoDTO.setFirstName("John");
        clientsInfoDTO.setLastName("Doe");
        clientsInfoDTO.setEmail("john.doe@example.com");
        clientsInfoDTO.setPhoneNumber("123-456-7890");

        // Act
        String result = clientsInfoDTO.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("firstName='John'"));
        assertTrue(result.contains("lastName='Doe'"));
        assertTrue(result.contains("email='john.doe@example.com'"));
        assertTrue(result.contains("phoneNumber='123-456-7890'"));
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        ClientsInfoDTO dto1 = new ClientsInfoDTO();
        dto1.setFirstName("John");
        dto1.setLastName("Doe");
        dto1.setEmail("john.doe@example.com");
        dto1.setPhoneNumber("123-456-7890");

        ClientsInfoDTO dto2 = new ClientsInfoDTO();
        dto2.setFirstName("John");
        dto2.setLastName("Doe");
        dto2.setEmail("john.doe@example.com");
        dto2.setPhoneNumber("123-456-7890");

        ClientsInfoDTO dto3 = new ClientsInfoDTO();
        dto3.setFirstName("Jane");
        dto3.setLastName("Smith");

        // Act & Assert
        assertEquals(dto1, dto2); // dto1 and dto2 are equal
        assertNotEquals(dto1, dto3); // dto1 and dto3 are not equal
        assertEquals(dto1.hashCode(), dto2.hashCode()); // Hash codes should match
        assertNotEquals(dto1.hashCode(), dto3.hashCode()); // Hash codes should not match
    }

    @Test
    void testEqualsWithNullAndDifferentClass() {
        // Arrange
        ClientsInfoDTO dto = new ClientsInfoDTO();
        dto.setFirstName("John");

        // Act & Assert
        assertNotEquals(dto, null); // dto compared to null should return false
        assertNotEquals(dto, "Not a ClientsInfoDTO"); // dto compared to a different class should return false
    }
}
