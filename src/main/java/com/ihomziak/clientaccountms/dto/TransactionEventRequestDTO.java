package com.ihomziak.clientaccountms.dto;

import java.math.BigDecimal;

import com.ihomziak.bankingapp.common.utils.TransactionStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEventRequestDTO {
    private String transactionUuid;
    private String senderUuid;
    private String receiverUuid;
    private BigDecimal amount;
    private TransactionStatus transactionStatus;
}