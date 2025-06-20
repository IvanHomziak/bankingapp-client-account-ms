package com.ihomziak.clientaccountms.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ihomziak.bankingapp.common.utils.AccountType;
import com.ihomziak.clientaccountms.exception.AccountDeletionConflictException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ihomziak.bankingapp.common.utils.TransactionStatus;
import com.ihomziak.clientaccountms.dao.AccountRepository;
import com.ihomziak.clientaccountms.dao.ClientRepository;
import com.ihomziak.clientaccountms.dto.AccountHolderDTO;
import com.ihomziak.clientaccountms.dto.AccountInfoDTO;
import com.ihomziak.clientaccountms.dto.AccountRequestDTO;
import com.ihomziak.clientaccountms.dto.AccountResponseDTO;
import com.ihomziak.clientaccountms.dto.TransactionEventRequestDTO;
import com.ihomziak.clientaccountms.entity.Account;
import com.ihomziak.clientaccountms.entity.Client;
import com.ihomziak.clientaccountms.exception.AccountNotFoundException;
import com.ihomziak.clientaccountms.exception.AccountNumberQuantityException;
import com.ihomziak.clientaccountms.exception.ClientNotFoundException;
import com.ihomziak.clientaccountms.mapper.MapStructMapper;
import com.ihomziak.clientaccountms.producer.TransactionEventProducer;
import com.ihomziak.clientaccountms.service.AccountService;
import com.ihomziak.clientaccountms.util.AccountNumberGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final MapStructMapper mapper;
    private final ObjectMapper objectMapper;
    private final TransactionEventProducer transactionEventProducer;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, ClientRepository clientRepository, MapStructMapper mapper, ObjectMapper objectMapper, TransactionEventProducer transactionEventProducer) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.mapper = mapper;
        this.objectMapper = objectMapper;
        this.transactionEventProducer = transactionEventProducer;
    }

    @Override
    @Transactional
    public AccountResponseDTO createCheckingAccount(AccountRequestDTO accountRequestDTO) {
        int maxAccountNumberOfCheckingType = 2;
        Optional<Client> client = this.clientRepository.findClientByUUID(accountRequestDTO.getAccountUuid());

        if (client.isEmpty()) {
            throw new ClientNotFoundException("Client not found");
        }

        List<Account> accounts = this.accountRepository.findAccountsByAccountTypeAndClientUUID(accountRequestDTO.getAccountType(), client.get().getUUID());

        if (accounts.size() == maxAccountNumberOfCheckingType) {
            throw new AccountNumberQuantityException("Client reach max account number of type: " + accountRequestDTO.getAccountType());
        }

        Account theAccount = Account.builder()
                .client(client.get())
                .accountNumber(AccountNumberGenerator.generateBankAccountNumber())
                .accountType(accountRequestDTO.getAccountType())
                .balance(accountRequestDTO.getBalance())
                .UUID(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .build();

        this.accountRepository.save(theAccount);

        AccountResponseDTO accountResponseDTO = mapper.accountToAccountResponseDto(theAccount);
        AccountHolderDTO accountHolderDTO = mapper.clientToAccountHolderDto(client);

        accountResponseDTO.setAccountHolderDTO(accountHolderDTO);

        return accountResponseDTO;
    }

    @Override
    @Transactional
    public AccountInfoDTO deleteAccount(String uuid) {
        Account account = accountRepository.findAccountByUUID(uuid)
                .orElseThrow(() -> new AccountNotFoundException("Account not exist: " + uuid));

        String clientUUID = account.getClient().getUUID();
        AccountType accountType = account.getAccountType();

        List<Account> clientAccounts = accountRepository.findAccountsByClientUUID(clientUUID);

        // Всі акаунти такого ж типу
        List<Account> sameTypeAccounts = clientAccounts.stream()
                .filter(acc -> acc.getAccountType().equals(accountType))
                .toList();

        if (sameTypeAccounts.size() == 1) {
            throw new AccountDeletionConflictException("Cannot delete the only account of type: " + accountType);
        }

        Account accountToTransferMoney = sameTypeAccounts.stream()
                .filter(ac -> !ac.getUUID().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No other account of this type to transfer balance"));

        // Переказ балансу
        accountToTransferMoney.setBalance(
                accountToTransferMoney.getBalance().add(account.getBalance())
        );

        // Зберігаємо новий баланс
        accountRepository.save(accountToTransferMoney);

        // Копіюємо DTO перед видаленням
        AccountInfoDTO dto = mapper.accountToAccountInfoDto(account);

        // Видаляємо акаунт
        accountRepository.delete(account);

        return dto;
    }


    @Override
    @Transactional
    public AccountResponseDTO updateAccount(AccountRequestDTO accountRequestDTO) {
        Optional<Account> account = this.accountRepository.findAccountByUUID(accountRequestDTO.getAccountUuid());
        if (account.isEmpty()) {
            throw new AccountNotFoundException("Account number " + accountRequestDTO.getAccountNumber() + " not exist: " + accountRequestDTO.getAccountUuid());
        }

        Account theAccount = account.get();
        Optional<Client> theClient = this.clientRepository.findClientByUUID(accountRequestDTO.getAccountUuid());
        if (theClient.isEmpty()) {
            throw new ClientNotFoundException("Client not found");
        }

        theAccount.setClient(theClient.get());
        theAccount.setAccountNumber(accountRequestDTO.getAccountNumber());
        theAccount.setAccountType(accountRequestDTO.getAccountType());
        theAccount.setBalance(accountRequestDTO.getBalance());
        theAccount.setLastUpdate(LocalDateTime.now());

        this.accountRepository.save(theAccount);
        return mapper.accountToAccountResponseDto(theAccount);
    }

    @Override
    public List<AccountInfoDTO> findAllAccounts() {
        List<Account> accountList = this.accountRepository.findAll();
        if (accountList.isEmpty()) {
            throw new AccountNotFoundException("Accounts not found exception");
        }

        return accountList.stream()
                .map(mapper::accountToAccountInfoDto)
                .toList();
    }

    @Override
    @Transactional
    public List<AccountResponseDTO> findAllAccountsByClientUUID(String uuid) {
        Optional<Client> client = this.clientRepository.findClientByUUID(uuid);
        if (client.isEmpty()) {
            throw new ClientNotFoundException("Client not found. UUID: " + uuid);
        }

        List<Account> accountList = this.accountRepository.findAccountsByClientUUID(uuid);
        List<AccountResponseDTO> accountResponseDTOList = new ArrayList<>();
        for (Account account : accountList) {
            accountResponseDTOList.add(mapper.accountToAccountResponseDto(account));
        }
        return accountResponseDTOList;
    }

    @Override
    public AccountInfoDTO findAccountByUuid(String uuid) {
        Optional<Account> account = this.accountRepository.findAccountByUUID(uuid);
        if (account.isEmpty()) {
            throw new AccountNotFoundException("Account not exist. UUID: " + uuid);
        }
        return mapper.accountToAccountInfoDto(account.get());
    }

    @Transactional
    public void processTransactionEvent(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {

        TransactionEventRequestDTO transactionEventRequestDTO = objectMapper.readValue(consumerRecord.value(), TransactionEventRequestDTO.class);

        Optional<Account> sender = accountRepository.findAccountByUUID(transactionEventRequestDTO.getSenderUuid());

        if (sender.isEmpty()) {
            this.transactionEventProducer.sendTransactionResponse(transactionEventRequestDTO, "Sender account not found", TransactionStatus.FAILED);
            return;
        }

        Optional<Account> receiver = accountRepository.findAccountByUUID(transactionEventRequestDTO.getReceiverUuid());

        if (receiver.isEmpty()) {
            this.transactionEventProducer.sendTransactionResponse(transactionEventRequestDTO, "Receiver account not found", TransactionStatus.FAILED);
            return;
        }

        Account senderAccount = sender.get();
        Account receiverAccount = receiver.get();

        if (senderAccount.getBalance().compareTo(transactionEventRequestDTO.getAmount()) < 0) {
            this.transactionEventProducer.sendTransactionResponse(transactionEventRequestDTO, "Insufficient funds", TransactionStatus.FAILED);
            return;
        }

        senderAccount.setBalance(senderAccount.getBalance().subtract(transactionEventRequestDTO.getAmount()));
        receiverAccount.setBalance(receiverAccount.getBalance().add(transactionEventRequestDTO.getAmount()));

        // написати сторед процедуру для цього методу. почитати про сторед процедури та вьюшки. до бази потрібно звертатись не 4 рази а один
        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        this.transactionEventProducer.sendTransactionResponse(transactionEventRequestDTO, null, TransactionStatus.COMPLETED);
    }

    @Transactional
    public void processTransactionEvent2(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        TransactionEventRequestDTO transactionEventRequestDTO = objectMapper.readValue(consumerRecord.value(), TransactionEventRequestDTO.class);

        String result = this.accountRepository.transferMoney(
                transactionEventRequestDTO.getSenderUuid(),
                transactionEventRequestDTO.getReceiverUuid(),
                transactionEventRequestDTO.getAmount()
        );

        if (TransactionStatus.FAILED.toString().equals(result)) {
            this.transactionEventProducer.sendTransactionResponse(transactionEventRequestDTO, "message", TransactionStatus.FAILED);

        } else if (TransactionStatus.COMPLETED.toString().equals(result)) {
            this.transactionEventProducer.sendTransactionResponse(transactionEventRequestDTO, "message", TransactionStatus.COMPLETED);
        }

    }
}
