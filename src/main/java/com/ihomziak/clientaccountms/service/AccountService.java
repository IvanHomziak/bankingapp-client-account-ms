package com.ihomziak.clientaccountms.service;

import com.ihomziak.clientaccountms.dto.AccountInfoDTO;
import com.ihomziak.clientaccountms.dto.AccountRequestDTO;
import com.ihomziak.clientaccountms.dto.AccountResponseDTO;

import java.util.List;

public interface AccountService {

    AccountInfoDTO findAccountByUuid(String accountNumber);

    AccountResponseDTO createCheckingAccount(AccountRequestDTO account);

    AccountInfoDTO deleteAccount(String uuid);

    AccountResponseDTO updateAccount(AccountRequestDTO accountRequestDTO);

    List<AccountInfoDTO> findAllAccounts();

    List<AccountResponseDTO> findAllAccountsByClientUUID(String uuid);
}
