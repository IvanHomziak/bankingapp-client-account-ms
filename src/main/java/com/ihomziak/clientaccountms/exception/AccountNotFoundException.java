package com.ihomziak.clientaccountms.exception;

public class AccountNotFoundException  extends RuntimeException {
    public AccountNotFoundException(String msg) {
        super(msg);
    }
}