package com.ihomziak.clientmanagerservice.dto;

import com.ihomziak.bankingapp.common.utils.TransactionStatus;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEventRequestDTO {
    private String transactionUuid;
    private String senderUuid;
    private String receiverUuid;
    private Double amount;
    private TransactionStatus transactionStatus;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TransactionEventRequestDTO that = (TransactionEventRequestDTO) o;
        return Objects.equals(transactionUuid, that.transactionUuid) && Objects.equals(senderUuid, that.senderUuid) && Objects.equals(receiverUuid, that.receiverUuid) && Objects.equals(amount, that.amount) && transactionStatus == that.transactionStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionUuid, senderUuid, receiverUuid, amount, transactionStatus);
    }

    @Override
    public String toString() {
        return "TransactionEventRequestDTO{" +
                "transactionUuid='" + transactionUuid + '\'' +
                ", senderUuid='" + senderUuid + '\'' +
                ", receiverUuid='" + receiverUuid + '\'' +
                ", amount=" + amount +
                ", transactionStatus=" + transactionStatus +
                '}';
    }
}