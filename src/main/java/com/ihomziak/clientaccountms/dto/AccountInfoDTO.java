package com.ihomziak.clientaccountms.dto;

import java.math.BigDecimal;

import com.ihomziak.bankingapp.common.utils.AccountType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfoDTO {

    @NotBlank(message = "Account number must not be blank")
    private String accountNumber;

    @NotNull(message = "Account type must not be null")
    private AccountType accountType;

    private BigDecimal balance;

    @NotBlank(message = "Account UUID must not be blank")
    private String UUID;
}