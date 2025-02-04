package com.ihomziak.clientaccountms.dto;

import com.ihomziak.bankingapp.common.utils.TransactionStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionEventResponseDTOTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        TransactionEventResponseDTO responseDTO = new TransactionEventResponseDTO();
        String transactionUuid = "123e4567-e89b-12d3-a456-426614174001";
        TransactionStatus transactionStatus = TransactionStatus.COMPLETED;
        String statusMessage = "Transaction completed successfully";

        // Act
        responseDTO.setTransactionUuid(transactionUuid);
        responseDTO.setTransactionStatus(transactionStatus);
        responseDTO.setStatusMessage(statusMessage);

        // Assert
        assertEquals(transactionUuid, responseDTO.getTransactionUuid());
        assertEquals(transactionStatus, responseDTO.getTransactionStatus());
        assertEquals(statusMessage, responseDTO.getStatusMessage());
    }

    @Test
    void testBuilder() {
        // Arrange & Act
        TransactionEventResponseDTO responseDTO = TransactionEventResponseDTO.builder()
                .transactionUuid("123e4567-e89b-12d3-a456-426614174001")
                .transactionStatus(TransactionStatus.FAILED)
                .statusMessage("Transaction failed")
                .build();

        // Assert
        assertNotNull(responseDTO);
        assertEquals("123e4567-e89b-12d3-a456-426614174001", responseDTO.getTransactionUuid());
        assertEquals(TransactionStatus.FAILED, responseDTO.getTransactionStatus());
        assertEquals("Transaction failed", responseDTO.getStatusMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        TransactionEventResponseDTO response1 = new TransactionEventResponseDTO(
                "123e4567-e89b-12d3-a456-426614174001",
                TransactionStatus.COMPLETED,
                "Transaction completed successfully"
        );
        TransactionEventResponseDTO response2 = new TransactionEventResponseDTO(
                "123e4567-e89b-12d3-a456-426614174001",
                TransactionStatus.COMPLETED,
                "Transaction completed successfully"
        );
        TransactionEventResponseDTO response3 = new TransactionEventResponseDTO(
                "different-uuid",
                TransactionStatus.FAILED,
                "Transaction failed"
        );

        // Assert equality
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        TransactionEventResponseDTO responseDTO = new TransactionEventResponseDTO(
                "123e4567-e89b-12d3-a456-426614174001",
                TransactionStatus.COMPLETED,
                "Transaction completed successfully"
        );

        String expectedString = "TransactionEventResponseDTO{" +
                "transactionUuid='123e4567-e89b-12d3-a456-426614174001'," +
                " transactionStatus=COMPLETED," +
                " statusMessage='Transaction completed successfully'" +
                '}';

        // Assert
        assertEquals(expectedString, responseDTO.toString());
    }
}
