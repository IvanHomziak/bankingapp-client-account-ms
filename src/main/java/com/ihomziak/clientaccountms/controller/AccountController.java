package com.ihomziak.clientaccountms.controller;

import com.ihomziak.clientaccountms.dto.AccountInfoDTO;
import com.ihomziak.clientaccountms.service.AccountService;
import com.ihomziak.clientaccountms.dto.AccountRequestDTO;
import com.ihomziak.clientaccountms.dto.AccountResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/V1/account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<AccountInfoDTO> getAccount(@PathVariable String uuid) {
        return ResponseEntity.status(HttpStatus.FOUND).body(this.accountService.findAccountByUuid(uuid));
    }

    @GetMapping("/list/{uuid}")
    public ResponseEntity<List<AccountResponseDTO>> getClientAccounts(@PathVariable String uuid) {
        return ResponseEntity.status(HttpStatus.FOUND).body(this.accountService.findAllAccountsByClientUUID(uuid));
    }

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createCheckingAccount(@RequestBody @Valid AccountRequestDTO accountRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.accountService.createCheckingAccount(accountRequestDTO));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<AccountInfoDTO> deleteAccount(@PathVariable String uuid) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.accountService.deleteAccount(uuid));
    }

    @PatchMapping
    public ResponseEntity<AccountResponseDTO> updateAccount(@RequestBody @Valid AccountRequestDTO accountRequestDTO) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.accountService.updateAccount(accountRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<AccountInfoDTO>> getAccounts() {
        return ResponseEntity.status(HttpStatus.FOUND).body(this.accountService.findAllAccounts());
    }

}
