package com.ihomziak.clientmanagerservice.dao;

import com.ihomziak.clientmanagerservice.entity.Account;
import com.ihomziak.bankingapp.common.utils.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findAccountByUUID(String uuid);

    Optional<Account> findAccountByAccountNumber(String accountNumber);

    List<Account> findAccountsByAccountType(AccountType accountType);

    List<Account> findAccountsByAccountTypeAndClientUUID(AccountType accountType, String clientUUD);

    List<Account> findAccountsByClientUUID(String clientUUID);

    @Procedure(procedureName = "TransferMoney")
    String transferMoney(
            @Param("senderId") String senderUuid,
            @Param("recipientId") String receiverUuid,
            @Param("amount") double amount
    );
}
