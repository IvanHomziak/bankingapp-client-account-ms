package com.ihomziak.clientaccountms.dto;

import java.math.BigDecimal;

import com.ihomziak.bankingapp.common.utils.TransactionStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEventRequestDTO {

    @NotBlank(message = "Transaction UUID must not be blank")
    private String transactionUuid;

    @NotBlank(message = "Sender UUID must not be blank")
    private String senderUuid;

    @NotBlank(message = "Receiver UUID must not be blank")
    private String receiverUuid;

    @NotNull(message = "Amount must not be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Transaction status must not be null")
    private TransactionStatus transactionStatus;
}
