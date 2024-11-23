package com.ihomziak.clientmanagerservice.dto;

import com.ihomziak.transactioncommon.utils.AccountType;
import lombok.Setter;
import lombok.Getter;

import java.time.LocalDateTime;

@Setter
@Getter
public class AccountResponseDTO {

    private long accountId;
    private AccountHolderDTO accountHolderDTO;
    private String accountNumber;
    private AccountType accountType;
    private double balance;
    private String UUID;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;
}
