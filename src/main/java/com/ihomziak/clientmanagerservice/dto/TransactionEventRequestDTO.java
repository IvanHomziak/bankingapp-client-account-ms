package com.ihomziak.clientmanagerservice.dto;

import com.ihomziak.transactioncommon.utils.TransactionStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransactionEventRequestDTO {
    private String transactionUuid;
    private String senderUuid;
    private String receiverUuid;
    private Double amount;
    private TransactionStatus transactionStatus;
}