package com.ihomziak.clientmanagerservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ihomziak.clientmanagerservice.service.impl.AccountServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransactionEventConsumer {

    private final AccountServiceImpl accountService;

    @Autowired
    public TransactionEventConsumer(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    @KafkaListener(topics = {"${spring.kafka.topic.transfer-transactions-topic}"})
    public void onMessage(ConsumerRecord<String, AvroTransactionEventRequestDTO> consumerRecord) throws JsonProcessingException {
        log.info("Received ConsumerRecord: {}", consumerRecord.value());
        accountService.processTransactionEvent2(consumerRecord);
    }
}
