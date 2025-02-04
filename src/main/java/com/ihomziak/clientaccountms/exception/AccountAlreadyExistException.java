package com.ihomziak.clientaccountms.exception;

public class AccountAlreadyExistException extends RuntimeException {
    public AccountAlreadyExistException(String msg) {
        super(msg);
    }
}
