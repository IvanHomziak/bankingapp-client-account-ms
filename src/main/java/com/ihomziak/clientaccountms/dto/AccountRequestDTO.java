package com.ihomziak.clientaccountms.dto;

import java.math.BigDecimal;

import com.ihomziak.bankingapp.common.utils.AccountType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDTO {

    private String accountNumber;
    private AccountType accountType;
    private BigDecimal balance;
    private String clientUUID;
}
