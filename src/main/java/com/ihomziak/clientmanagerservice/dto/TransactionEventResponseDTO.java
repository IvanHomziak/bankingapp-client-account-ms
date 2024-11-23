package com.ihomziak.clientmanagerservice.dto;

import com.ihomziak.transactioncommon.utils.TransactionStatus;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEventResponseDTO {
    private String transactionUuid;
    private TransactionStatus transactionStatus;
    private String statusMessage;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TransactionEventResponseDTO that = (TransactionEventResponseDTO) o;
        return Objects.equals(transactionUuid, that.transactionUuid) && transactionStatus == that.transactionStatus && Objects.equals(statusMessage, that.statusMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionUuid, transactionStatus, statusMessage);
    }

    @Override
    public String toString() {
        return "TransactionEventResponseDTO{" +
                "transactionUuid='" + transactionUuid + '\'' +
                ", transactionStatus=" + transactionStatus +
                ", statusMessage='" + statusMessage + '\'' +
                '}';
    }
}