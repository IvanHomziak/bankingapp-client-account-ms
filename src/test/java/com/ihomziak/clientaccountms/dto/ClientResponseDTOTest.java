package com.ihomziak.clientaccountms.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ClientResponseDTOTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        ClientResponseDTO clientResponseDTO = new ClientResponseDTO();
        long clientId = 1L;
        String firstName = "John";
        String lastName = "Doe";
        String dateOfBirth = "1990-01-01";
        String taxNumber = "123456789";
        String email = "john.doe@example.com";
        String phoneNumber = "123-456-7890";
        String address = "123 Main Street";
        LocalDateTime createdAt = LocalDateTime.of(2024, 11, 21, 10, 0);
        LocalDateTime updateAt = LocalDateTime.of(2024, 11, 22, 12, 30);
        String UUID = "123e4567-e89b-12d3-a456-426614174000";

        // Act
        clientResponseDTO.setClientId(clientId);
        clientResponseDTO.setFirstName(firstName);
        clientResponseDTO.setLastName(lastName);
        clientResponseDTO.setDateOfBirth(dateOfBirth);
        clientResponseDTO.setTaxNumber(taxNumber);
        clientResponseDTO.setEmail(email);
        clientResponseDTO.setPhoneNumber(phoneNumber);
        clientResponseDTO.setAddress(address);
        clientResponseDTO.setCreatedAt(createdAt);
        clientResponseDTO.setUpdateAt(updateAt);
        clientResponseDTO.setUUID(UUID);

        // Assert
        assertEquals(clientId, clientResponseDTO.getClientId());
        assertEquals(firstName, clientResponseDTO.getFirstName());
        assertEquals(lastName, clientResponseDTO.getLastName());
        assertEquals(dateOfBirth, clientResponseDTO.getDateOfBirth());
        assertEquals(taxNumber, clientResponseDTO.getTaxNumber());
        assertEquals(email, clientResponseDTO.getEmail());
        assertEquals(phoneNumber, clientResponseDTO.getPhoneNumber());
        assertEquals(address, clientResponseDTO.getAddress());
        assertEquals(createdAt, clientResponseDTO.getCreatedAt());
        assertEquals(updateAt, clientResponseDTO.getUpdateAt());
        assertEquals(UUID, clientResponseDTO.getUUID());
    }

    @Test
    void testToString() {
        // Arrange
        ClientResponseDTO clientResponseDTO = new ClientResponseDTO();
        clientResponseDTO.setClientId(1L);
        clientResponseDTO.setFirstName("John");
        clientResponseDTO.setLastName("Doe");
        clientResponseDTO.setDateOfBirth("1990-01-01");
        clientResponseDTO.setTaxNumber("123456789");
        clientResponseDTO.setEmail("john.doe@example.com");
        clientResponseDTO.setPhoneNumber("123-456-7890");
        clientResponseDTO.setAddress("123 Main Street");
        clientResponseDTO.setCreatedAt(LocalDateTime.of(2024, 11, 21, 10, 0));
        clientResponseDTO.setUpdateAt(LocalDateTime.of(2024, 11, 22, 12, 30));
        clientResponseDTO.setUUID("123e4567-e89b-12d3-a456-426614174000");

        // Act
        String result = clientResponseDTO.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("clientId=1"));
        assertTrue(result.contains("firstName='John'"));
        assertTrue(result.contains("lastName='Doe'"));
        assertTrue(result.contains("dateOfBirth='1990-01-01'"));
        assertTrue(result.contains("taxNumber='123456789'"));
        assertTrue(result.contains("email='john.doe@example.com'"));
        assertTrue(result.contains("phoneNumber='123-456-7890'"));
        assertTrue(result.contains("address='123 Main Street'"));
        assertTrue(result.contains("createdAt=2024-11-21T10:00"));
        assertTrue(result.contains("updateAt=2024-11-22T12:30"));
        assertTrue(result.contains("UUID='123e4567-e89b-12d3-a456-426614174000'"));
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        ClientResponseDTO dto1 = new ClientResponseDTO();
        dto1.setClientId(1L);
        dto1.setFirstName("John");
        dto1.setLastName("Doe");
        dto1.setDateOfBirth("1990-01-01");
        dto1.setTaxNumber("123456789");
        dto1.setEmail("john.doe@example.com");
        dto1.setPhoneNumber("123-456-7890");
        dto1.setAddress("123 Main Street");
        dto1.setCreatedAt(LocalDateTime.of(2024, 11, 21, 10, 0));
        dto1.setUpdateAt(LocalDateTime.of(2024, 11, 22, 12, 30));
        dto1.setUUID("123e4567-e89b-12d3-a456-426614174000");

        ClientResponseDTO dto2 = new ClientResponseDTO();
        dto2.setClientId(1L);
        dto2.setFirstName("John");
        dto2.setLastName("Doe");
        dto2.setDateOfBirth("1990-01-01");
        dto2.setTaxNumber("123456789");
        dto2.setEmail("john.doe@example.com");
        dto2.setPhoneNumber("123-456-7890");
        dto2.setAddress("123 Main Street");
        dto2.setCreatedAt(LocalDateTime.of(2024, 11, 21, 10, 0));
        dto2.setUpdateAt(LocalDateTime.of(2024, 11, 22, 12, 30));
        dto2.setUUID("123e4567-e89b-12d3-a456-426614174000");

        ClientResponseDTO dto3 = new ClientResponseDTO();
        dto3.setClientId(2L);
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
        ClientResponseDTO dto = new ClientResponseDTO();
        dto.setClientId(1L);
        dto.setFirstName("John");

        // Act & Assert
        assertNotEquals(dto, null); // dto compared to null should return false
        assertNotEquals(dto, "Not a ClientResponseDTO"); // dto compared to a different class should return false
    }
}
