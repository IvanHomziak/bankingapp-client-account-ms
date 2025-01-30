package com.ihomziak.clientaccountms.exception;

public class ClientAlreadyExistException extends RuntimeException {
    public ClientAlreadyExistException(String msg) {
        super(msg);
    }
}
