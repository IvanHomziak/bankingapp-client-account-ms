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
    @Autowired
    private PathMatcher mvcPathMatcher;

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
        accountRequestDTO.setClientUUID(faker.internet().uuid());

        accountHolder = new AccountHolderDTO();
        accountHolder.setUUID(accountRequestDTO.getClientUUID());
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
        accountRequestDTO.setClientUUID(UUID.randomUUID().toString());
        String errorMessage = "Client not found: " + accountRequestDTO.getClientUUID();
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
        accountRequestDTO.setClientUUID(faker.internet().uuid());

        accountRequestDTO.setClientUUID(UUID.randomUUID().toString());
        AccountInfoDTO expectedResponse = new AccountInfoDTO();
        expectedResponse.setAccountNumber(accountRequestDTO.getAccountNumber());
        expectedResponse.setBalance(accountRequestDTO.getBalance());
        expectedResponse.setUUID(UUID.randomUUID().toString());
        expectedResponse.setAccountType(accountRequestDTO.getAccountType());
        when(accountService.findAccountByUuid(accountRequestDTO.getClientUUID()))
                .thenReturn(expectedResponse);

        // Act
        MvcResult result = mockMvc.perform(get(API_ACCOUNT_V1 + GET_ACCOUNT, accountRequestDTO.getClientUUID())
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
        accountRequestDTO.setClientUUID(faker.internet().uuid());

        accountRequestDTO.setClientUUID(UUID.randomUUID().toString());
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
    void createCheckingAccount() {
    }

    @Test
    void deleteAccount() {
    }

    @Test
    void updateAccount() {
    }

    @Test
    void getAccounts() {
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
