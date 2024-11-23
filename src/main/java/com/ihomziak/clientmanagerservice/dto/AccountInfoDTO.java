package com.ihomziak.clientmanagerservice.dto;

import com.ihomziak.transactioncommon.utils.AccountType;
import lombok.Setter;
import lombok.Getter;

import java.util.Objects;

@Setter
@Getter
public class AccountInfoDTO {

    private String accountNumber;
    private AccountType accountType;
    private double balance;
    private String UUID;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AccountInfoDTO that = (AccountInfoDTO) o;
        return Double.compare(balance, that.balance) == 0 && Objects.equals(accountNumber, that.accountNumber) && accountType == that.accountType && Objects.equals(UUID, that.UUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, accountType, balance, UUID);
    }

    @Override
    public String toString() {
        return "AccountInfoDTO{" +
                "accountNumber='" + accountNumber + '\'' +
                ", accountType=" + accountType +
                ", balance=" + balance +
                ", UUID='" + UUID + '\'' +
                '}';
    }
}
