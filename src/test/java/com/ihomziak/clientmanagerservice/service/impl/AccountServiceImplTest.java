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
    private ClientRequestDTO clientRequestDTO;
    private ClientResponseDTO clientResponseDTO;
    private AccountInfoDTO accountInfoDTO;
    private Client client;
    private Account account;

    private List<Account> accountList;
    private String clientUuid;
    private String accountNumber;

    @BeforeEach
    public void setup() {
        clientUuid = "206625ce-3ee7-4174-8f92-4bdc41c18274";
        accountNumber = "165445000023211234";

        accountRequestDTO = new AccountRequestDTO();
        accountRequestDTO.setAccountNumber(accountNumber);
        accountRequestDTO.setAccountType(AccountType.CHECKING);
        accountRequestDTO.setBalance(1000);
        accountRequestDTO.setClientUUID(clientUuid);

        AccountHolderDTO accountHolder = new AccountHolderDTO();
        accountHolder.setFirstName("John");
        accountHolder.setLastName("Doe");
        accountHolder.setUUID(clientUuid);

        accountResponseDTO = new AccountResponseDTO();
        accountResponseDTO.setAccountId(1);
        accountResponseDTO.setAccountHolderDTO(accountHolder);
        accountResponseDTO.setAccountNumber(accountRequestDTO.getAccountNumber());
        accountResponseDTO.setAccountType(accountRequestDTO.getAccountType());
        accountResponseDTO.setBalance(accountRequestDTO.getBalance());
        accountResponseDTO.setUUID(clientUuid);
        accountResponseDTO.setCreatedAt(null);
        accountResponseDTO.setLastUpdated(null);

        clientRequestDTO = new ClientRequestDTO();
        clientRequestDTO.setFirstName("John");
        clientRequestDTO.setLastName("Doe");
        clientRequestDTO.setTaxNumber("123456789");
        clientRequestDTO.setDateOfBirth("07/07/1992");
        clientRequestDTO.setEmail("john.doe@example.com");
        clientRequestDTO.setPhoneNumber("123-456-7890");
        clientRequestDTO.setAddress("123 Main St");


        account = new Account();
        account = mapper.accountRequestDtoToAccount(accountRequestDTO);

        client = new Client();
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setTaxNumber("123456789");
        client.setDateOfBirth("07/07/1992");
        client.setEmail("john.doe@example.com");
        client.setPhoneNumber("123-456-7890");
        client.setAddress("123 Main St");
        client.setUUID(clientUuid);
        client.setCreatedAt(null);
        client.setUpdatedAt(null);

        accountInfoDTO = new AccountInfoDTO();
        accountInfoDTO = mapper.accountToAccountInfoDto(account);

    }

    @Test
    public void testCreateCheckingAccount() {
        accountList = new ArrayList<>();
        accountList.add(account);

        when(clientRepository.findClientByUUID(accountRequestDTO.getClientUUID())).thenReturn(Optional.ofNullable(client));
        when(accountRepository.findAccountsByAccountTypeAndClientUUID(accountRequestDTO.getAccountType(), accountRequestDTO.getClientUUID())).thenReturn(accountList);
        when(mapper.accountRequestDtoToAccount(accountRequestDTO)).thenReturn(account);
        when(mapper.accountToAccountResponseDto(account)).thenReturn(accountResponseDTO);


        verify(clientRepository, times(1)).findClientByUUID(accountRequestDTO.getClientUUID());
        verify(accountRepository, times(1)).findAccountsByAccountTypeAndClientUUID(accountRequestDTO.getAccountType(), accountRequestDTO.getClientUUID());
    }

    @Test
    public void testCreateCheckingAccount_ClientNotFound() {
        when(clientRepository.findClientByUUID(accountRequestDTO.getClientUUID())).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> accountService.createCheckingAccount(accountRequestDTO));

        verify(clientRepository, times(1)).findClientByUUID(accountRequestDTO.getClientUUID());
        verify(accountRepository, never()).findAccountByAccountNumber(anyString());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void testCreateCheckingAccount_LimitOfBankAccountsExceeded_SameAccountType() {
        accountList = new ArrayList<>();
        accountList.add(account);
        accountList.add(account);
        accountList.add(account);

        when(clientRepository.findClientByUUID(accountRequestDTO.getClientUUID())).thenReturn(Optional.ofNullable(client));
        when(accountRepository.findAccountsByAccountTypeAndClientUUID(accountRequestDTO.getAccountType(), accountRequestDTO.getClientUUID())).thenReturn(accountList);

        assertThrows(AccountNumberQuantityException.class, () -> accountService.createCheckingAccount(accountRequestDTO));

        verify(accountRepository, times(1)).findAccountsByAccountType(accountRequestDTO.getAccountType());
    }
}