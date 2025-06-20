package com.ihomziak.clientaccountms.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ihomziak.clientaccountms.dto.ClientRequestDTO;
import com.ihomziak.clientaccountms.dto.ClientResponseDTO;
import com.ihomziak.clientaccountms.dto.ClientsInfoDTO;
import com.ihomziak.clientaccountms.dto.LastNameCountDTO;
import com.ihomziak.clientaccountms.exceptionhandler.GlobalExceptionHandler;
import com.ihomziak.clientaccountms.service.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.UUID;

import static com.ihomziak.clientaccountms.util.constants.Endpoints.ClientEndpoints.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClientController.class)
@Import({
        ClientControllerTest.NoSecurityConfig.class, // 👈 disables Spring Security
        GlobalExceptionHandler.class
})
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClientService clientService;

    private Faker faker;

    private ClientRequestDTO clientRequestDTO;
    private ClientResponseDTO clientResponseDTO;

    @BeforeEach
    void setUp() {
        faker = new Faker();
        clientRequestDTO = ClientRequestDTO.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .dateOfBirth("1990-01-01")
                .taxNumber(faker.idNumber().valid())
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().cellPhone())
                .address(faker.address().fullAddress())
                .build();

        clientResponseDTO = ClientResponseDTO.builder()
                .UUID(UUID.randomUUID().toString())
                .firstName(clientRequestDTO.getFirstName())
                .lastName(clientRequestDTO.getLastName())
                .email(clientRequestDTO.getEmail())
                .phoneNumber(clientRequestDTO.getPhoneNumber())
                .address(clientRequestDTO.getAddress())
                .build();
    }


    @Test
    void addClient_whenValidRequest_shouldReturnCreatedClient() throws Exception {
        when(clientService.createClient(any(ClientRequestDTO.class))).thenReturn(clientResponseDTO);

        MvcResult result = mockMvc.perform(post(API_CLIENT_V1 + ADD_CLIENT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        ClientResponseDTO actual = objectMapper.readValue(result.getResponse().getContentAsString(), ClientResponseDTO.class);
        assertEquals(clientResponseDTO.getEmail(), actual.getEmail());
    }

    @Test
    void getClient_whenExists_shouldReturnClient() throws Exception {
        when(clientService.findClientByUUID(clientResponseDTO.getUUID())).thenReturn(clientResponseDTO);

        MvcResult result = mockMvc.perform(get(API_CLIENT_V1 + GET_CLIENT, clientResponseDTO.getUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ClientResponseDTO actual = objectMapper.readValue(result.getResponse().getContentAsString(), ClientResponseDTO.class);
        assertEquals(clientResponseDTO.getUUID(), actual.getUUID());
    }

    @Test
    void getClients_withNoFilters_shouldReturnClientsList() throws Exception {
        ClientsInfoDTO info = ClientsInfoDTO.builder()
                .firstName(faker.starWars().character())
                .lastName(faker.starWars().planets())
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .build();

        when(clientService.findAll()).thenReturn(List.of(info));

        MvcResult result = mockMvc.perform(get(API_CLIENT_V1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        List<ClientsInfoDTO> actualList = objectMapper.readValue(
                responseBody,
                new TypeReference<List<ClientsInfoDTO>>() {
                }
        );

        assertEquals(1, actualList.size());
    }

    @Test
    void deleteClient_whenClientExists_shouldReturnDeletedClient() throws Exception {
        String uuid = clientResponseDTO.getUUID();
        when(clientService.deleteByUUID(uuid)).thenReturn(clientResponseDTO);

        mockMvc.perform(delete(API_CLIENT_V1 + DELETE_CLIENT, uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(uuid));
    }

    @Test
    void countClientsByLastName_shouldReturnCountDTO() throws Exception {
        LastNameCountDTO countDTO = new LastNameCountDTO("Smith", 5L);

        when(clientService.countClientsByLastName("Smith", "DESC")).thenReturn(countDTO);

        mockMvc.perform(get(API_CLIENT_V1 + COUNT_CLIENTS_BY_LAST_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("lastName", "Smith")
                        .param("order", "DESC")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.count").value(5));
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