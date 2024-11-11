package com.ihomziak.clientmanagerservice.service.impl;

import com.ihomziak.clientmanagerservice.dao.ClientRepository;
import com.ihomziak.clientmanagerservice.dto.ClientRequestDTO;
import com.ihomziak.clientmanagerservice.dto.ClientResponseDTO;
import com.ihomziak.clientmanagerservice.dto.ClientsInfoDTO;
import com.ihomziak.clientmanagerservice.entity.Account;
import com.ihomziak.clientmanagerservice.entity.Client;
import com.ihomziak.clientmanagerservice.exception.ClientAlreadyExistException;
import com.ihomziak.clientmanagerservice.exception.ClientNotFoundException;
import com.ihomziak.clientmanagerservice.mapper.impl.MapStructMapperImpl;
import com.ihomziak.clientmanagerservice.service.ClientService;
import com.ihomziak.transactioncommon.AccountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final MapStructMapperImpl mapper;


    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository,
                             MapStructMapperImpl mapper) {
        this.clientRepository = clientRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public List<ClientsInfoDTO> findAll() {
        List<Client> clients = clientRepository.findAll();
        if (clients.isEmpty()) {
            throw new ClientNotFoundException("Clients not found.");
        }
        return this.mapper.clientsToClientInfoDto(clients);
    }

    @Override
    @Transactional
    public ClientResponseDTO createClient(ClientRequestDTO clientRequestDTO) {
        if (this.clientRepository.findClientByTaxNumber(clientRequestDTO.getTaxNumber()).isPresent()) {
            throw new ClientAlreadyExistException("Client already exist");
        }

        Client theClient = mapper.clientRequestDtoToClient(clientRequestDTO);

        theClient.setUUID(UUID.randomUUID().toString());

        Account account = new Account();
        account.setAccountType(AccountType.CHECKING);
        account.setClient(theClient);

        List<Account> accounts = Collections.singletonList(account);
        theClient.setAccount(accounts);
        theClient.setCreatedAt(LocalDateTime.now());
        this.clientRepository.save(theClient);

        return mapper.clientToClientResponseDto(theClient);
    }

    @Override
    public ClientResponseDTO deleteByUUID(String uuid) {
        Optional<Client> client = this.clientRepository.findClientByUUID(uuid);

        if (client.isEmpty()) {
            throw new ClientNotFoundException("Client does not exist");
        }

        this.clientRepository.delete(client.get());

        return mapper.clientToClientResponseDto(client.get());
    }

    @Override
    @Transactional
    public ClientResponseDTO updateClient(ClientRequestDTO clientRequestDTO) {
        Optional<Client> theClient = clientRepository.findClientByTaxNumber(clientRequestDTO.getTaxNumber());

        if (theClient.isEmpty()) {
            throw new ClientNotFoundException("Client is not exist");
        }

        Client newClient = theClient.get();

        newClient.setFirstName(clientRequestDTO.getFirstName());
        newClient.setLastName(clientRequestDTO.getLastName());
        newClient.setPhoneNumber(clientRequestDTO.getPhoneNumber());
        newClient.setEmail(clientRequestDTO.getEmail());
        newClient.setAddress(clientRequestDTO.getAddress());
        newClient.setCreatedAt(theClient.get().getCreatedAt());
        newClient.setUpdatedAt(LocalDateTime.now());

        clientRepository.save(newClient);

        return mapper.clientToClientResponseDto(newClient);
    }

    @Override
    @Transactional
    public ClientResponseDTO findClientByUUID(String uuid) {
        Optional<Client> theClient = this.clientRepository.findClientByUUID(uuid);

        if (theClient.isEmpty()) {
            throw new ClientNotFoundException("Client not exist. UUID: " + uuid);
        }

        return this.mapper.clientToClientResponseDto(theClient.get());
    }
}
