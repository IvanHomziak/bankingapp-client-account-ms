package com.ihomziak.clientaccountms.exception;

public class AccountDeletionConflictException extends RuntimeException {
    public AccountDeletionConflictException(String message) {
        super(message);
    }
}
