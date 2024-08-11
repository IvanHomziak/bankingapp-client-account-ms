package com.ihomziak.webbankingapp.service;

import com.ihomziak.webbankingapp.dto.AccountInfoDTO;
import com.ihomziak.webbankingapp.dto.AccountRequestDTO;
import com.ihomziak.webbankingapp.dto.AccountResponseDTO;

import java.util.List;

public interface AccountService {

    AccountInfoDTO findAccountByUuid(String accountNumber);

    AccountResponseDTO createCheckingAccount(AccountRequestDTO account);

    AccountInfoDTO deleteAccount(String uuid);

    AccountResponseDTO updateAccount(AccountRequestDTO accountRequestDTO);

    List<AccountInfoDTO> findAllAccounts();

    List<AccountResponseDTO> findAllAccountsByClientUUID(String uuid);
}
