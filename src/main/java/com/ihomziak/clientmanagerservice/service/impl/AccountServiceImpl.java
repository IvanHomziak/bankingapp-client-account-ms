package com.ihomziak.clientmanagerservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ihomziak.clientmanagerservice.dao.AccountRepository;
import com.ihomziak.clientmanagerservice.dao.ClientRepository;
import com.ihomziak.clientmanagerservice.dto.*;
import com.ihomziak.clientmanagerservice.dto.TransactionRequestDTO;
import com.ihomziak.clientmanagerservice.enums.TransactionStatus;
import com.ihomziak.clientmanagerservice.producer.TransactionEventProducer;
import com.ihomziak.clientmanagerservice.service.AccountService;
import com.ihomziak.clientmanagerservice.entity.Account;
import com.ihomziak.clientmanagerservice.entity.Client;
import com.ihomziak.clientmanagerservice.exception.AccountNotFoundException;
import com.ihomziak.clientmanagerservice.exception.AccountNumberQuantityException;
import com.ihomziak.clientmanagerservice.exception.ClientNotFoundException;
import com.ihomziak.clientmanagerservice.mapper.MapStructMapper;
import com.ihomziak.clientmanagerservice.util.AccountNumberGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public AccountResponseDTO createCheckingAccount(AccountRequestDTO accountRequestDTO) {
        int maxAccountNumberOfCheckingType = 2;
        Optional<Client> client = this.clientRepository.findClientByUUID(accountRequestDTO.getClientUUID());

        if (client.isEmpty()) {
            throw new ClientNotFoundException("Client not found");
        }

        List<Account> accountNumberSize = this.accountRepository.findAccountsByAccountTypeAndClientUUID(accountRequestDTO.getAccountType(), client.get().getUUID());

        Account theAccount = mapper.accountRequestDtoToAccount(accountRequestDTO);

        if (accountNumberSize.size() >= maxAccountNumberOfCheckingType) {
            throw new AccountNumberQuantityException("Client reach max account number of type: " + accountRequestDTO.getAccountType());
        } else {
            theAccount.setAccountNumber(AccountNumberGenerator.generateBankAccountNumber());
        }

        theAccount.setClient(client.get());
        theAccount.setCreatedAt(LocalDateTime.now());

        this.accountRepository.save(theAccount);

        AccountResponseDTO accountResponseDTO = mapper.accountToAccountResponseDto(theAccount);
        AccountHolderDTO accountHolderDTO = mapper.clientToAccountHolderDto(client);

        accountResponseDTO.setAccountHolderDTO(accountHolderDTO);

        return accountResponseDTO;
    }

    @Override
    public AccountInfoDTO deleteAccount(String uuid) {
        Optional<Account> account = this.accountRepository.findAccountByUUID(uuid);

        if (account.isEmpty()) {
            throw new AccountNotFoundException("Account not exist: " + uuid);
        }
        this.accountRepository.delete(account.get());
        return mapper.accountToAccountInfoDto(account.get());
    }

    @Override
    public AccountResponseDTO updateAccount(AccountRequestDTO accountRequestDTO) {
        Optional<Account> account = this.accountRepository.findAccountByUUID(accountRequestDTO.getClientUUID());
        if (account.isEmpty()) {
            throw new AccountNotFoundException("Account number " + accountRequestDTO.getAccountNumber() + " not exist: " + accountRequestDTO.getClientUUID());
        }

        Account theAccount = account.get();
        Optional<Client> theClient = this.clientRepository.findClientByUUID(accountRequestDTO.getClientUUID());
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
        List<AccountInfoDTO> accountInfoDTOList = new ArrayList<>();
        for (Account account : accountList) {
            accountInfoDTOList.add(mapper.accountToAccountInfoDto(account));
        }
        return accountInfoDTOList;
    }

    @Override
    public List<AccountResponseDTO> findAllAccountsByClientUUID(String uuid) {
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

    public void processTransactionEvent(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {

        TransactionRequestDTO transactionRequestDTO = objectMapper.readValue(consumerRecord.value(), TransactionRequestDTO.class);

        Optional<Account> sender = accountRepository.findAccountByUUID(transactionRequestDTO.getSenderUuid());

        if (sender.isEmpty()) {
            this.transactionEventProducer.sendTransactionResponse(transactionRequestDTO, "Sender account not found", TransactionStatus.FAILED, null, null);
            return;
        }

        Optional<Account> receiver = accountRepository.findAccountByUUID(transactionRequestDTO.getReceiverUuid());

        if (receiver.isEmpty()) {
            this.transactionEventProducer.sendTransactionResponse(transactionRequestDTO, "Receiver account not found", TransactionStatus.FAILED, null, null);
            return;
        }

        Account senderAccount = sender.get();
        Account receiverAccount = receiver.get();

        if (senderAccount.getBalance() < transactionRequestDTO.getAmount()) {
            this.transactionEventProducer.sendTransactionResponse(transactionRequestDTO, "Insufficient funds", TransactionStatus.FAILED, null, null);
            return;
        }

        senderAccount.setBalance(senderAccount.getBalance() - transactionRequestDTO.getAmount());
        receiverAccount.setBalance(receiverAccount.getBalance() + transactionRequestDTO.getAmount());

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        this.transactionEventProducer.sendTransactionResponse(transactionRequestDTO, null, TransactionStatus.COMPLETED, senderAccount.getBalance(), receiverAccount.getBalance());
    }
}
