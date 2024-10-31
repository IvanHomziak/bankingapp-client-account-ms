package com.ihomziak.clientmanagerservice.service;

import com.ihomziak.clientmanagerservice.dto.ClientRequestDTO;
import com.ihomziak.clientmanagerservice.dto.ClientResponseDTO;
import com.ihomziak.clientmanagerservice.dto.ClientsInfoDTO;

import java.util.List;

public interface ClientService {

    List<ClientsInfoDTO> findAll();

    ClientResponseDTO createClient(ClientRequestDTO client);

    ClientResponseDTO deleteByUUID(String uuid);

    ClientResponseDTO updateClient(ClientRequestDTO clientRequestDTO);

    ClientResponseDTO findClientByUUID(String uuid);
}