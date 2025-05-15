package com.ihomziak.clientaccountms.service;

import com.ihomziak.clientaccountms.dto.ClientRequestDTO;
import com.ihomziak.clientaccountms.dto.ClientResponseDTO;
import com.ihomziak.clientaccountms.dto.ClientsInfoDTO;
import com.ihomziak.clientaccountms.dto.LastNameCountDTO;

import java.util.List;

public interface ClientService {

    List<ClientsInfoDTO> findAll();

    ClientResponseDTO createClient(ClientRequestDTO client);

    ClientResponseDTO deleteByUUID(String uuid);

    ClientResponseDTO updateClient(ClientRequestDTO clientRequestDTO);

    ClientResponseDTO findClientByUUID(String uuid);

    List<ClientResponseDTO> findClientByName(String firstName, String lastName);

    List<ClientsInfoDTO> findUsers(String order, String firstName, String lastName, String email, Integer page, Integer size);

    LastNameCountDTO countClientsByLastName(String lastName, String order);
}