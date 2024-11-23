package com.ihomziak.clientmanagerservice.dto;

import com.ihomziak.transactioncommon.utils.AccountType;
import lombok.Setter;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AccountResponseDTO that = (AccountResponseDTO) o;
        return accountId == that.accountId && Double.compare(balance, that.balance) == 0 && Objects.equals(accountHolderDTO, that.accountHolderDTO) && Objects.equals(accountNumber, that.accountNumber) && accountType == that.accountType && Objects.equals(UUID, that.UUID) && Objects.equals(createdAt, that.createdAt) && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, accountHolderDTO, accountNumber, accountType, balance, UUID, createdAt, lastUpdated);
    }

    @Override
    public String toString() {
        return "AccountResponseDTO{" +
                "accountId=" + accountId +
                ", accountHolderDTO=" + accountHolderDTO +
                ", accountNumber='" + accountNumber + '\'' +
                ", accountType=" + accountType +
                ", balance=" + balance +
                ", UUID='" + UUID + '\'' +
                ", createdAt=" + createdAt +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
