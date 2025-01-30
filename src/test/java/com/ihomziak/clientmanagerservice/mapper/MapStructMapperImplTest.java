package com.ihomziak.clientmanagerservice.mapper;

import com.ihomziak.clientmanagerservice.dto.*;
import com.ihomziak.clientmanagerservice.entity.Account;
import com.ihomziak.clientmanagerservice.entity.Client;
import com.ihomziak.clientmanagerservice.mapper.impl.MapStructMapperImpl;
import com.ihomziak.bankingapp.common.utils.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MapStructMapperImplTest {

    private MapStructMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new MapStructMapperImpl();
    }

    @Test
    void testClientRequestDtoToClient() {
        ClientRequestDTO requestDTO = new ClientRequestDTO();
        requestDTO.setFirstName("John");
        requestDTO.setLastName("Doe");
        requestDTO.setTaxNumber("123456789");
        requestDTO.setAddress("123 Main St");
        requestDTO.setEmail("john.doe@example.com");
        requestDTO.setPhoneNumber("123-456-7890");
        requestDTO.setDateOfBirth("1990-01-01");

        Client client = mapper.clientRequestDtoToClient(requestDTO);

        assertNotNull(client);
        assertEquals(requestDTO.getFirstName(), client.getFirstName());
        assertEquals(requestDTO.getLastName(), client.getLastName());
        assertEquals(requestDTO.getTaxNumber(), client.getTaxNumber());
        assertEquals(requestDTO.getAddress(), client.getAddress());
        assertEquals(requestDTO.getEmail(), client.getEmail());
        assertEquals(requestDTO.getPhoneNumber(), client.getPhoneNumber());
        assertEquals(requestDTO.getDateOfBirth(), client.getDateOfBirth());
    }

    @Test
    void testClientToClientResponseDto() {
        Client client = new Client();
        client.setClientId(1L);
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setTaxNumber("123456789");
        client.setAddress("123 Main St");
        client.setEmail("john.doe@example.com");
        client.setPhoneNumber("123-456-7890");
        client.setDateOfBirth("1990-01-01");
        client.setUUID("client-uuid");
        client.setCreatedAt(LocalDateTime.now());
        client.setUpdatedAt(LocalDateTime.now());

        ClientResponseDTO responseDTO = mapper.clientToClientResponseDto(client);

        assertNotNull(responseDTO);
        assertEquals(client.getClientId(), responseDTO.getClientId());
        assertEquals(client.getFirstName(), responseDTO.getFirstName());
        assertEquals(client.getLastName(), responseDTO.getLastName());
        assertEquals(client.getTaxNumber(), responseDTO.getTaxNumber());
        assertEquals(client.getAddress(), responseDTO.getAddress());
        assertEquals(client.getEmail(), responseDTO.getEmail());
        assertEquals(client.getPhoneNumber(), responseDTO.getPhoneNumber());
        assertEquals(client.getDateOfBirth(), responseDTO.getDateOfBirth());
        assertEquals(client.getUUID(), responseDTO.getUUID());
    }

    @Test
    void testClientsToClientInfoDto() {
        Client client1 = new Client();
        client1.setFirstName("John");
        client1.setLastName("Doe");
        client1.setEmail("john.doe@example.com");
        client1.setPhoneNumber("123-456-7890");

        Client client2 = new Client();
        client2.setFirstName("Jane");
        client2.setLastName("Smith");
        client2.setEmail("jane.smith@example.com");
        client2.setPhoneNumber("098-765-4321");

        List<Client> clients = new ArrayList<>();
        clients.add(client1);
        clients.add(client2);

        List<ClientsInfoDTO> clientsInfoDTOList = mapper.clientsToClientInfoDto(clients);

        assertNotNull(clientsInfoDTOList);
        assertEquals(2, clientsInfoDTOList.size());

        assertEquals(client1.getFirstName(), clientsInfoDTOList.get(0).getFirstName());
        assertEquals(client1.getLastName(), clientsInfoDTOList.get(0).getLastName());
        assertEquals(client1.getEmail(), clientsInfoDTOList.get(0).getEmail());
        assertEquals(client1.getPhoneNumber(), clientsInfoDTOList.get(0).getPhoneNumber());

        assertEquals(client2.getFirstName(), clientsInfoDTOList.get(1).getFirstName());
        assertEquals(client2.getLastName(), clientsInfoDTOList.get(1).getLastName());
        assertEquals(client2.getEmail(), clientsInfoDTOList.get(1).getEmail());
        assertEquals(client2.getPhoneNumber(), clientsInfoDTOList.get(1).getPhoneNumber());
    }

    @Test
    void testAccountRequestDtoToAccount() {
        AccountRequestDTO requestDTO = new AccountRequestDTO();
        requestDTO.setAccountNumber("123456789");
        requestDTO.setAccountType(AccountType.CHECKING);
        requestDTO.setBalance(1000.0);
        requestDTO.setClientUUID("client-uuid");

        Account account = mapper.accountRequestDtoToAccount(requestDTO);

        assertNotNull(account);
        assertEquals(requestDTO.getAccountNumber(), account.getAccountNumber());
        assertEquals(requestDTO.getAccountType(), account.getAccountType());
        assertEquals(requestDTO.getBalance(), account.getBalance());
        assertEquals(requestDTO.getClientUUID(), account.getUUID());
    }

    @Test
    void testAccountToAccountResponseDto() {
        Account account = new Account();
        account.setAccountId(1L);
        account.setAccountNumber("123456789");
        account.setAccountType(AccountType.CHECKING);
        account.setBalance(1000.0);
        account.setUUID("account-uuid");

        Client client = new Client();
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setUUID("client-uuid");

        account.setClient(client);

        AccountResponseDTO responseDTO = mapper.accountToAccountResponseDto(account);

        assertNotNull(responseDTO);
        assertEquals(account.getAccountId(), responseDTO.getAccountId());
        assertEquals(account.getAccountNumber(), responseDTO.getAccountNumber());
        assertEquals(account.getAccountType(), responseDTO.getAccountType());
        assertEquals(account.getBalance(), responseDTO.getBalance());
        assertEquals(account.getUUID(), responseDTO.getUUID());
        assertEquals(client.getFirstName(), responseDTO.getAccountHolderDTO().getFirstName());
        assertEquals(client.getLastName(), responseDTO.getAccountHolderDTO().getLastName());
        assertEquals(client.getUUID(), responseDTO.getAccountHolderDTO().getUUID());
    }

    @Test
    void testAccountToAccountInfoDto() {
        Account account = new Account();
        account.setAccountNumber("123456789");
        account.setAccountType(AccountType.SAVINGS);
        account.setBalance(500.0);
        account.setUUID("account-uuid");

        AccountInfoDTO infoDTO = mapper.accountToAccountInfoDto(account);

        assertNotNull(infoDTO);
        assertEquals(account.getAccountNumber(), infoDTO.getAccountNumber());
        assertEquals(account.getAccountType(), infoDTO.getAccountType());
        assertEquals(account.getBalance(), infoDTO.getBalance());
        assertEquals(account.getUUID(), infoDTO.getUUID());
    }

    @Test
    void testClientToAccountHolderDto() {
        Client client = new Client();
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setUUID("client-uuid");

        AccountHolderDTO holderDTO = mapper.clientToAccountHolderDto(Optional.of(client));

        assertNotNull(holderDTO);
        assertEquals(client.getFirstName(), holderDTO.getFirstName());
        assertEquals(client.getLastName(), holderDTO.getLastName());
        assertEquals(client.getUUID(), holderDTO.getUUID());
    }
}

