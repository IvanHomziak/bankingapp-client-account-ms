package com.ihomziak.clientaccountms.service.impl;

import com.ihomziak.bankingapp.common.utils.AccountType;
import com.ihomziak.clientaccountms.dao.ClientRepository;
import com.ihomziak.clientaccountms.dto.ClientRequestDTO;
import com.ihomziak.clientaccountms.dto.ClientResponseDTO;
import com.ihomziak.clientaccountms.dto.ClientsInfoDTO;
import com.ihomziak.clientaccountms.entity.Account;
import com.ihomziak.clientaccountms.entity.Client;
import com.ihomziak.clientaccountms.exception.ClientAlreadyExistException;
import com.ihomziak.clientaccountms.exception.ClientNotFoundException;
import com.ihomziak.clientaccountms.mapper.impl.MapStructMapperImpl;
import com.ihomziak.clientaccountms.service.ClientService;
import com.ihomziak.clientaccountms.util.UserSpecifications;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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

    // Selects data from a database using query language
    @Override
    public ClientResponseDTO findClientByName(final String firstName, final String lastName) {
        Optional<Client> theClient = this.clientRepository.findByFirstNameAndLastNameIgnoreCase(firstName, lastName);

        if (theClient.isEmpty()) {
            throw new ClientNotFoundException(String.format("Client %s not found. Firstname: %s, Lastname: $s", firstName, lastName));
        }
        return this.mapper.clientToClientResponseDto(theClient.get());
    }

    @Override
    public List<ClientsInfoDTO> findUsers(String order, String firstName, String lastName, String email, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, getSort(order));

        Specification<Client> spec = Specification.where(
                UserSpecifications.hasFirstName(firstName)
                        .and(UserSpecifications.hasLastName(lastName))
                        .and(UserSpecifications.hasEmail(email))
        );

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return clientRepository.findAll(spec, pageable)
                .stream()
                .map(client -> modelMapper.map(client, ClientsInfoDTO.class))
                .toList();
    }


    private Sort getSort(String order) {
        if ("desc".equalsIgnoreCase(order)) {
            return Sort.by(Sort.Direction.DESC, "firstName");
        } else {
            return Sort.by(Sort.Direction.ASC, "firstName");
        }
    }
}
