package com.ihomziak.clientaccountms.controller;

import static com.ihomziak.clientaccountms.util.constants.Endpoints.ClientEndpoints.*;

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
@RequestMapping(API_CLIENT_V1)
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;

    }

    @PostMapping(ADD_CLIENT)
    public ResponseEntity<ClientResponseDTO> addClient(@RequestBody @Valid ClientRequestDTO clientRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.clientService.createClient(clientRequestDTO));
    }

    @GetMapping(GET_CLIENT)
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


    @DeleteMapping(DELETE_CLIENT)
    public ResponseEntity<ClientResponseDTO> deleteClient(@PathVariable String uuid) {
        return ResponseEntity.status(HttpStatus.OK).body(this.clientService.deleteByUUID(uuid));
    }

    @PatchMapping(UPDATE_CLIENT)
    public ResponseEntity<ClientResponseDTO> updateClient(@RequestBody @Valid ClientRequestDTO clientRequestDTO) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.clientService.updateClient(clientRequestDTO));
    }

    @GetMapping(SEARCH_CLIENT)
    public ResponseEntity<List<ClientResponseDTO>> searchClient(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName
    ) {
        return ResponseEntity.status(HttpStatus.FOUND).body(this.clientService.findClientByName(firstName, lastName));
    }

    @GetMapping(COUNT_CLIENTS_BY_LAST_NAME)
    public ResponseEntity<LastNameCountDTO> countClientsByLastName(
            @RequestParam("lastName") String lastName,
            @RequestParam(value = "order", defaultValue = "DESC") String order
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.countClientsByLastName(lastName, order));
    }
}
