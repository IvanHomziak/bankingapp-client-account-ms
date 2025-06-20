package com.ihomziak.clientaccountms.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ihomziak.bankingapp.common.dto.ErrorDTO;
import com.ihomziak.bankingapp.common.utils.AccountType;
import com.ihomziak.clientaccountms.dto.AccountHolderDTO;
import com.ihomziak.clientaccountms.dto.AccountInfoDTO;
import com.ihomziak.clientaccountms.dto.AccountRequestDTO;
import com.ihomziak.clientaccountms.dto.AccountResponseDTO;
import com.ihomziak.clientaccountms.exception.AccountDeletionConflictException;
import com.ihomziak.clientaccountms.exception.AccountNotFoundException;
import com.ihomziak.clientaccountms.exception.ClientNotFoundException;
import com.ihomziak.clientaccountms.exceptionhandler.GlobalExceptionHandler;
import com.ihomziak.clientaccountms.service.AccountService;
import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.PathMatcher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static com.ihomziak.clientaccountms.util.constants.Endpoints.AccountEndpoints.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountController.class)
@Import({
        AccountControllerTest.NoSecurityConfig.class, // 👈 disables Spring Security
        GlobalExceptionHandler.class})
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    private Faker faker;

    private ObjectMapper objectMapper;
    private AccountRequestDTO accountRequestDTO;
    private AccountHolderDTO accountHolder;
    private AccountInfoDTO accountInfoDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        faker = new Faker();

        accountRequestDTO = new AccountRequestDTO();
        accountRequestDTO.setAccountNumber(faker.finance().iban());
        accountRequestDTO.setAccountType(AccountType.CHECKING);
        accountRequestDTO.setBalance(BigDecimal.valueOf(faker.number().numberBetween(100, 10000)));
        accountRequestDTO.setAccountUuid(faker.internet().uuid());

        accountHolder = new AccountHolderDTO();
        accountHolder.setUUID(accountRequestDTO.getAccountUuid());
        accountHolder.setFirstName(faker.name().firstName());
        accountHolder.setLastName(faker.name().lastName());

        accountInfoDTO = new AccountInfoDTO();
        accountInfoDTO.setAccountNumber(accountRequestDTO.getAccountNumber());

    }

    @Test
    void createCheckingAccount_whenClientExist_ShouldReturnCreatedAccount() throws Exception {
        // Given
        AccountResponseDTO expectedResponse = new ModelMapper().map(accountRequestDTO, AccountResponseDTO.class);
        expectedResponse.setAccountHolderDTO(accountHolder);
        expectedResponse.setCreatedAt(LocalDateTime.now());
        expectedResponse.setLastUpdated(LocalDateTime.now());

        when(accountService.createCheckingAccount(any(AccountRequestDTO.class)))
                .thenReturn(expectedResponse);

        // When
        MvcResult result = mockMvc.perform(post(API_ACCOUNT_V1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequestDTO)))
                .andReturn();

        // Then
        String responseBody = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        assertEquals(HttpStatus.CREATED.value(), status, "Incorrect response status");
        assertFalse(responseBody.isBlank());

        AccountResponseDTO actual = objectMapper.readValue(responseBody, AccountResponseDTO.class);

        assertEquals(expectedResponse.getAccountNumber(), actual.getAccountNumber(), "Account number mismatch");
        assertEquals(expectedResponse.getAccountType(), actual.getAccountType(), "Account type mismatch");
        assertEquals(expectedResponse.getBalance(), actual.getBalance(), "Account balance mismatch");
        assertEquals(expectedResponse.getUUID(), actual.getUUID(), "Client uuid mismatch");
        assertNotNull(actual.getCreatedAt(), "Created account should be not null");
        assertNotNull(actual.getLastUpdated(), "Last updated account should be not null");
        assertEquals(accountHolder.getFirstName(), actual.getAccountHolderDTO().getFirstName(), "First name mismatch");
        assertEquals(accountHolder.getLastName(), actual.getAccountHolderDTO().getLastName(), "Last name mismatch");
        assertEquals(accountHolder.getUUID(), actual.getAccountHolderDTO().getUUID(), "Client uuid mismatch");
    }

    @Test
    void createCheckingAccount_whenClientNotExist_ShouldReturnClientNotFoundException() throws Exception {
        // Arrange
        accountRequestDTO.setAccountUuid(UUID.randomUUID().toString());
        String errorMessage = "Client not found: " + accountRequestDTO.getAccountUuid();
        when(accountService.createCheckingAccount(any(AccountRequestDTO.class)))
                .thenThrow(new ClientNotFoundException(errorMessage));

        // Act + Assert
        mockMvc.perform(post(API_ACCOUNT_V1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().json("""
                        {
                            "error": "%s"
                        }
                        """.formatted(errorMessage)));
    }

    @Test
    void getAccount_whenClientExist_ShouldReturnCreatedAccount() throws Exception {
        // Arrange
        AccountRequestDTO accountRequestDTO = new AccountRequestDTO();
        accountRequestDTO.setAccountNumber(faker.finance().iban());
        accountRequestDTO.setAccountType(AccountType.CHECKING);
        accountRequestDTO.setBalance(BigDecimal.valueOf(faker.number().numberBetween(10000, 10000)));
        accountRequestDTO.setAccountUuid(faker.internet().uuid());

        accountRequestDTO.setAccountUuid(UUID.randomUUID().toString());
        AccountInfoDTO expectedResponse = new AccountInfoDTO();
        expectedResponse.setAccountNumber(accountRequestDTO.getAccountNumber());
        expectedResponse.setBalance(accountRequestDTO.getBalance());
        expectedResponse.setUUID(UUID.randomUUID().toString());
        expectedResponse.setAccountType(accountRequestDTO.getAccountType());
        when(accountService.findAccountByUuid(accountRequestDTO.getAccountUuid()))
                .thenReturn(expectedResponse);

        // Act
        MvcResult result = mockMvc.perform(get(API_ACCOUNT_V1 + GET_ACCOUNT, accountRequestDTO.getAccountUuid())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        // Assert
        String responseBody = result.getResponse().getContentAsString();
        AccountInfoDTO actual = objectMapper.readValue(responseBody, AccountInfoDTO.class);
        Assertions.assertEquals(expectedResponse.getAccountNumber(), actual.getAccountNumber(), "Account number mismatch");
        Assertions.assertEquals(expectedResponse.getAccountType(), actual.getAccountType(), "Account type mismatch");
        Assertions.assertEquals(expectedResponse.getBalance(), actual.getBalance(), "Account balance mismatch");
        Assertions.assertEquals(expectedResponse.getUUID(), actual.getUUID(), "Account uuid mismatch");
    }

    @Test
    void getAccount_whenClientNotExist_ShouldReturnClientNotFoundException() throws Exception {
        // Arrange
        String uuid = UUID.randomUUID().toString();
        String errorMessage = "Account not exist. UUID: " + uuid;
        when(accountService.findAccountByUuid(uuid))
                .thenThrow(new AccountNotFoundException(errorMessage));
        // Act
        MvcResult result = mockMvc.perform(get(API_ACCOUNT_V1 + GET_ACCOUNT, uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        int statusCode = result.getResponse().getStatus();
        ErrorDTO error = objectMapper.readValue(responseBody, ErrorDTO.class);

        // Assert
        Assertions.assertEquals(errorMessage, error.getError(), "Account error description mismatch");
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), statusCode, "Account not found status code mismatch");
    }

    @Test
    void getClientAccounts_whenClientExist_ShouldReturnClientAccounts() throws Exception {
        String clientUuid = UUID.randomUUID().toString();
        // Arrange
        AccountRequestDTO accountRequestDTO = new AccountRequestDTO();
        accountRequestDTO.setAccountNumber(faker.finance().iban());
        accountRequestDTO.setAccountType(AccountType.CHECKING);
        accountRequestDTO.setBalance(BigDecimal.valueOf(faker.number().numberBetween(10000, 10000)));
        accountRequestDTO.setAccountUuid(faker.internet().uuid());

        accountRequestDTO.setAccountUuid(UUID.randomUUID().toString());
        AccountInfoDTO expectedResponse = new AccountInfoDTO();
        expectedResponse.setAccountNumber(accountRequestDTO.getAccountNumber());
        expectedResponse.setBalance(accountRequestDTO.getBalance());
        expectedResponse.setUUID(UUID.randomUUID().toString());
        expectedResponse.setAccountType(accountRequestDTO.getAccountType());

        String account1 = UUID.randomUUID().toString();
        String account2 = UUID.randomUUID().toString();
        AccountResponseDTO accountResponseDTO1 = new AccountResponseDTO();
        accountResponseDTO1.setAccountNumber(faker.finance().iban());
        accountResponseDTO1.setUUID(account1);
        accountResponseDTO1.setAccountType(AccountType.CHECKING);
        accountResponseDTO1.setBalance(BigDecimal.valueOf(faker.number().numberBetween(1000, 2000)));
        accountResponseDTO1.setCreatedAt(LocalDateTime.now().minusDays(3));
        accountResponseDTO1.setLastUpdated(LocalDateTime.now());

        AccountResponseDTO accountResponseDTO2 = new AccountResponseDTO();
        accountResponseDTO2.setAccountNumber(faker.finance().iban());
        accountResponseDTO2.setUUID(account2);
        accountResponseDTO2.setAccountType(AccountType.CHECKING);
        accountResponseDTO2.setBalance(BigDecimal.valueOf(faker.number().numberBetween(1000, 2000)));
        accountResponseDTO2.setCreatedAt(LocalDateTime.now().minusDays(3));
        accountResponseDTO2.setLastUpdated(LocalDateTime.now());

        List<AccountResponseDTO> accountResponseDTOList = List.of(accountResponseDTO1, accountResponseDTO2);

        when(accountService.findAllAccountsByClientUUID(clientUuid))
                .thenReturn(accountResponseDTOList);

        // Act
        MvcResult result = mockMvc.perform(get(API_ACCOUNT_V1 + GET_CLIENT_ACCOUNTS, clientUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        int statusCode = result.getResponse().getStatus();
        List<AccountResponseDTO> actualList = objectMapper.readValue(
                responseBody,
                new TypeReference<List<AccountResponseDTO>>() {
                }
        );

        // Assert
        Assertions.assertEquals(HttpStatus.FOUND.value(), statusCode, "Account not found status code mismatch");
        Assertions.assertEquals(accountResponseDTOList.size(), actualList.size(), "Account response list mismatch");
        IntStream.range(0, actualList.size())
                .forEach(i -> {
                    Assertions.assertEquals(accountResponseDTOList.get(i).getAccountNumber(), actualList.get(i).getAccountNumber(), "Account mismatch");
                    Assertions.assertEquals(accountResponseDTOList.get(i).getAccountType(), actualList.get(i).getAccountType(), "Account type mismatch");
                    Assertions.assertEquals(accountResponseDTOList.get(i).getBalance(), actualList.get(i).getBalance(), "Account balance mismatch");
                    Assertions.assertEquals(accountResponseDTOList.get(i).getUUID(), actualList.get(i).getUUID(), "Account uuid mismatch");
                    Assertions.assertEquals(accountResponseDTOList.get(i).getCreatedAt(), actualList.get(i).getCreatedAt(), "Account created at mismatch");
                    Assertions.assertEquals(accountResponseDTOList.get(i).getLastUpdated(), actualList.get(i).getLastUpdated(), "Account last updated mismatch");
                    Assertions.assertEquals(accountResponseDTOList.get(i).getAccountHolderDTO(), actualList.get(i).getAccountHolderDTO(), "Account holder mismatch");
                });
    }

    @Test
    void getClientAccounts_whenClientNotExist_ShouldReturnAccountNotFoundException() throws Exception {
        // Arrange
        String clientUuid = UUID.randomUUID().toString();
        String errorMessage = "Client not found. UUID: " + clientUuid;

        when(accountService.findAllAccountsByClientUUID(clientUuid))
                .thenThrow(new ClientNotFoundException(errorMessage));

        // Act
        MvcResult result = mockMvc.perform(get(API_ACCOUNT_V1 + GET_CLIENT_ACCOUNTS, clientUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn();

        // Assert
        String responseBody = result.getResponse().getContentAsString();
        int statusCode = result.getResponse().getStatus();
        ErrorDTO error = objectMapper.readValue(responseBody, ErrorDTO.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), statusCode, "Account not found status code mismatch");
        Assertions.assertEquals(errorMessage, error.getError(), "Account not found error message");
    }


    @Test
    void deleteAccount_whenAccountExists_shouldReturnAcceptedAndDto() throws Exception {
        // given
        String uuid = UUID.randomUUID().toString();
        AccountInfoDTO dto = new AccountInfoDTO();
        dto.setUUID(uuid);
        dto.setBalance(BigDecimal.valueOf(1000));
        dto.setAccountNumber(faker.finance().iban());
        dto.setAccountType(AccountType.CHECKING);

        when(accountService.deleteAccount(uuid)).thenReturn(dto);

        // when
        MvcResult result = mockMvc.perform(delete(API_ACCOUNT_V1 + DELETE_ACCOUNT, uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();

        // then
        String responseBody = result.getResponse().getContentAsString();
        int statusCode = result.getResponse().getStatus();
        AccountInfoDTO actual = objectMapper.readValue(responseBody, AccountInfoDTO.class);

        Assertions.assertEquals(HttpStatus.ACCEPTED.value(), statusCode, "Account not found status code mismatch");
        Assertions.assertEquals(dto.getBalance(), actual.getBalance(), "Account balance mismatch");
        Assertions.assertEquals(dto.getAccountNumber(), actual.getAccountNumber(), "Account number mismatch");
        Assertions.assertEquals(dto.getAccountType(), actual.getAccountType(), "Account type mismatch");
    }

    @Test
    void deleteAccount_whenAccountNotFound_shouldReturn404() throws Exception {
        // given
        String uuid = UUID.randomUUID().toString();
        String expectedErrorMessage = "Account not found: " + uuid;

        when(accountService.deleteAccount(uuid))
                .thenThrow(new AccountNotFoundException(expectedErrorMessage));

        // when
        MvcResult result = mockMvc.perform(delete(API_ACCOUNT_V1 + DELETE_ACCOUNT, uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        // then
        String responseBody = result.getResponse().getContentAsString();
        int statusCode = result.getResponse().getStatus();
        ErrorDTO errorDTO = objectMapper.readValue(responseBody, ErrorDTO.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), statusCode, "Status code should be 404");
        Assertions.assertEquals(expectedErrorMessage, errorDTO.getError(), "Error message mismatch");
    }

    @Test
    void deleteAccount_whenOnlyAccountOfType_shouldReturn409() throws Exception {
        // given
        String uuid = UUID.randomUUID().toString();
        String expectedErrorMessage = "Cannot delete the only account of this type.";

        when(accountService.deleteAccount(uuid))
                .thenThrow(new AccountDeletionConflictException(expectedErrorMessage));

        // when
        MvcResult result = mockMvc.perform(delete(API_ACCOUNT_V1 + DELETE_ACCOUNT, uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();

        // then
        String responseBody = result.getResponse().getContentAsString();
        int statusCode = result.getResponse().getStatus();
        ErrorDTO errorDTO = objectMapper.readValue(responseBody, ErrorDTO.class);

        Assertions.assertEquals(HttpStatus.CONFLICT.value(), statusCode, "Status code should be 409");
        Assertions.assertEquals(expectedErrorMessage, errorDTO.getError(), "Error message mismatch");
    }

    @Test
    void updateAccount_whenValidRequest_shouldReturnAccepted() throws Exception {
        // given
        AccountRequestDTO request = new AccountRequestDTO();
        request.setAccountUuid(UUID.randomUUID().toString());
        request.setAccountNumber(faker.finance().iban());
        request.setAccountType(AccountType.SAVINGS);
        request.setBalance(BigDecimal.valueOf(1500));

        AccountResponseDTO response = new AccountResponseDTO();
        response.setUUID(UUID.randomUUID().toString());
        response.setAccountNumber(request.getAccountNumber());
        response.setAccountType(request.getAccountType());
        response.setBalance(request.getBalance());
        response.setCreatedAt(LocalDateTime.now().minusDays(1));
        response.setLastUpdated(LocalDateTime.now());

        when(accountService.updateAccount(any())).thenReturn(response);

        // when
        MvcResult result = mockMvc.perform(patch(API_ACCOUNT_V1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();

        // then
        AccountResponseDTO actual = objectMapper.readValue(result.getResponse().getContentAsString(), AccountResponseDTO.class);
        Assertions.assertEquals(request.getAccountNumber(), actual.getAccountNumber(), "Account number mismatch");
        Assertions.assertEquals(request.getAccountType(), actual.getAccountType(), "Account type mismatch");
        Assertions.assertEquals(request.getBalance(), actual.getBalance(), "Balance mismatch");
    }

    @Test
    void updateAccount_whenAccountNotFound_shouldReturn404() throws Exception {
        // given
        AccountRequestDTO request = new AccountRequestDTO();
        request.setAccountUuid(UUID.randomUUID().toString());
        request.setAccountNumber(faker.finance().iban());
        request.setAccountType(AccountType.CHECKING);
        request.setBalance(BigDecimal.valueOf(2000));

        String errorMessage = "Account number " + request.getAccountNumber() + " not exist: " + request.getAccountUuid();
        when(accountService.updateAccount(any())).thenThrow(new AccountNotFoundException(errorMessage));

        // when
        MvcResult result = mockMvc.perform(patch(API_ACCOUNT_V1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ErrorDTO error = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDTO.class);
        Assertions.assertEquals(errorMessage, error.getError(), "Error message mismatch");
    }

    @Test
    void updateAccount_whenClientNotFound_shouldReturn404() throws Exception {
        // given
        AccountRequestDTO request = new AccountRequestDTO();
        request.setAccountUuid(UUID.randomUUID().toString());
        request.setAccountNumber(faker.finance().iban());
        request.setAccountType(AccountType.SAVINGS);
        request.setBalance(BigDecimal.valueOf(3000));

        when(accountService.updateAccount(any())).thenThrow(new ClientNotFoundException("Client not found"));

        // when
        MvcResult result = mockMvc.perform(patch(API_ACCOUNT_V1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ErrorDTO error = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorDTO.class);
        Assertions.assertEquals("Client not found", error.getError(), "Error message mismatch");
    }

    @Test
    void updateAccount_whenInvalidRequest_shouldReturn400() throws Exception {
        // given: empty request body
        String emptyBody = "{}";

        // when & then
        mockMvc.perform(patch(API_ACCOUNT_V1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptyBody)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAccounts_whenAccountsExist_shouldReturnListOfAccounts() throws Exception {
        // given
        AccountInfoDTO dto1 = AccountInfoDTO.builder()
                .UUID(UUID.randomUUID().toString())
                .accountNumber(faker.finance().iban())
                .balance(BigDecimal.valueOf(1000))
                .accountType(AccountType.CHECKING)
                .build();

        AccountInfoDTO dto2 = AccountInfoDTO.builder()
                .UUID(UUID.randomUUID().toString())
                .accountNumber(faker.finance().iban())
                .balance(BigDecimal.valueOf(2000))
                .accountType(AccountType.SAVINGS)
                .build();

        when(accountService.findAllAccounts()).thenReturn(List.of(dto1, dto2));

        // when
        MvcResult result = mockMvc.perform(get(API_ACCOUNT_V1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        String responseBody = result.getResponse().getContentAsString();
        List<AccountInfoDTO> actual = objectMapper.readValue(responseBody,
                objectMapper.getTypeFactory().constructCollectionType(List.class, AccountInfoDTO.class));

        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(dto1.getAccountNumber(), actual.get(0).getAccountNumber());
        Assertions.assertEquals(dto2.getBalance(), actual.get(1).getBalance());
    }

    @Test
    void getAccounts_whenNoAccountsExist_shouldReturn404() throws Exception {
        // given
        when(accountService.findAllAccounts()).thenThrow(new AccountNotFoundException("Accounts not found"));

        // when
        MvcResult result = mockMvc.perform(get(API_ACCOUNT_V1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        // then
        String responseBody = result.getResponse().getContentAsString();
        ErrorDTO errorDTO = objectMapper.readValue(responseBody, ErrorDTO.class);

        Assertions.assertEquals("Accounts not found", errorDTO.getError());
    }

    @TestConfiguration
    static class NoSecurityConfig {
        @Bean
        public org.springframework.security.web.SecurityFilterChain securityFilterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {
            http.csrf(org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }
}
