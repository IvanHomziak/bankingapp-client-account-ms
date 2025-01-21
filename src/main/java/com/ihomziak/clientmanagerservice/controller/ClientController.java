package com.ihomziak.clientmanagerservice.controller;

import com.ihomziak.clientmanagerservice.dto.ClientResponseDTO;
import com.ihomziak.clientmanagerservice.service.ClientService;
import com.ihomziak.clientmanagerservice.dto.ClientRequestDTO;
import com.ihomziak.clientmanagerservice.dto.ClientsInfoDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;

    }

    @PostMapping
    public ResponseEntity<ClientResponseDTO> addClient(@RequestBody @Valid ClientRequestDTO clientRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.clientService.createClient(clientRequestDTO));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ClientResponseDTO> getClient(@PathVariable String uuid) {
        return ResponseEntity.status(HttpStatus.FOUND).body(this.clientService.findClientByUUID(uuid));
    }

    @GetMapping
    public ResponseEntity<List<ClientsInfoDTO>> getClients() {
        return ResponseEntity.status(HttpStatus.FOUND).body(this.clientService.findAll());
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<ClientResponseDTO> deleteClient(@PathVariable String uuid) {
        return ResponseEntity.status(HttpStatus.OK).body(this.clientService.deleteByUUID(uuid));
    }

    @PatchMapping("/update")
    public ResponseEntity<ClientResponseDTO> updateClient(@RequestBody @Valid ClientRequestDTO clientRequestDTO) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.clientService.updateClient(clientRequestDTO));
    }
}
