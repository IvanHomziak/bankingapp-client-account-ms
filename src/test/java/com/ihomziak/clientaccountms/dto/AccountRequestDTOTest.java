package com.ihomziak.clientaccountms.dto;

import com.ihomziak.bankingapp.common.utils.AccountType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

class AccountRequestDTOTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        AccountRequestDTO accountRequestDTO = new AccountRequestDTO();
        String accountNumber = "123456789012345678";
        AccountType accountType = AccountType.CHECKING;
        BigDecimal balance = BigDecimal.valueOf(1000.50);
        String clientUUID = "987e6543-e21b-12d3-a456-426614174000";

        // Act
        accountRequestDTO.setAccountNumber(accountNumber);
        accountRequestDTO.setAccountType(accountType);
        accountRequestDTO.setBalance(balance);
        accountRequestDTO.setAccountUuid(clientUUID);

        // Assert
        assertEquals(accountNumber, accountRequestDTO.getAccountNumber());
        assertEquals(accountType, accountRequestDTO.getAccountType());
        assertEquals(balance, accountRequestDTO.getBalance());
        assertEquals(clientUUID, accountRequestDTO.getAccountUuid());
    }

    @Test
    void testDefaultConstructor() {
        // Arrange & Act
        AccountRequestDTO accountRequestDTO = new AccountRequestDTO();

        // Assert
        assertNotNull(accountRequestDTO);
        assertNull(accountRequestDTO.getAccountNumber());
        assertNull(accountRequestDTO.getAccountType());
        assertEquals(0.0, accountRequestDTO.getBalance());
        assertNull(accountRequestDTO.getAccountUuid());
    }

    @Test
    void testToString() {
        // Arrange
        AccountRequestDTO accountRequestDTO = new AccountRequestDTO();
        accountRequestDTO.setAccountNumber("123456789012345678");
        accountRequestDTO.setAccountType(AccountType.SAVINGS);
        accountRequestDTO.setBalance(BigDecimal.valueOf(2500.75));
        accountRequestDTO.setAccountUuid("123e4567-e89b-12d3-a456-426614174000");

        // Act
        String result = accountRequestDTO.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("accountNumber='123456789012345678'"));
        assertTrue(result.contains("accountType=Savings"));
        assertTrue(result.contains("balance=2500.75"));
        assertTrue(result.contains("clientUUID='123e4567-e89b-12d3-a456-426614174000'"));
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        AccountRequestDTO dto1 = new AccountRequestDTO();
        dto1.setAccountNumber("123456789012345678");
        dto1.setAccountType(AccountType.CHECKING);
        dto1.setBalance(BigDecimal.valueOf(1000.0));
        dto1.setAccountUuid("123e4567-e89b-12d3-a456-426614174000");

        AccountRequestDTO dto2 = new AccountRequestDTO();
        dto2.setAccountNumber("123456789012345678");
        dto2.setAccountType(AccountType.CHECKING);
        dto2.setBalance(BigDecimal.valueOf(1000.0));
        dto2.setAccountUuid("123e4567-e89b-12d3-a456-426614174000");

        AccountRequestDTO dto3 = new AccountRequestDTO();
        dto3.setAccountNumber("987654321098765432");
        dto3.setAccountType(AccountType.SAVINGS);
        dto3.setBalance(BigDecimal.valueOf(2000.0));
        dto3.setAccountUuid("987e6543-e21b-12d3-a456-426614174000");

        // Act & Assert
        assertEquals(dto1, dto2); // dto1 and dto2 have the same data
        assertNotEquals(dto1, dto3); // dto1 and dto3 have different data
        assertEquals(dto1.hashCode(), dto2.hashCode()); // dto1 and dto2 should have the same hashCode
        assertNotEquals(dto1.hashCode(), dto3.hashCode()); // dto1 and dto3 should have different hashCodes
    }

    @Test
    void testEqualsWithNullAndDifferentClass() {
        // Arrange
        AccountRequestDTO dto = new AccountRequestDTO();
        dto.setAccountNumber("123456789012345678");
        dto.setAccountType(AccountType.CHECKING);
        dto.setBalance(BigDecimal.valueOf(1000.0));
        dto.setAccountUuid("123e4567-e89b-12d3-a456-426614174000");

        // Act & Assert
        assertNotEquals(dto, null); // dto compared to null should return false
        assertNotEquals(dto, "Not an AccountRequestDTO"); // dto compared to a different class should return false
    }
}
