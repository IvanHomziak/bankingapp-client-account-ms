package com.ihomziak.clientmanagerservice.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ihomziak.clientmanagerservice.dto.TransactionEventRequestDTO;
import com.ihomziak.clientmanagerservice.dto.TransactionEventResponseDTO;
import com.ihomziak.transactioncommon.TransactionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class TransactionEventProducer {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<Integer, String> kafkaTemplate;


    @Value("${spring.kafka.topic.transaction-results-topic}")
    private String transactionResultsTopic;

    public TransactionEventProducer(ObjectMapper objectMapper, KafkaTemplate<Integer, String> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }


    public CompletableFuture<SendResult<Integer, String>> sendTransactionResponse(TransactionEventRequestDTO transactionEventRequestDTO, String statusMessage, TransactionStatus status) throws JsonProcessingException {
        TransactionEventResponseDTO responseDTO = TransactionEventResponseDTO.builder()
                .transactionUuid(transactionEventRequestDTO.getTransactionUuid())
                .transactionStatus(status)
                .statusMessage(statusMessage)
                .build();
        log.info("Sending transaction response message: {}", responseDTO);
        Integer key = 1;
        String value = objectMapper.writeValueAsString(responseDTO);

        log.info("Sending transaction response: {}", value);
        CompletableFuture<SendResult<Integer, String>> completableFuture = kafkaTemplate.send(transactionResultsTopic, key, value);

        completableFuture
                .whenComplete((sendResult, throwable) -> {
                    if (throwable != null) {
                        handleFailure(key, value, throwable);
                    } else {
                        handleSeccess(key, value, sendResult);
                    }
                });

        return completableFuture;
    }


    private void handleSeccess(Integer key, String value, SendResult<Integer, String> sendResult) {
        log.info("Message sent successfully for the key: {} and value: {}, partition is {}",
                key, value, sendResult.getRecordMetadata().partition());
    }

    private void handleFailure(Integer key, String value, Throwable ex) {
        log.error("Error occurred while sending library event to Kafka: {}", ex.getMessage(), ex);
    }
}