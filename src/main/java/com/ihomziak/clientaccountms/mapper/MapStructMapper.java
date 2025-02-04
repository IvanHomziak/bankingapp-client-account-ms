package com.ihomziak.clientaccountms.mapper;

import com.ihomziak.clientaccountms.dto.*;
import com.ihomziak.clientaccountms.entity.Account;
import com.ihomziak.clientaccountms.entity.Client;

import java.util.List;
import java.util.Optional;

public interface MapStructMapper {

    Client clientRequestDtoToClient(ClientRequestDTO clientRequestDTO);

    ClientResponseDTO clientToClientResponseDto(Client theClient);

    List<ClientsInfoDTO> clientsToClientInfoDto(List<Client> clients);

    AccountResponseDTO accountToAccountResponseDto(Account account);

    AccountHolderDTO clientToAccountHolderDto(Optional<Client> theClient);

    Account accountRequestDtoToAccount(AccountRequestDTO accountRequestDTO);

    AccountInfoDTO accountToAccountInfoDto(Account accountByUUID);
}

