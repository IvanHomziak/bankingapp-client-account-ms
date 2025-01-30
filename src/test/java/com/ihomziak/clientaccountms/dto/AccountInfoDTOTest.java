package com.ihomziak.clientaccountms.dto;

import com.ihomziak.bankingapp.common.utils.AccountType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountInfoDTOTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        AccountInfoDTO accountInfoDTO = new AccountInfoDTO();
        String accountNumber = "123456789012345678";
        AccountType accountType = AccountType.CHECKING;
        double balance = 1500.75;
        String uuid = "987e6543-e21b-12d3-a456-426614174000";

        // Act
        accountInfoDTO.setAccountNumber(accountNumber);
        accountInfoDTO.setAccountType(accountType);
        accountInfoDTO.setBalance(balance);
        accountInfoDTO.setUUID(uuid);

        // Assert
        assertEquals(accountNumber, accountInfoDTO.getAccountNumber());
        assertEquals(accountType, accountInfoDTO.getAccountType());
        assertEquals(balance, accountInfoDTO.getBalance());
        assertEquals(uuid, accountInfoDTO.getUUID());
    }

    @Test
    void testDefaultConstructor() {
        // Arrange & Act
        AccountInfoDTO accountInfoDTO = new AccountInfoDTO();

        // Assert
        assertNotNull(accountInfoDTO);
        assertNull(accountInfoDTO.getAccountNumber());
        assertNull(accountInfoDTO.getAccountType());
        assertEquals(0.0, accountInfoDTO.getBalance());
        assertNull(accountInfoDTO.getUUID());
    }

    @Test
    void testToString() {
        // Arrange
        AccountInfoDTO accountInfoDTO = new AccountInfoDTO();
        accountInfoDTO.setAccountNumber("123456789012345678");
        accountInfoDTO.setAccountType(AccountType.SAVINGS);
        accountInfoDTO.setBalance(2500.50);
        accountInfoDTO.setUUID("123e4567-e89b-12d3-a456-426614174000");

        // Act
        String result = accountInfoDTO.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("accountNumber='123456789012345678'"));
        assertTrue(result.contains("accountType=Savings"));
        assertTrue(result.contains("balance=2500.5"));
        assertTrue(result.contains("UUID='123e4567-e89b-12d3-a456-426614174000'"));
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        AccountInfoDTO dto1 = new AccountInfoDTO();
        dto1.setAccountNumber("123456789012345678");
        dto1.setAccountType(AccountType.CHECKING);
        dto1.setBalance(1000.0);
        dto1.setUUID("123e4567-e89b-12d3-a456-426614174000");

        AccountInfoDTO dto2 = new AccountInfoDTO();
        dto2.setAccountNumber("123456789012345678");
        dto2.setAccountType(AccountType.CHECKING);
        dto2.setBalance(1000.0);
        dto2.setUUID("123e4567-e89b-12d3-a456-426614174000");

        AccountInfoDTO dto3 = new AccountInfoDTO();
        dto3.setAccountNumber("987654321098765432");
        dto3.setAccountType(AccountType.SAVINGS);
        dto3.setBalance(5000.0);
        dto3.setUUID("987e6543-e21b-12d3-a456-426614174000");

        // Act & Assert
        assertEquals(dto1, dto2); // dto1 and dto2 have the same data
        assertNotEquals(dto1, dto3); // dto1 and dto3 have different data
        assertEquals(dto1.hashCode(), dto2.hashCode()); // dto1 and dto2 should have the same hashCode
        assertNotEquals(dto1.hashCode(), dto3.hashCode()); // dto1 and dto3 should have different hashCodes
    }

    @Test
    void testEqualsWithNullAndDifferentClass() {
        // Arrange
        AccountInfoDTO dto1 = new AccountInfoDTO();
        dto1.setAccountNumber("123456789012345678");
        dto1.setAccountType(AccountType.CHECKING);
        dto1.setBalance(1000.0);
        dto1.setUUID("123e4567-e89b-12d3-a456-426614174000");

        // Act & Assert
        assertNotEquals(dto1, null); // dto1 compared to null should return false
        assertNotEquals(dto1, "Not an AccountInfoDTO"); // dto1 compared to a different class should return false
    }
}
