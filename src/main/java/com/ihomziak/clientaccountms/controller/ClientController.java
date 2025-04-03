package com.ihomziak.clientaccountms.controller;

import com.ihomziak.clientaccountms.dto.ClientResponseDTO;
import com.ihomziak.clientaccountms.dto.LastNameCountDTO;
import com.ihomziak.clientaccountms.service.ClientService;
import com.ihomziak.clientaccountms.dto.ClientRequestDTO;
import com.ihomziak.clientaccountms.dto.ClientsInfoDTO;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;

    }

    @PostMapping("/add-client")
    public ResponseEntity<ClientResponseDTO> addClient(@RequestBody @Valid ClientRequestDTO clientRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.clientService.createClient(clientRequestDTO));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ClientResponseDTO> getClient(@PathVariable String uuid) {
        return ResponseEntity.status(HttpStatus.FOUND).body(this.clientService.findClientByUUID(uuid));
    }

    @GetMapping
    public ResponseEntity<List<ClientsInfoDTO>> getClients(
            @RequestParam(value = "order", required = false, defaultValue = "asc") String order,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "page", required = false) String pageStr,
            @RequestParam(value = "size", required = false) String sizeStr
    ) {
        // normalize page & size
        int page = (pageStr == null || pageStr.isBlank()) ? 0 : Integer.parseInt(pageStr);
        int size = (sizeStr == null || sizeStr.isBlank()) ? 10 : Integer.parseInt(sizeStr);

        boolean hasFilters = (firstName != null && !firstName.isBlank())
                || (lastName != null && !lastName.isBlank())
                || (email != null && !email.isBlank());

        List<ClientsInfoDTO> result = hasFilters
                ? clientService.findUsers(order, firstName, lastName, email, page, size)
                : clientService.findAll();

        return ResponseEntity.status(HttpStatus.FOUND).body(result);
    }


    @DeleteMapping("/{uuid}")
    public ResponseEntity<ClientResponseDTO> deleteClient(@PathVariable String uuid) {
        return ResponseEntity.status(HttpStatus.OK).body(this.clientService.deleteByUUID(uuid));
    }

    @PatchMapping("/update")
    public ResponseEntity<ClientResponseDTO> updateClient(@RequestBody @Valid ClientRequestDTO clientRequestDTO) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.clientService.updateClient(clientRequestDTO));
    }

    @GetMapping("/search")
    public ResponseEntity<ClientResponseDTO> searchClient(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName
    ) {
        return ResponseEntity.status(HttpStatus.FOUND).body(this.clientService.findClientByName(firstName, lastName));
    }

    @GetMapping("/count")
    public ResponseEntity<LastNameCountDTO> countClientsByLastName(
            @RequestParam("lastName") String lastName,
            @RequestParam(value = "order", defaultValue = "DESC") String order
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.countClientsByLastName(lastName, order));
    }
}
