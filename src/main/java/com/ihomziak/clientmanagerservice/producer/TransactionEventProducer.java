package com.ihomziak.clientmanagerservice.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ihomziak.transactionmanagementservice.dto.AvroTransactionEventResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class TransactionEventProducer {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, AvroTransactionEventResponseDTO> kafkaTemplate;


    @Value("${spring.kafka.topic.transaction-results-topic}")
    private String transactionResultsTopic;

    public TransactionEventProducer(ObjectMapper objectMapper, KafkaTemplate<String, AvroTransactionEventResponseDTO> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }


    public CompletableFuture<SendResult<String, AvroTransactionEventResponseDTO>> sendTransactionResponse(AvroTransactionEventRequestDTO transactionEventRequestDTO, String statusMessage, String status) throws JsonProcessingException {
        AvroTransactionEventResponseDTO responseDTO = new AvroTransactionEventResponseDTO();
        responseDTO.setTransactionUuid(transactionEventRequestDTO.getTransactionUuid().toString());
        responseDTO.setTransactionStatus(status);
        responseDTO.setStatusMessage(statusMessage);

        log.info("Sending transaction response message: {}", responseDTO);
        String key = UUID.randomUUID().toString();
//        String value = objectMapper.writeValueAsString(responseDTO);

        log.info("Sending transaction response: {}", responseDTO);
        CompletableFuture<SendResult<String, AvroTransactionEventResponseDTO>> completableFuture = kafkaTemplate.send(transactionResultsTopic, key, responseDTO);

        completableFuture
                .whenComplete((sendResult, throwable) -> {
                    if (throwable != null) {
                        handleFailure(key, responseDTO, throwable);
                    } else {
                        handleSeccess(key, responseDTO, sendResult);
                    }
                });

        return completableFuture;
    }


    private void handleSeccess(String key, AvroTransactionEventResponseDTO value, SendResult<String, AvroTransactionEventResponseDTO> sendResult) {
        log.info("Message sent successfully for the key: {} and value: {}, partition is {}",
                key, value, sendResult.getRecordMetadata().partition());
    }

    private void handleFailure(String key, AvroTransactionEventResponseDTO value, Throwable ex) {
        log.error("Error occurred while sending library event to Kafka: {}", ex.getMessage(), ex);
    }
}