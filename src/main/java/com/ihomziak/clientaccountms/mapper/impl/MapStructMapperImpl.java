package com.ihomziak.clientaccountms.mapper.impl;

import com.ihomziak.clientaccountms.dto.*;
import com.ihomziak.clientaccountms.entity.Account;
import com.ihomziak.clientaccountms.entity.Client;
import com.ihomziak.clientaccountms.mapper.MapStructMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MapStructMapperImpl implements MapStructMapper {

    @Override
    public Client clientRequestDtoToClient(ClientRequestDTO clientRequestDTO) {

        if (clientRequestDTO == null) {
            return null;
        }

        Client client = new Client();

        client.setFirstName(clientRequestDTO.getFirstName());
        client.setLastName(clientRequestDTO.getLastName());
        client.setTaxNumber(clientRequestDTO.getTaxNumber());
        client.setAddress(clientRequestDTO.getAddress());
        client.setEmail(clientRequestDTO.getEmail());
        client.setPhoneNumber(clientRequestDTO.getPhoneNumber());
        client.setDateOfBirth(clientRequestDTO.getDateOfBirth());

        return client;
    }

    @Override
    public ClientResponseDTO clientToClientResponseDto(Client theClient) {
        ClientResponseDTO responseDTO = new ClientResponseDTO();

        responseDTO.setClientId(theClient.getClientId());
        responseDTO.setFirstName(theClient.getFirstName());
        responseDTO.setLastName(theClient.getLastName());
        responseDTO.setTaxNumber(theClient.getTaxNumber());
        responseDTO.setAddress(theClient.getAddress());
        responseDTO.setEmail(theClient.getEmail());
        responseDTO.setPhoneNumber(theClient.getPhoneNumber());
        responseDTO.setDateOfBirth(theClient.getDateOfBirth());
        responseDTO.setUUID(theClient.getUUID());
        responseDTO.setCreatedAt(theClient.getCreatedAt());
        responseDTO.setUpdateAt(theClient.getUpdatedAt());

        return responseDTO;
    }

    @Override
    public List<ClientsInfoDTO> clientsToClientInfoDto(List<Client> clients) {
        List<ClientsInfoDTO> clientsInfoDTOList = new ArrayList<>();

        for (Client client : clients) {
            ClientsInfoDTO clientInfoDTO = new ClientsInfoDTO();
            clientInfoDTO.setFirstName(client.getFirstName());
            clientInfoDTO.setLastName(client.getLastName());
            clientInfoDTO.setEmail(client.getEmail());
            clientInfoDTO.setPhoneNumber(client.getPhoneNumber());

            clientsInfoDTOList.add(clientInfoDTO);
        }

        return clientsInfoDTOList;
    }

    @Override
    public AccountResponseDTO accountToAccountResponseDto(Account account) {
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO();


        accountHolderDTO.setFirstName(account.getClient().getFirstName());
        accountHolderDTO.setLastName(account.getClient().getLastName());
        accountHolderDTO.setUUID(account.getClient().getUUID());

        accountResponseDTO.setAccountHolderDTO(accountHolderDTO);
        accountResponseDTO.setBalance(account.getBalance());
        accountResponseDTO.setAccountNumber(account.getAccountNumber());
        accountResponseDTO.setAccountType(account.getAccountType());
        accountResponseDTO.setUUID(account.getUUID());
        accountResponseDTO.setCreatedAt(account.getCreatedAt());
        accountResponseDTO.setLastUpdated(account.getLastUpdate());

        return accountResponseDTO;
    }

    @Override
    public AccountHolderDTO clientToAccountHolderDto(Optional<Client> theClient) {
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO();
        Client client = theClient.get();

        accountHolderDTO.setFirstName(client.getFirstName());
        accountHolderDTO.setLastName(client.getLastName());
        accountHolderDTO.setUUID(client.getUUID());

        return accountHolderDTO;
    }

    @Override
    public Account accountRequestDtoToAccount(AccountRequestDTO accountRequestDTO) {

        Account account = new Account();

        account.setAccountNumber(accountRequestDTO.getAccountNumber());
        account.setAccountType(accountRequestDTO.getAccountType());
        account.setBalance(accountRequestDTO.getBalance());
        account.setUUID(accountRequestDTO.getAccountUuid());

        return account;
    }

    @Override
    public AccountInfoDTO accountToAccountInfoDto(Account accountByUUID) {
        AccountInfoDTO accountInfoDTO = new AccountInfoDTO();
        accountInfoDTO.setAccountType(accountByUUID.getAccountType());
        accountInfoDTO.setAccountNumber(accountByUUID.getAccountNumber());
        accountInfoDTO.setBalance(accountByUUID.getBalance());
        accountInfoDTO.setUUID(accountByUUID.getUUID());
        return accountInfoDTO;
    }
}
