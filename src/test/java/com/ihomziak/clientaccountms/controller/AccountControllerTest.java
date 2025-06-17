package com.ihomziak.clientaccountms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ihomziak.bankingapp.common.utils.AccountType;
import com.ihomziak.clientaccountms.dto.AccountHolderDTO;
import com.ihomziak.clientaccountms.dto.AccountRequestDTO;
import com.ihomziak.clientaccountms.dto.AccountResponseDTO;
import com.ihomziak.clientaccountms.service.AccountService;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.ihomziak.clientaccountms.util.constants.Endpoints.AccountEndpoints.API_ACCOUNT_V1;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = AccountController.class)
@Import(AccountControllerTest.NoSecurityConfig.class)  // 👈 disables Spring Security
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    private ObjectMapper objectMapper;
    private AccountRequestDTO accountRequestDTO;
    private AccountHolderDTO accountHolder;
    private Faker faker;

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
        assertEquals(expectedResponse.getAccountType(), actual.getAccountType(),  "Account type mismatch");
        assertEquals(expectedResponse.getBalance(), actual.getBalance(), "Account balance mismatch");
        assertEquals(expectedResponse.getUUID(), actual.getUUID(), "Client uuid mismatch");
        assertNotNull(actual.getCreatedAt(), "Created account should be not null");
        assertNotNull(actual.getLastUpdated(), "Last updated account should be not null");
        assertEquals(accountHolder.getFirstName(), actual.getAccountHolderDTO().getFirstName(), "First name mismatch");
        assertEquals(accountHolder.getLastName(), actual.getAccountHolderDTO().getLastName(), "Last name mismatch");
        assertEquals(accountHolder.getUUID(), actual.getAccountHolderDTO().getUUID(),"Client uuid mismatch");
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
