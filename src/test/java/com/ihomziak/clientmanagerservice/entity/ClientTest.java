package com.ihomziak.clientmanagerservice.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    private Client client;
    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setAccountId(100L);
        account.setAccountNumber("123456789012345678");
        account.setBalance(1000.50);
        account.setCreatedAt(LocalDateTime.of(2024, 11, 21, 10, 0));
        account.setLastUpdate(LocalDateTime.of(2024, 11, 22, 12, 30));
        account.setUUID("987e6543-e21b-12d3-a456-426614174001");

        client = new Client();
        client.setClientId(1L);
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setDateOfBirth("1990-01-01");
        client.setTaxNumber("123456789");
        client.setEmail("john.doe@example.com");
        client.setPhoneNumber("123-456-7890");
        client.setAddress("123 Main Street");
        client.setCreatedAt(LocalDateTime.of(2024, 11, 21, 10, 0));
        client.setUpdatedAt(LocalDateTime.of(2024, 11, 22, 12, 30));
        client.setUUID("123e4567-e89b-12d3-a456-426614174000");
        client.setAccount(Collections.singletonList(account));

        account.setClient(client); // Setting the relationship
    }

    @Test
    void testClientFields() {
        assertEquals(1L, client.getClientId());
        assertEquals("John", client.getFirstName());
        assertEquals("Doe", client.getLastName());
        assertEquals("1990-01-01", client.getDateOfBirth());
        assertEquals("123456789", client.getTaxNumber());
        assertEquals("john.doe@example.com", client.getEmail());
        assertEquals("123-456-7890", client.getPhoneNumber());
        assertEquals("123 Main Street", client.getAddress());
        assertEquals(LocalDateTime.of(2024, 11, 21, 10, 0), client.getCreatedAt());
        assertEquals(LocalDateTime.of(2024, 11, 22, 12, 30), client.getUpdatedAt());
        assertEquals("123e4567-e89b-12d3-a456-426614174000", client.getUUID());
    }

    @Test
    void testClientRelationship() {
        assertNotNull(client.getAccount());
        assertEquals(1, client.getAccount().size());
        assertEquals(account, client.getAccount().get(0));
    }

    @Test
    void testToString() {
        String clientString = client.toString();

        assertTrue(clientString.contains("clientId=1"));
        assertTrue(clientString.contains("firstName='John'"));
        assertTrue(clientString.contains("lastName='Doe'"));
        assertTrue(clientString.contains("dateOfBirth='1990-01-01'"));
        assertTrue(clientString.contains("taxNumber='123456789'"));
        assertTrue(clientString.contains("email='john.doe@example.com'"));
        assertTrue(clientString.contains("phoneNumber='123-456-7890'"));
        assertTrue(clientString.contains("address='123 Main Street'"));
        assertTrue(clientString.contains("createdAt=2024-11-21T10:00"));
        assertTrue(clientString.contains("updatedAt=2024-11-22T12:30"));
        assertTrue(clientString.contains("UUID='123e4567-e89b-12d3-a456-426614174000'"));
        assertTrue(clientString.contains("accountIds=[100]"));
    }
}
