package com.ihomziak.clientmanagerservice.service.impl;

import com.ihomziak.clientmanagerservice.dao.ClientRepository;
import com.ihomziak.clientmanagerservice.dto.ClientRequestDTO;
import com.ihomziak.clientmanagerservice.dto.ClientResponseDTO;
import com.ihomziak.clientmanagerservice.dto.ClientsInfoDTO;
import com.ihomziak.clientmanagerservice.entity.Client;
import com.ihomziak.clientmanagerservice.exception.ClientAlreadyExistException;
import com.ihomziak.clientmanagerservice.exception.ClientNotFoundException;
import com.ihomziak.clientmanagerservice.mapper.impl.MapStructMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private MapStructMapperImpl mapper;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client client;
    private ClientRequestDTO clientRequestDTO;
    private ClientResponseDTO clientResponseDTO;
    private String clientUUID;

    @BeforeEach
    void setUp() {
        clientUUID = UUID.randomUUID().toString();

        client = new Client();
        client.setUUID(clientUUID);
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setTaxNumber("123456789");
        client.setEmail("john.doe@example.com");
        client.setPhoneNumber("123-456-7890");
        client.setAddress("123 Main St");
        client.setCreatedAt(LocalDateTime.now());
        client.setUpdatedAt(LocalDateTime.now());

        clientRequestDTO = new ClientRequestDTO();
        clientRequestDTO.setFirstName("John");
        clientRequestDTO.setLastName("Doe");
        clientRequestDTO.setTaxNumber("123456789");
        clientRequestDTO.setEmail("john.doe@example.com");
        clientRequestDTO.setPhoneNumber("123-456-7890");
        clientRequestDTO.setAddress("123 Main St");

        clientResponseDTO = new ClientResponseDTO();
        clientResponseDTO.setClientId(1L);
        clientResponseDTO.setFirstName("John");
        clientResponseDTO.setLastName("Doe");
        clientResponseDTO.setTaxNumber("123456789");
        clientResponseDTO.setEmail("john.doe@example.com");
        clientResponseDTO.setPhoneNumber("123-456-7890");
        clientResponseDTO.setAddress("123 Main St");
        clientResponseDTO.setUUID(clientUUID);
        clientResponseDTO.setCreatedAt(LocalDateTime.now());
        clientResponseDTO.setUpdateAt(LocalDateTime.now());
    }

    @Test
    void findAll_ShouldReturnClients_WhenClientsExist() {
        when(clientRepository.findAll()).thenReturn(List.of(client));
        when(mapper.clientsToClientInfoDto(anyList())).thenReturn(List.of(new ClientsInfoDTO()));

        List<ClientsInfoDTO> clientsInfoDTOs = clientService.findAll();

        assertNotNull(clientsInfoDTOs);
        assertFalse(clientsInfoDTOs.isEmpty());
        verify(clientRepository, times(1)).findAll();
        verify(mapper, times(1)).clientsToClientInfoDto(anyList());
    }

    @Test
    void findAll_ShouldThrowException_WhenNoClientsExist() {
        when(clientRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ClientNotFoundException.class, () -> clientService.findAll());
        verify(clientRepository, times(1)).findAll();
        verify(mapper, never()).clientsToClientInfoDto(anyList());
    }

    @Test
    void createClient_ShouldReturnClientResponse_WhenClientCreated() {
        when(clientRepository.findClientByTaxNumber(anyString())).thenReturn(Optional.empty());
        when(mapper.clientRequestDtoToClient(any(ClientRequestDTO.class))).thenReturn(client);
        when(mapper.clientToClientResponseDto(any(Client.class))).thenReturn(clientResponseDTO);

        ClientResponseDTO response = clientService.createClient(clientRequestDTO);

        assertNotNull(response);
        assertEquals(clientResponseDTO, response);
        verify(clientRepository, times(1)).findClientByTaxNumber(anyString());
        verify(clientRepository, times(1)).save(any(Client.class));
        verify(mapper, times(1)).clientRequestDtoToClient(any(ClientRequestDTO.class));
        verify(mapper, times(1)).clientToClientResponseDto(any(Client.class));
    }

    @Test
    void createClient_ShouldThrowException_WhenClientAlreadyExists() {
        when(clientRepository.findClientByTaxNumber(anyString())).thenReturn(Optional.of(client));

        assertThrows(ClientAlreadyExistException.class, () -> clientService.createClient(clientRequestDTO));
        verify(clientRepository, times(1)).findClientByTaxNumber(anyString());
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void deleteByUUID_ShouldReturnClientResponse_WhenClientExists() {
        when(clientRepository.findClientByUUID(anyString())).thenReturn(Optional.of(client));
        when(mapper.clientToClientResponseDto(any(Client.class))).thenReturn(clientResponseDTO);

        ClientResponseDTO response = clientService.deleteByUUID(clientUUID);

        assertNotNull(response);
        assertEquals(clientResponseDTO, response);
        verify(clientRepository, times(1)).findClientByUUID(anyString());
        verify(clientRepository, times(1)).delete(any(Client.class));
        verify(mapper, times(1)).clientToClientResponseDto(any(Client.class));
    }

    @Test
    void deleteByUUID_ShouldThrowException_WhenClientDoesNotExist() {
        when(clientRepository.findClientByUUID(anyString())).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.deleteByUUID(clientUUID));
        verify(clientRepository, times(1)).findClientByUUID(anyString());
        verify(clientRepository, never()).delete(any(Client.class));
    }

    @Test
    void updateClient_ShouldReturnClientResponse_WhenClientUpdated() {
        when(clientRepository.findClientByTaxNumber(anyString())).thenReturn(Optional.of(client));
        when(mapper.clientToClientResponseDto(any(Client.class))).thenReturn(clientResponseDTO);

        ClientResponseDTO response = clientService.updateClient(clientRequestDTO);

        assertNotNull(response);
        assertEquals(clientResponseDTO, response);
        verify(clientRepository, times(1)).findClientByTaxNumber(anyString());
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void updateClient_ShouldThrowException_WhenClientDoesNotExist() {
        when(clientRepository.findClientByTaxNumber(anyString())).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.updateClient(clientRequestDTO));
        verify(clientRepository, times(1)).findClientByTaxNumber(anyString());
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void findClientByUUID_ShouldReturnClientResponse_WhenClientExists() {
        when(clientRepository.findClientByUUID(anyString())).thenReturn(Optional.of(client));
        when(mapper.clientToClientResponseDto(any(Client.class))).thenReturn(clientResponseDTO);

        ClientResponseDTO response = clientService.findClientByUUID(clientUUID);

        assertNotNull(response);
        assertEquals(clientResponseDTO, response);
        verify(clientRepository, times(1)).findClientByUUID(anyString());
        verify(mapper, times(1)).clientToClientResponseDto(any(Client.class));
    }

    @Test
    void findClientByUUID_ShouldThrowException_WhenClientDoesNotExist() {
        when(clientRepository.findClientByUUID(anyString())).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.findClientByUUID(clientUUID));
        verify(clientRepository, times(1)).findClientByUUID(anyString());
        verify(mapper, never()).clientToClientResponseDto(any(Client.class));
    }
}
