package com.ihomziak.clientmanagerservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientRequestDTOTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        ClientRequestDTO clientRequestDTO = new ClientRequestDTO();
        String firstName = "John";
        String lastName = "Doe";
        String dateOfBirth = "1990-01-01";
        String taxNumber = "123456789";
        String email = "john.doe@example.com";
        String phoneNumber = "123-456-7890";
        String address = "123 Main Street";

        // Act
        clientRequestDTO.setFirstName(firstName);
        clientRequestDTO.setLastName(lastName);
        clientRequestDTO.setDateOfBirth(dateOfBirth);
        clientRequestDTO.setTaxNumber(taxNumber);
        clientRequestDTO.setEmail(email);
        clientRequestDTO.setPhoneNumber(phoneNumber);
        clientRequestDTO.setAddress(address);

        // Assert
        assertEquals(firstName, clientRequestDTO.getFirstName());
        assertEquals(lastName, clientRequestDTO.getLastName());
        assertEquals(dateOfBirth, clientRequestDTO.getDateOfBirth());
        assertEquals(taxNumber, clientRequestDTO.getTaxNumber());
        assertEquals(email, clientRequestDTO.getEmail());
        assertEquals(phoneNumber, clientRequestDTO.getPhoneNumber());
        assertEquals(address, clientRequestDTO.getAddress());
    }

    @Test
    void testToString() {
        // Arrange
        ClientRequestDTO clientRequestDTO = new ClientRequestDTO();
        clientRequestDTO.setFirstName("John");
        clientRequestDTO.setLastName("Doe");
        clientRequestDTO.setDateOfBirth("1990-01-01");
        clientRequestDTO.setTaxNumber("123456789");
        clientRequestDTO.setEmail("john.doe@example.com");
        clientRequestDTO.setPhoneNumber("123-456-7890");
        clientRequestDTO.setAddress("123 Main Street");

        // Act
        String result = clientRequestDTO.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("firstName='John'"));
        assertTrue(result.contains("lastName='Doe'"));
        assertTrue(result.contains("dateOfBirth='1990-01-01'"));
        assertTrue(result.contains("taxNumber='123456789'"));
        assertTrue(result.contains("email='john.doe@example.com'"));
        assertTrue(result.contains("phoneNumber='123-456-7890'"));
        assertTrue(result.contains("address='123 Main Street'"));
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        ClientRequestDTO dto1 = new ClientRequestDTO();
        dto1.setFirstName("John");
        dto1.setLastName("Doe");
        dto1.setDateOfBirth("1990-01-01");
        dto1.setTaxNumber("123456789");
        dto1.setEmail("john.doe@example.com");
        dto1.setPhoneNumber("123-456-7890");
        dto1.setAddress("123 Main Street");

        ClientRequestDTO dto2 = new ClientRequestDTO();
        dto2.setFirstName("John");
        dto2.setLastName("Doe");
        dto2.setDateOfBirth("1990-01-01");
        dto2.setTaxNumber("123456789");
        dto2.setEmail("john.doe@example.com");
        dto2.setPhoneNumber("123-456-7890");
        dto2.setAddress("123 Main Street");

        ClientRequestDTO dto3 = new ClientRequestDTO();
        dto3.setFirstName("Jane");
        dto3.setLastName("Smith");
        dto3.setDateOfBirth("1985-05-05");
        dto3.setTaxNumber("987654321");
        dto3.setEmail("jane.smith@example.com");
        dto3.setPhoneNumber("987-654-3210");
        dto3.setAddress("456 Elm Street");

        // Act & Assert
        assertEquals(dto1, dto2); // dto1 and dto2 have the same data
        assertNotEquals(dto1, dto3); // dto1 and dto3 have different data
        assertEquals(dto1.hashCode(), dto2.hashCode()); // dto1 and dto2 should have the same hashCode
        assertNotEquals(dto1.hashCode(), dto3.hashCode()); // dto1 and dto3 should have different hashCodes
    }

    @Test
    void testEqualsWithNullAndDifferentClass() {
        // Arrange
        ClientRequestDTO dto = new ClientRequestDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setDateOfBirth("1990-01-01");
        dto.setTaxNumber("123456789");
        dto.setEmail("john.doe@example.com");
        dto.setPhoneNumber("123-456-7890");
        dto.setAddress("123 Main Street");

        // Act & Assert
        assertNotEquals(dto, null); // dto compared to null should return false
        assertNotEquals(dto, "Not a ClientRequestDTO"); // dto compared to a different class should return false
    }
}
