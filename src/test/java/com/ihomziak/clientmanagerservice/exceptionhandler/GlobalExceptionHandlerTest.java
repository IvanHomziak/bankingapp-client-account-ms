package com.ihomziak.clientmanagerservice.exceptionhandler;

import com.ihomziak.clientmanagerservice.exception.*;
import com.ihomziak.transactioncommon.dto.ErrorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        globalExceptionHandler = new GlobalExceptionHandler();
        webRequest = mock(WebRequest.class);
    }

    @Test
    void testHandleClientNotFoundException() {
        ClientNotFoundException ex = new ClientNotFoundException("Client not found");
        ResponseEntity<ErrorDTO> response = globalExceptionHandler.handleException(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Client not found", response.getBody().getError());
    }

    @Test
    void testHandleAccountNotFoundException() {
        AccountNotFoundException ex = new AccountNotFoundException("Account not found");
        ResponseEntity<ErrorDTO> response = globalExceptionHandler.handleException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Account not found", response.getBody().getError());
    }

    @Test
    void testHandleAccountAlreadyExistException() {
        AccountAlreadyExistException ex = new AccountAlreadyExistException("Account already exists");
        ResponseEntity<ErrorDTO> response = globalExceptionHandler.handleException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Account already exists", response.getBody().getError());
    }

    @Test
    void testHandleClientAlreadyExistException() {
        ClientAlreadyExistException ex = new ClientAlreadyExistException("Client already exists");
        ResponseEntity<ErrorDTO> response = globalExceptionHandler.handleException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Client already exists", response.getBody().getError());
    }

    @Test
    void testHandleAccountNumberQuantityException() {
        AccountNumberQuantityException ex = new AccountNumberQuantityException("Exceeded account limit");
        ResponseEntity<ErrorDTO> response = globalExceptionHandler.handleException(ex, webRequest);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Exceeded account limit", response.getBody().getError());
    }

    @Test
    void testHandleNonSufficientFundsException() {
        NonSufficientFundsException ex = new NonSufficientFundsException("Insufficient funds");
        ResponseEntity<ErrorDTO> response = globalExceptionHandler.handleException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Insufficient funds", response.getBody().getError());
    }

    @Test
    void testHandleUnknownException() {
        Exception ex = new Exception("Unknown error");
        ResponseEntity<ErrorDTO> response = globalExceptionHandler.handleException(ex, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unknown error", response.getBody().getError());
    }
}

