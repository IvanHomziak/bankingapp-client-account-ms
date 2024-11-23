package com.ihomziak.clientmanagerservice.service.impl;

import com.ihomziak.clientmanagerservice.dao.AccountRepository;
import com.ihomziak.clientmanagerservice.dao.ClientRepository;
import com.ihomziak.clientmanagerservice.dto.*;
import com.ihomziak.clientmanagerservice.entity.Account;
import com.ihomziak.clientmanagerservice.entity.Client;
import com.ihomziak.clientmanagerservice.exception.AccountNumberQuantityException;
import com.ihomziak.clientmanagerservice.mapper.MapStructMapper;
import com.ihomziak.transactioncommon.utils.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ihomziak.clientmanagerservice.exception.ClientNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private MapStructMapper mapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    private AccountRequestDTO accountRequestDTO;
    private AccountResponseDTO accountResponseDTO;
    private Client client;
    private Account account;
    private List<Account> accountList;
    private String clientUuid;
    private String accountNumber;

    @BeforeEach
    public void setup() {
        clientUuid = "206625ce-3ee7-4174-8f92-4bdc41c18274";
        accountNumber = "165445000023211234";

        // Mock AccountRequestDTO
        accountRequestDTO = new AccountRequestDTO();
        accountRequestDTO.setAccountNumber(accountNumber);
        accountRequestDTO.setAccountType(AccountType.CHECKING);
        accountRequestDTO.setBalance(1000);
        accountRequestDTO.setClientUUID(clientUuid);

        // Mock AccountResponseDTO
        accountResponseDTO = new AccountResponseDTO();
        accountResponseDTO.setAccountId(1);
        accountResponseDTO.setAccountNumber(accountRequestDTO.getAccountNumber());
        accountResponseDTO.setAccountType(accountRequestDTO.getAccountType());
        accountResponseDTO.setBalance(accountRequestDTO.getBalance());
        accountResponseDTO.setUUID(clientUuid);

        // Mock Client
        client = new Client();
        client.setUUID(clientUuid);

        // Mock Account
        account = new Account();
        account.setUUID(clientUuid);
        account.setAccountNumber(accountNumber);
        account.setAccountType(AccountType.CHECKING);

        accountList = new ArrayList<>();
    }

    @Test
    public void testCreateCheckingAccount() {
        // Mock dependencies
        accountList.add(account);
        when(clientRepository.findClientByUUID(clientUuid)).thenReturn(Optional.of(client));
        when(accountRepository.findAccountsByAccountTypeAndClientUUID(AccountType.CHECKING, clientUuid)).thenReturn(accountList);
        when(mapper.accountRequestDtoToAccount(accountRequestDTO)).thenReturn(account);
        when(mapper.accountToAccountResponseDto(account)).thenReturn(accountResponseDTO);

        // Call method under test
        AccountResponseDTO response = accountService.createCheckingAccount(accountRequestDTO);

        // Assertions
        assertNotNull(response);
        assertEquals(accountResponseDTO, response);

        // Verify interactions
        verify(clientRepository, times(1)).findClientByUUID(clientUuid);
        verify(accountRepository, times(1)).findAccountsByAccountTypeAndClientUUID(AccountType.CHECKING, clientUuid);
        verify(mapper, times(1)).accountRequestDtoToAccount(accountRequestDTO);
        verify(mapper, times(1)).accountToAccountResponseDto(account);
    }

    @Test
    public void testCreateCheckingAccount_ClientNotFound() {
        // Mock client not found
        when(clientRepository.findClientByUUID(clientUuid)).thenReturn(Optional.empty());

        // Call method and assert exception
        assertThrows(ClientNotFoundException.class, () -> accountService.createCheckingAccount(accountRequestDTO));

        // Verify interactions
        verify(clientRepository, times(1)).findClientByUUID(clientUuid);
        verify(accountRepository, never()).findAccountsByAccountTypeAndClientUUID(any(), any());
    }

    @Test
    public void testCreateCheckingAccount_LimitOfBankAccountsExceeded_SameAccountType() {
        // Mock max accounts of the same type
        accountList.add(account);
        accountList.add(account);
        accountList.add(account); // Exceeds limit
        when(clientRepository.findClientByUUID(clientUuid)).thenReturn(Optional.of(client));
        when(accountRepository.findAccountsByAccountTypeAndClientUUID(AccountType.CHECKING, clientUuid)).thenReturn(accountList);

        // Call method and assert exception
        assertThrows(AccountNumberQuantityException.class, () -> accountService.createCheckingAccount(accountRequestDTO));

        // Verify interactions
        verify(clientRepository, times(1)).findClientByUUID(clientUuid);
        verify(accountRepository, times(1)).findAccountsByAccountTypeAndClientUUID(AccountType.CHECKING, clientUuid);
        verify(accountRepository, never()).save(any(Account.class));
    }
}