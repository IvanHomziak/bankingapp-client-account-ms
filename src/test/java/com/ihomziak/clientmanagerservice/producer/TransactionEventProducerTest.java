package com.ihomziak.clientmanagerservice.producer;

//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ihomziak.clientmanagerservice.dto.TransactionEventRequestDTO;
//import com.ihomziak.bankingapp.common.utils.TransactionStatus;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.SendResult;
//import org.springframework.util.concurrent.SettableListenableFuture;
//
//import java.util.concurrent.CompletableFuture;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;

class TransactionEventProducerTest {
//
//    @Mock
//    private ObjectMapper objectMapper;
//
//    @Mock
//    private KafkaTemplate<Integer, String> kafkaTemplate;
//
//    @InjectMocks
//    private TransactionEventProducer transactionEventProducer;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testSendTransactionResponse_Success() throws JsonProcessingException {
//        // Mocking data
//        TransactionEventRequestDTO transactionEventRequestDTO = new TransactionEventRequestDTO();
//        transactionEventRequestDTO.setTransactionUuid("123e4567-e89b-12d3-a456-426614174001");
//
//        String statusMessage = "Transaction completed successfully";
//        TransactionStatus status = TransactionStatus.COMPLETED;
//
//        String serializedResponse = "{\"transactionUuid\":\"123e4567-e89b-12d3-a456-426614174001\",\"transactionStatus\":\"COMPLETED\",\"statusMessage\":\"Transaction completed successfully\"}";
//
//        when(objectMapper.writeValueAsString(any())).thenReturn(serializedResponse);
//
//        // Mocking Kafka response
//        SettableListenableFuture<SendResult<Integer, String>> future = new SettableListenableFuture<>();
//        future.set(mock(SendResult.class));
//        when(kafkaTemplate.send(anyString(), anyInt(), anyString())).thenReturn(CompletableFuture.completedFuture(mock(SendResult.class)));
//
//        // Method call
//        CompletableFuture<SendResult<Integer, String>> response = transactionEventProducer.sendTransactionResponse(transactionEventRequestDTO, statusMessage, status);
//
//        // Assertions
//        assertNotNull(response);
//        verify(kafkaTemplate, times(1)).send(anyString(), anyInt(), eq(serializedResponse));
//        verify(objectMapper, times(1)).writeValueAsString(any());
//    }
//
//    @Test
//    void testSendTransactionResponse_Failure() throws JsonProcessingException {
//        // Mocking data
//        TransactionEventRequestDTO transactionEventRequestDTO = new TransactionEventRequestDTO();
//        transactionEventRequestDTO.setTransactionUuid("123e4567-e89b-12d3-a456-426614174001");
//
//        String statusMessage = "Transaction failed";
//        TransactionStatus status = TransactionStatus.FAILED;
//
//        String serializedResponse = "{\"transactionUuid\":\"123e4567-e89b-12d3-a456-426614174001\",\"transactionStatus\":\"FAILED\",\"statusMessage\":\"Transaction failed\"}";
//
//        when(objectMapper.writeValueAsString(any())).thenReturn(serializedResponse);
//
//        // Mocking Kafka failure
//        CompletableFuture<SendResult<Integer, String>> future = CompletableFuture.failedFuture(new RuntimeException("Kafka send failed"));
//        when(kafkaTemplate.send(anyString(), anyInt(), anyString())).thenReturn(future);
//
//        // Method call
//        CompletableFuture<SendResult<Integer, String>> response = transactionEventProducer.sendTransactionResponse(transactionEventRequestDTO, statusMessage, status);
//
//        // Assertions
//        assertNotNull(response);
//        verify(kafkaTemplate, times(1)).send(anyString(), anyInt(), eq(serializedResponse));
//        verify(objectMapper, times(1)).writeValueAsString(any());
//    }
//
//    @Test
//    void testHandleSuccessThroughSendTransactionResponse() throws JsonProcessingException {
//        // Mocking data
//        TransactionEventRequestDTO transactionEventRequestDTO = new TransactionEventRequestDTO();
//        transactionEventRequestDTO.setTransactionUuid("123e4567-e89b-12d3-a456-426614174001");
//
//        String statusMessage = "Transaction completed successfully";
//        TransactionStatus status = TransactionStatus.COMPLETED;
//
//        String serializedResponse = "{\"transactionUuid\":\"123e4567-e89b-12d3-a456-426614174001\",\"transactionStatus\":\"COMPLETED\",\"statusMessage\":\"Transaction completed successfully\"}";
//
//        when(objectMapper.writeValueAsString(any())).thenReturn(serializedResponse);
//
//        // Mocking Kafka response
//        SendResult<Integer, String> sendResult = mock(SendResult.class);
//        when(kafkaTemplate.send(anyString(), anyInt(), anyString())).thenReturn(CompletableFuture.completedFuture(sendResult));
//
//        // Method call
//        CompletableFuture<SendResult<Integer, String>> response = transactionEventProducer.sendTransactionResponse(transactionEventRequestDTO, statusMessage, status);
//
//        // Assertions
//        assertNotNull(response);
//        verify(kafkaTemplate, times(1)).send(anyString(), anyInt(), eq(serializedResponse));
//        verify(objectMapper, times(1)).writeValueAsString(any());
//        // Successful send indirectly tests `handleSeccess`.
//    }
//
//    @Test
//    void testHandleFailureThroughSendTransactionResponse() throws JsonProcessingException {
//        // Mocking data
//        TransactionEventRequestDTO transactionEventRequestDTO = new TransactionEventRequestDTO();
//        transactionEventRequestDTO.setTransactionUuid("123e4567-e89b-12d3-a456-426614174001");
//
//        String statusMessage = "Transaction failed";
//        TransactionStatus status = TransactionStatus.FAILED;
//
//        String serializedResponse = "{\"transactionUuid\":\"123e4567-e89b-12d3-a456-426614174001\",\"transactionStatus\":\"FAILED\",\"statusMessage\":\"Transaction failed\"}";
//
//        when(objectMapper.writeValueAsString(any())).thenReturn(serializedResponse);
//
//        // Mocking Kafka failure
//        CompletableFuture<SendResult<Integer, String>> future = CompletableFuture.failedFuture(new RuntimeException("Kafka send failed"));
//        when(kafkaTemplate.send(anyString(), anyInt(), anyString())).thenReturn(future);
//
//        // Method call
//        CompletableFuture<SendResult<Integer, String>> response = transactionEventProducer.sendTransactionResponse(transactionEventRequestDTO, statusMessage, status);
//
//        // Assertions
//        assertNotNull(response);
//        verify(kafkaTemplate, times(1)).send(anyString(), anyInt(), eq(serializedResponse));
//        verify(objectMapper, times(1)).writeValueAsString(any());
//        // Failed send indirectly tests `handleFailure`.
//    }

}
