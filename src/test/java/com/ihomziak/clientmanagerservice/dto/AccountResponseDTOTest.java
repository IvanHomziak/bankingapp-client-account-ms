package com.ihomziak.clientmanagerservice.dto;

import com.ihomziak.transactioncommon.utils.AccountType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AccountResponseDTOTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
        long accountId = 1L;
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO();
        accountHolderDTO.setUUID("123e4567-e89b-12d3-a456-426614174000");
        accountHolderDTO.setFirstName("John");
        accountHolderDTO.setLastName("Doe");

        String accountNumber = "123456789012345678";
        AccountType accountType = AccountType.CHECKING;
        double balance = 1000.50;
        String UUID = "987e6543-e21b-12d3-a456-426614174001";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime lastUpdated = LocalDateTime.now();

        // Act
        accountResponseDTO.setAccountId(accountId);
        accountResponseDTO.setAccountHolderDTO(accountHolderDTO);
        accountResponseDTO.setAccountNumber(accountNumber);
        accountResponseDTO.setAccountType(accountType);
        accountResponseDTO.setBalance(balance);
        accountResponseDTO.setUUID(UUID);
        accountResponseDTO.setCreatedAt(createdAt);
        accountResponseDTO.setLastUpdated(lastUpdated);

        // Assert
        assertEquals(accountId, accountResponseDTO.getAccountId());
        assertEquals(accountHolderDTO, accountResponseDTO.getAccountHolderDTO());
        assertEquals(accountNumber, accountResponseDTO.getAccountNumber());
        assertEquals(accountType, accountResponseDTO.getAccountType());
        assertEquals(balance, accountResponseDTO.getBalance());
        assertEquals(UUID, accountResponseDTO.getUUID());
        assertEquals(createdAt, accountResponseDTO.getCreatedAt());
        assertEquals(lastUpdated, accountResponseDTO.getLastUpdated());
    }

    @Test
    void testToString() {
        // Arrange
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
        accountResponseDTO.setAccountId(1L);

        AccountHolderDTO accountHolderDTO = new AccountHolderDTO();
        accountHolderDTO.setUUID("123e4567-e89b-12d3-a456-426614174000");
        accountHolderDTO.setFirstName("John");
        accountHolderDTO.setLastName("Doe");

        accountResponseDTO.setAccountHolderDTO(accountHolderDTO);
        accountResponseDTO.setAccountNumber("123456789012345678");
        accountResponseDTO.setAccountType(AccountType.SAVINGS);
        accountResponseDTO.setBalance(2500.75);
        accountResponseDTO.setUUID("987e6543-e21b-12d3-a456-426614174001");
        accountResponseDTO.setCreatedAt(LocalDateTime.of(2024, 11, 21, 10, 0));
        accountResponseDTO.setLastUpdated(LocalDateTime.of(2024, 11, 22, 12, 30));

        // Act
        String result = accountResponseDTO.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("accountId=1"));
        assertTrue(result.contains("accountHolderDTO="));
        assertTrue(result.contains("accountNumber='123456789012345678'"));
        assertTrue(result.contains("accountType=Savings"));
        assertTrue(result.contains("balance=2500.75"));
        assertTrue(result.contains("UUID='987e6543-e21b-12d3-a456-426614174001'"));
        assertTrue(result.contains("createdAt=2024-11-21T10:00"));
        assertTrue(result.contains("lastUpdated=2024-11-22T12:30"));
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        AccountResponseDTO dto1 = new AccountResponseDTO();
        dto1.setAccountId(1L);
        dto1.setAccountNumber("123456789012345678");
        dto1.setAccountType(AccountType.CHECKING);
        dto1.setBalance(1000.0);
        dto1.setUUID("987e6543-e21b-12d3-a456-426614174001");
        dto1.setCreatedAt(LocalDateTime.of(2024, 11, 21, 10, 0));
        dto1.setLastUpdated(LocalDateTime.of(2024, 11, 22, 12, 30));

        AccountResponseDTO dto2 = new AccountResponseDTO();
        dto2.setAccountId(1L);
        dto2.setAccountNumber("123456789012345678");
        dto2.setAccountType(AccountType.CHECKING);
        dto2.setBalance(1000.0);
        dto2.setUUID("987e6543-e21b-12d3-a456-426614174001");
        dto2.setCreatedAt(LocalDateTime.of(2024, 11, 21, 10, 0));
        dto2.setLastUpdated(LocalDateTime.of(2024, 11, 22, 12, 30));

        AccountResponseDTO dto3 = new AccountResponseDTO();
        dto3.setAccountId(2L);
        dto3.setAccountNumber("987654321098765432");
        dto3.setAccountType(AccountType.SAVINGS);
        dto3.setBalance(2000.0);
        dto3.setUUID("123e4567-e89b-12d3-a456-426614174000");
        dto3.setCreatedAt(LocalDateTime.of(2024, 11, 20, 9, 0));
        dto3.setLastUpdated(LocalDateTime.of(2024, 11, 21, 11, 0));

        // Act & Assert
        assertEquals(dto1, dto2); // dto1 and dto2 have the same data
        assertNotEquals(dto1, dto3); // dto1 and dto3 have different data
        assertEquals(dto1.hashCode(), dto2.hashCode()); // dto1 and dto2 should have the same hashCode
        assertNotEquals(dto1.hashCode(), dto3.hashCode()); // dto1 and dto3 should have different hashCodes
    }

    @Test
    void testEqualsWithNullAndDifferentClass() {
        // Arrange
        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setAccountId(1L);
        dto.setAccountNumber("123456789012345678");
        dto.setAccountType(AccountType.CHECKING);
        dto.setBalance(1000.0);
        dto.setUUID("987e6543-e21b-12d3-a456-426614174001");

        // Act & Assert
        assertNotEquals(dto, null); // dto compared to null should return false
        assertNotEquals(dto, "Not an AccountResponseDTO"); // dto compared to a different class should return false
    }
}
