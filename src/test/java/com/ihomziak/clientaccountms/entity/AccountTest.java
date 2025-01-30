package com.ihomziak.clientaccountms.entity;

import com.ihomziak.bankingapp.common.utils.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Account account;
    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setClientId(1L);
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setUUID("123e4567-e89b-12d3-a456-426614174000");

        account = new Account();
        account.setAccountId(100L);
        account.setAccountNumber("123456789012345678");
        account.setAccountType(AccountType.CHECKING);
        account.setBalance(1500.75);
        account.setCreatedAt(LocalDateTime.of(2024, 11, 21, 10, 0));
        account.setLastUpdate(LocalDateTime.of(2024, 11, 22, 12, 30));
        account.setUUID("987e6543-e21b-12d3-a456-426614174001");
        account.setClient(client);
    }

    @Test
    void testAccountFields() {
        assertEquals(100L, account.getAccountId());
        assertEquals("123456789012345678", account.getAccountNumber());
        assertEquals(AccountType.CHECKING, account.getAccountType());
        assertEquals(1500.75, account.getBalance());
        assertEquals(LocalDateTime.of(2024, 11, 21, 10, 0), account.getCreatedAt());
        assertEquals(LocalDateTime.of(2024, 11, 22, 12, 30), account.getLastUpdate());
        assertEquals("987e6543-e21b-12d3-a456-426614174001", account.getUUID());
    }

    @Test
    void testAccountRelationship() {
        assertNotNull(account.getClient());
        assertEquals("123e4567-e89b-12d3-a456-426614174000", account.getClient().getUUID());
        assertEquals("John", account.getClient().getFirstName());
        assertEquals("Doe", account.getClient().getLastName());
    }

    @Test
    void testToString() {
        String accountString = account.toString();

        assertTrue(accountString.contains("accountId=100"));
        assertTrue(accountString.contains("accountNumber='123456789012345678'"));
        assertTrue(accountString.contains("accountType=Checking"));
        assertTrue(accountString.contains("balance=1500.75"));
        assertTrue(accountString.contains("createdAt=2024-11-21T10:00"));
        assertTrue(accountString.contains("lastUpdate=2024-11-22T12:30"));
        assertTrue(accountString.contains("UUID='987e6543-e21b-12d3-a456-426614174001'"));
        assertTrue(accountString.contains("clientUUID=123e4567-e89b-12d3-a456-426614174000"));
    }

    @Test
    void testNullClient() {
        account.setClient(null);
        String accountString = account.toString();

        assertTrue(accountString.contains("clientUUID=null"));
    }
}
