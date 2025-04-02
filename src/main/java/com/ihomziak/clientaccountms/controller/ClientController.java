package com.ihomziak.clientaccountms.controller;

import com.ihomziak.clientaccountms.dto.ClientResponseDTO;
import com.ihomziak.clientaccountms.service.ClientService;
import com.ihomziak.clientaccountms.dto.ClientRequestDTO;
import com.ihomziak.clientaccountms.dto.ClientsInfoDTO;
import jakarta.validation.Valid;
import jakarta.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;

    }

    @PostMapping("/clients")
    public ResponseEntity<ClientResponseDTO> addClient(@RequestBody @Valid ClientRequestDTO clientRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.clientService.createClient(clientRequestDTO));
    }

    @GetMapping("/clients/{uuid}")
    public ResponseEntity<ClientResponseDTO> getClient(@PathVariable String uuid) {
        return ResponseEntity.status(HttpStatus.FOUND).body(this.clientService.findClientByUUID(uuid));
    }

    @GetMapping("/clients")
    public ResponseEntity<List<ClientsInfoDTO>> getClients() {
        return ResponseEntity.status(HttpStatus.FOUND).body(this.clientService.findAll());
    }

    @DeleteMapping("/clients/{uuid}")
    public ResponseEntity<ClientResponseDTO> deleteClient(@PathVariable String uuid) {
        return ResponseEntity.status(HttpStatus.OK).body(this.clientService.deleteByUUID(uuid));
    }

    @PatchMapping("/clients/update")
    public ResponseEntity<ClientResponseDTO> updateClient(@RequestBody @Valid ClientRequestDTO clientRequestDTO) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.clientService.updateClient(clientRequestDTO));
    }

    @GetMapping("/clients/search")
    public ResponseEntity<ClientResponseDTO> searchClient(
        @QueryParam("firstName") String firstName,
        @QueryParam("lastName") String lastName
        ) {
        return ResponseEntity.status(HttpStatus.FOUND).body(this.clientService.findClientByName(firstName,lastName));
    }
}
