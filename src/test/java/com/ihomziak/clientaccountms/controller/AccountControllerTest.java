package com.ihomziak.clientaccountms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ihomziak.bankingapp.common.utils.AccountType;
import com.ihomziak.clientaccountms.dto.AccountHolderDTO;
import com.ihomziak.clientaccountms.dto.AccountInfoDTO;
import com.ihomziak.clientaccountms.dto.AccountRequestDTO;
import com.ihomziak.clientaccountms.dto.AccountResponseDTO;
import com.ihomziak.clientaccountms.exception.AccountNotFoundException;
import com.ihomziak.clientaccountms.exceptionhandler.GlobalExceptionHandler;
import com.ihomziak.clientaccountms.service.AccountService;

import net.datafaker.Faker;
// Faker
@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private AccountRequestDTO accountRequestDTO;
    private AccountResponseDTO accountResponseDTO;
    private AccountInfoDTO accountInfoDTO;
    private String clientUuid;
    private Faker faker;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        faker = new Faker();
        clientUuid = faker.internet().uuid();
        String accountNumber = faker.finance().iban();

        accountInfoDTO = new AccountInfoDTO();
        accountInfoDTO.setAccountNumber(accountNumber);
        accountInfoDTO.setAccountType(AccountType.CHECKING);
        accountInfoDTO.setBalance(faker.number().numberBetween(1, 1000));
        accountInfoDTO.setUUID(clientUuid);

        accountRequestDTO = new AccountRequestDTO();
        accountRequestDTO.setAccountNumber(accountNumber);
        accountRequestDTO.setAccountType(AccountType.CHECKING);
        accountRequestDTO.setBalance(faker.number().numberBetween(1, 1000));
        accountRequestDTO.setClientUUID(clientUuid);

        AccountHolderDTO accountHolder = new AccountHolderDTO();
        accountHolder.setFirstName(faker.name().firstName());
        accountHolder.setLastName(faker.name().lastName());
        accountHolder.setUUID(clientUuid);

        accountResponseDTO = new AccountResponseDTO();
        accountResponseDTO.setAccountId(faker.number().numberBetween(1, 1000));
        accountResponseDTO.setAccountHolderDTO(accountHolder);
        accountResponseDTO.setAccountNumber(accountRequestDTO.getAccountNumber());
        accountResponseDTO.setAccountType(accountRequestDTO.getAccountType());
        accountResponseDTO.setBalance(accountRequestDTO.getBalance());
        accountResponseDTO.setUUID(clientUuid);
        accountResponseDTO.setCreatedAt(null);
        accountResponseDTO.setLastUpdated(null);
    }

    @Test
    public void getAccount_ShouldReturnAccount_WhenAccountExists() throws Exception {
        when(accountService.findAccountByUuid(clientUuid)).thenReturn(accountInfoDTO);

        mockMvc.perform(get("/api/account/{uuid}", clientUuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(content().json(objectMapper.writeValueAsString(accountInfoDTO)));
    }

    @Test
    public void getAccount_ShouldReturnNotFound_WhenAccountDoesNotExist() throws Exception {
        String uuid = faker.finance().iban();

        when(accountService.findAccountByUuid(uuid)).thenThrow(new AccountNotFoundException("Account not exist. UUID: " + uuid));

        mockMvc.perform(get("/api/account/{uuid}", uuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\":\"Account not exist. UUID: " + uuid + "\"}"));
    }

    @Test
    public void createCheckingAccount_ShouldReturnCreatedAccount() throws Exception {
        when(accountService.createCheckingAccount(any(AccountRequestDTO.class))).thenReturn(accountResponseDTO);

        mockMvc.perform(post("/api/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(accountResponseDTO)));
    }

    @Test
    public void deleteAccount_ShouldReturnAccepted_WhenAccountExists() throws Exception {
        when(accountService.deleteAccount(clientUuid)).thenReturn(accountInfoDTO);

        mockMvc.perform(delete("/api/account/{uuid}", clientUuid))
                .andExpect(status().isAccepted())
                .andExpect(content().json(objectMapper.writeValueAsString(accountInfoDTO)));
    }

    @Test
    public void deleteAccount_ShouldReturnNotFound_WhenAccountNotExists() throws Exception {
        when(accountService.deleteAccount(clientUuid)).thenThrow(new AccountNotFoundException("Account not exist. UUID: " + clientUuid));

        mockMvc.perform(delete("/api/account/{uuid}", clientUuid))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\":\"Account not exist. UUID: " + clientUuid + "\"}"));
    }

    @Test
    public void updateAccount_ShouldReturnUpdatedAccount() throws Exception {
        when(accountService.updateAccount(any(AccountRequestDTO.class))).thenReturn(accountResponseDTO);

        mockMvc.perform(patch("/api/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequestDTO)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(objectMapper.writeValueAsString(accountResponseDTO)));
    }

    @Test
    public void getAccounts_ShouldReturnListOfAccounts() throws Exception {
        List<AccountInfoDTO> accountInfoDTOList = new ArrayList<>();
        accountInfoDTOList.add(accountInfoDTO);
        accountInfoDTOList.add(accountInfoDTO);

        when(accountService.findAllAccounts()).thenReturn(accountInfoDTOList);

        mockMvc.perform(get("/api/account")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(content().json(objectMapper.writeValueAsString(accountInfoDTOList)));
    }

    @Test
    public void getAccounts_ShouldReturnEmptyList_WhenNoAccountsExist() throws Exception {
        when(accountService.findAllAccounts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/account")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(content().json("[]"));
    }

    @Test
    public void getClientAccounts_ShouldReturnListOfClientAccounts() throws Exception {
        List<AccountResponseDTO> clientAccounts = List.of(accountResponseDTO);

        when(accountService.findAllAccountsByClientUUID(clientUuid)).thenReturn(clientAccounts);

        mockMvc.perform(get("/api/account/list/{uuid}", clientUuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(content().json(objectMapper.writeValueAsString(clientAccounts)));
    }
}
