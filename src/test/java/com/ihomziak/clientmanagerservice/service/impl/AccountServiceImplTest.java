package com.ihomziak.clientmanagerservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ihomziak.clientmanagerservice.dao.AccountRepository;
import com.ihomziak.clientmanagerservice.dao.ClientRepository;
import com.ihomziak.clientmanagerservice.dto.*;
import com.ihomziak.clientmanagerservice.entity.Account;
import com.ihomziak.clientmanagerservice.entity.Client;
import com.ihomziak.clientmanagerservice.exception.*;
import com.ihomziak.clientmanagerservice.mapper.MapStructMapper;
import com.ihomziak.clientmanagerservice.producer.TransactionEventProducer;
import com.ihomziak.clientmanagerservice.util.AccountNumberGenerator;
import com.ihomziak.transactioncommon.utils.AccountType;
import com.ihomziak.transactioncommon.utils.TransactionStatus;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private MapStructMapper mapper;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private TransactionEventProducer transactionEventProducer;

    @InjectMocks
    private AccountServiceImpl accountService;

    private AccountRequestDTO accountRequestDTO;
    private AccountResponseDTO accountResponseDTO;
    private Client client;
    private Account account;

    @BeforeEach
    void setup() {
        accountRequestDTO = new AccountRequestDTO();
        accountRequestDTO.setClientUUID("client-uuid");
        accountRequestDTO.setAccountNumber("123456789");
        accountRequestDTO.setAccountType(AccountType.CHECKING);
        accountRequestDTO.setBalance(1000.0);

        accountResponseDTO = new AccountResponseDTO();
        accountResponseDTO.setUUID("client-uuid");
        accountResponseDTO.setAccountType(AccountType.CHECKING);

        client = new Client();
        client.setUUID("client-uuid");

        account = new Account();
        account.setUUID("client-uuid");
        account.setAccountNumber("123456789");
        account.setAccountType(AccountType.CHECKING);
    }

    @Test
    void createCheckingAccount_Success() {
        List<Account> accounts = new ArrayList<>();

        when(clientRepository.findClientByUUID(anyString())).thenReturn(Optional.of(client));
        when(accountRepository.findAccountsByAccountTypeAndClientUUID(any(), anyString())).thenReturn(accounts);
        when(mapper.accountRequestDtoToAccount(any())).thenReturn(account);
        when(mapper.accountToAccountResponseDto(any())).thenReturn(accountResponseDTO);

        AccountResponseDTO response = accountService.createCheckingAccount(accountRequestDTO);

        assertNotNull(response);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void createCheckingAccount_ClientNotFound() {
        when(clientRepository.findClientByUUID(anyString())).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> accountService.createCheckingAccount(accountRequestDTO));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void createCheckingAccount_MaxAccountLimitReached() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        accounts.add(account);

        when(clientRepository.findClientByUUID(anyString())).thenReturn(Optional.of(client));
        when(accountRepository.findAccountsByAccountTypeAndClientUUID(any(), anyString())).thenReturn(accounts);

        assertThrows(AccountNumberQuantityException.class, () -> accountService.createCheckingAccount(accountRequestDTO));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void deleteAccount_Success() {
        when(accountRepository.findAccountByUUID(anyString())).thenReturn(Optional.of(account));
        when(mapper.accountToAccountInfoDto(any())).thenReturn(new AccountInfoDTO());

        AccountInfoDTO response = accountService.deleteAccount("account-uuid");

        assertNotNull(response);
        verify(accountRepository, times(1)).delete(any(Account.class));
    }

    @Test
    void deleteAccount_AccountNotFound() {
        when(accountRepository.findAccountByUUID(anyString())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccount("account-uuid"));
        verify(accountRepository, never()).delete(any());
    }

    @Test
    void updateAccount_Success() {
        when(accountRepository.findAccountByUUID(anyString())).thenReturn(Optional.of(account));
        when(clientRepository.findClientByUUID(anyString())).thenReturn(Optional.of(client));
        when(mapper.accountToAccountResponseDto(any())).thenReturn(accountResponseDTO);

        AccountResponseDTO response = accountService.updateAccount(accountRequestDTO);

        assertNotNull(response);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void updateAccount_AccountNotFound() {
        when(accountRepository.findAccountByUUID(anyString())).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.updateAccount(accountRequestDTO));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void processTransactionEvent_Success() throws JsonProcessingException {
        // Mock the consumer record
        ConsumerRecord<Integer, String> consumerRecord = mock(ConsumerRecord.class);
        when(consumerRecord.value()).thenReturn("{\"senderUuid\":\"sender-uuid\",\"receiverUuid\":\"receiver-uuid\",\"amount\":500.0}");

        // Mock the deserialized DTO
        TransactionEventRequestDTO transactionEventRequestDTO = new TransactionEventRequestDTO();
        transactionEventRequestDTO.setSenderUuid("sender-uuid");
        transactionEventRequestDTO.setReceiverUuid("receiver-uuid");
        transactionEventRequestDTO.setAmount(500.0);
        when(objectMapper.readValue(anyString(), eq(TransactionEventRequestDTO.class))).thenReturn(transactionEventRequestDTO);

        // Mock the accounts
        Account senderAccount = new Account();
        senderAccount.setUUID("sender-uuid");
        senderAccount.setBalance(1000.0);

        Account receiverAccount = new Account();
        receiverAccount.setUUID("receiver-uuid");
        receiverAccount.setBalance(200.0);

        when(accountRepository.findAccountByUUID("sender-uuid")).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findAccountByUUID("receiver-uuid")).thenReturn(Optional.of(receiverAccount));

        // Execute the method under test
        accountService.processTransactionEvent(consumerRecord);

        // Verify behavior
        verify(accountRepository, times(2)).save(any(Account.class)); // Once for sender, once for receiver
        verify(transactionEventProducer, times(1)).sendTransactionResponse(
                eq(transactionEventRequestDTO),
                eq(null),
                eq(TransactionStatus.COMPLETED)
        );

        // Assert updated balances
        assertEquals(500.0, senderAccount.getBalance());
        assertEquals(700.0, receiverAccount.getBalance());
    }


    @Test
    void processTransactionEvent_InsufficientFunds() throws JsonProcessingException {
        // Mock the ConsumerRecord
        ConsumerRecord<Integer, String> consumerRecord = mock(ConsumerRecord.class);
        when(consumerRecord.value()).thenReturn("{\"senderUuid\":\"sender-uuid\",\"receiverUuid\":\"receiver-uuid\",\"amount\":2000.0}");

        // Mock the deserialized DTO
        TransactionEventRequestDTO dto = new TransactionEventRequestDTO();
        dto.setSenderUuid("sender-uuid");
        dto.setReceiverUuid("receiver-uuid");
        dto.setAmount(2000.0);

        when(objectMapper.readValue(anyString(), eq(TransactionEventRequestDTO.class))).thenReturn(dto);

        // Mock sender account with insufficient funds
        Account senderAccount = new Account();
        senderAccount.setUUID("sender-uuid");
        senderAccount.setBalance(1000.0);

        Account receiverAccount = new Account();
        receiverAccount.setUUID("receiver-uuid");
        receiverAccount.setBalance(200.0);

        when(accountRepository.findAccountByUUID("sender-uuid")).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findAccountByUUID("receiver-uuid")).thenReturn(Optional.of(receiverAccount));

        // Execute the method under test
        accountService.processTransactionEvent(consumerRecord);

        // Verify the transaction response was sent with FAILED status
        verify(transactionEventProducer, times(1)).sendTransactionResponse(
                eq(dto),
                eq("Insufficient funds"),
                eq(TransactionStatus.FAILED)
        );

        // Verify no accounts were saved
        verify(accountRepository, never()).save(any(Account.class));
    }
}
