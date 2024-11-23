package com.ihomziak.clientmanagerservice.dto;

import com.ihomziak.transactioncommon.utils.TransactionStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionEventRequestDTOTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        TransactionEventRequestDTO dto = new TransactionEventRequestDTO();
        String transactionUuid = "123e4567-e89b-12d3-a456-426614174001";
        String senderUuid = "sender-uuid-123";
        String receiverUuid = "receiver-uuid-456";
        Double amount = 1000.50;
        TransactionStatus transactionStatus = TransactionStatus.COMPLETED;

        // Act
        dto.setTransactionUuid(transactionUuid);
        dto.setSenderUuid(senderUuid);
        dto.setReceiverUuid(receiverUuid);
        dto.setAmount(amount);
        dto.setTransactionStatus(transactionStatus);

        // Assert
        assertEquals(transactionUuid, dto.getTransactionUuid());
        assertEquals(senderUuid, dto.getSenderUuid());
        assertEquals(receiverUuid, dto.getReceiverUuid());
        assertEquals(amount, dto.getAmount());
        assertEquals(transactionStatus, dto.getTransactionStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        TransactionEventRequestDTO dto1 = new TransactionEventRequestDTO(
                "123e4567-e89b-12d3-a456-426614174001",
                "sender-uuid-123",
                "receiver-uuid-456",
                1000.50,
                TransactionStatus.COMPLETED
        );
        TransactionEventRequestDTO dto2 = new TransactionEventRequestDTO(
                "123e4567-e89b-12d3-a456-426614174001",
                "sender-uuid-123",
                "receiver-uuid-456",
                1000.50,
                TransactionStatus.COMPLETED
        );
        TransactionEventRequestDTO dto3 = new TransactionEventRequestDTO(
                "different-uuid",
                "sender-uuid-789",
                "receiver-uuid-999",
                500.0,
                TransactionStatus.FAILED
        );

        // Assert equality
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        TransactionEventRequestDTO dto = new TransactionEventRequestDTO(
                "123e4567-e89b-12d3-a456-426614174001",
                "sender-uuid-123",
                "receiver-uuid-456",
                1000.50,
                TransactionStatus.COMPLETED
        );

        String expectedString = "TransactionEventRequestDTO{" +
                "transactionUuid='123e4567-e89b-12d3-a456-426614174001'," +
                " senderUuid='sender-uuid-123'," +
                " receiverUuid='receiver-uuid-456'," +
                " amount=1000.5," +
                " transactionStatus=COMPLETED" +
                '}';

        // Assert
        assertEquals(expectedString, dto.toString());
    }
}
