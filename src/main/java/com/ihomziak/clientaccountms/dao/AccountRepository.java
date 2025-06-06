package com.ihomziak.clientaccountms.dao;

import com.ihomziak.clientaccountms.entity.Account;
import com.ihomziak.bankingapp.common.utils.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findAccountByUUID(String uuid);

    List<Account> findAccountsByAccountTypeAndClientUUID(AccountType accountType, String clientUUD);

    List<Account> findAccountsByClientUUID(String clientUUID);

    @Procedure(procedureName = "TransferMoney")
    String transferMoney(
            @Param("senderId") String senderUuid,
            @Param("recipientId") String receiverUuid,
            @Param("amount") BigDecimal amount
    );
}
