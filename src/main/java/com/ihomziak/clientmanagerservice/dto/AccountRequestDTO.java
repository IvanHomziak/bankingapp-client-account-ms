package com.ihomziak.clientmanagerservice.dto;

import com.ihomziak.transactioncommon.utils.AccountType;
import lombok.Setter;
import lombok.Getter;

import java.util.Objects;

@Setter
@Getter
public class AccountRequestDTO {

    private String accountNumber;
    private AccountType accountType;
    private double balance;
    private String clientUUID;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AccountRequestDTO that = (AccountRequestDTO) o;
        return Double.compare(balance, that.balance) == 0 && Objects.equals(accountNumber, that.accountNumber) && accountType == that.accountType && Objects.equals(clientUUID, that.clientUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, accountType, balance, clientUUID);
    }

    @Override
    public String toString() {
        return "AccountRequestDTO{" +
                "accountNumber='" + accountNumber + '\'' +
                ", accountType=" + accountType +
                ", balance=" + balance +
                ", clientUUID='" + clientUUID + '\'' +
                '}';
    }
}
