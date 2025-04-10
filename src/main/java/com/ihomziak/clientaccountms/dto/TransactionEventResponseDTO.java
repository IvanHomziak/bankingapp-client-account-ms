package com.ihomziak.clientaccountms.dto;

import com.ihomziak.bankingapp.common.utils.TransactionStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEventResponseDTO {
    private String transactionUuid;
    private TransactionStatus transactionStatus;
    private String statusMessage;
}