package com.ihomziak.clientmanagerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ihomziak.clientmanagerservice.dto.ClientRequestDTO;
import com.ihomziak.clientmanagerservice.dto.ClientResponseDTO;
import com.ihomziak.clientmanagerservice.dto.ClientsInfoDTO;
import com.ihomziak.clientmanagerservice.exceptionhandler.GlobalExceptionHandler;
import com.ihomziak.clientmanagerservice.exception.ClientNotFoundException;
import com.ihomziak.clientmanagerservice.service.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private MockMvc mockMvc;

    private ClientRequestDTO clientRequestDTO;
    private ClientResponseDTO clientResponseDTO;
    private ClientsInfoDTO clientsInfoDTO;
    private ObjectMapper objectMapper;


    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clientController)
                .setControllerAdvice(new GlobalExceptionHandler()) // Ensure this is added
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        clientRequestDTO = new ClientRequestDTO();
        clientRequestDTO.setFirstName("John");
        clientRequestDTO.setLastName("Doe");
        clientRequestDTO.setTaxNumber("123456789");
        clientRequestDTO.setDateOfBirth("07/07/1992");
        clientRequestDTO.setEmail("john.doe@example.com");
        clientRequestDTO.setPhoneNumber("123-456-7890");
        clientRequestDTO.setAddress("123 Main St");

        clientResponseDTO = new ClientResponseDTO();
        clientResponseDTO.setClientId(1);
        clientResponseDTO.setFirstName(clientRequestDTO.getFirstName());
        clientResponseDTO.setLastName(clientRequestDTO.getLastName());
        clientResponseDTO.setDateOfBirth(clientRequestDTO.getDateOfBirth());
        clientResponseDTO.setTaxNumber(clientRequestDTO.getTaxNumber());
        clientResponseDTO.setEmail(clientRequestDTO.getEmail());
        clientResponseDTO.setPhoneNumber(clientRequestDTO.getPhoneNumber());
        clientResponseDTO.setAddress(clientRequestDTO.getAddress() + 1);
        clientResponseDTO.setCreatedAt(null);
        clientResponseDTO.setUpdateAt(null);
        clientResponseDTO.setUUID("test-uuid");

        clientsInfoDTO = new ClientsInfoDTO();
        clientsInfoDTO.setFirstName("John");
        clientsInfoDTO.setLastName("Doe");
    }

    @Test
    public void addClient_ShouldReturnCreatedStatus() throws Exception {
        when(clientService.createClient(any(ClientRequestDTO.class))).thenReturn(clientResponseDTO);

        mockMvc.perform(post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value(clientResponseDTO.getLastName()))
                .andExpect(jsonPath("$.dateOfBirth").value(clientResponseDTO.getDateOfBirth()))
                .andExpect(jsonPath("$.taxNumber").value(clientResponseDTO.getTaxNumber()))
                .andExpect(jsonPath("$.email").value(clientResponseDTO.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(clientResponseDTO.getPhoneNumber()))
                .andExpect(jsonPath("$.address").value(clientResponseDTO.getAddress()));
    }

    @Test
    public void getClient_ShouldReturnClient_WhenClientExists() throws Exception {
        String uuid = "test-uuid";

        when(clientService.findClientByUUID(uuid)).thenReturn(clientResponseDTO);

        mockMvc.perform(get("/api/clients/{uuid}", uuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(content().json(objectMapper.writeValueAsString(clientResponseDTO)));
    }

    @Test
    public void getClient_ShouldReturnNotFound_WhenClientDoesNotExist() throws Exception {
        String uuid = "non-existent-uuid";

        when(clientService.findClientByUUID(uuid)).thenThrow(new ClientNotFoundException("Client not exist. UUID: " + uuid));

        mockMvc.perform(get("/api/clients/{uuid}", uuid).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\":\"Client not exist. UUID: non-existent-uuid\"}"));
    }

    @Test
    public void getClients_ShouldReturnClients_WhenClientsExists() throws Exception {
        List<ClientsInfoDTO> clientsInfoDTOList = new ArrayList<>();
        clientsInfoDTOList.add(clientsInfoDTO);
        clientsInfoDTOList.add(clientsInfoDTO);

        when(clientService.findAll()).thenReturn(clientsInfoDTOList);

        mockMvc.perform(get("/api/clients").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(content().json(objectMapper.writeValueAsString(clientsInfoDTOList)));
    }

    @Test
    public void getClients_ShouldReturnNotFound_WhenClientsNotExists() throws Exception {
        when(clientService.findAll()).thenThrow(new ClientNotFoundException("Clients not exist."));

        mockMvc.perform(get("/api/clients").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\":\"Clients not exist.\"}"));


    }

    @Test
    public void deleteClient_ShouldReturnClientResponseDTO_WhenClientExists() throws Exception {
        String uuid = "test-uuid";

        when(clientService.deleteByUUID(uuid)).thenReturn(clientResponseDTO);

        mockMvc.perform(delete("/api/clients/{uuid}", uuid).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(clientResponseDTO)));
    }

    @Test
    public void deleteClient_ShouldReturnClientNotFound_WhenClientNotExists() throws Exception {
        String uuid = "test-uuid";

        when(clientService.deleteByUUID(uuid)).thenThrow(new ClientNotFoundException("Client does not exist"));

        mockMvc.perform(delete("/api/clients/{uuid}", uuid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\":\"Client does not exist\"}"));
    }

    @Test
    public void updateClient_ShouldReturnClientResponseDTO_WhenClientExists() throws Exception {

        when(clientService.updateClient(any(ClientRequestDTO.class))).thenReturn(clientResponseDTO);

        mockMvc.perform(patch("/api/clients/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequestDTO)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(objectMapper.writeValueAsString(clientResponseDTO)));
    }
}
