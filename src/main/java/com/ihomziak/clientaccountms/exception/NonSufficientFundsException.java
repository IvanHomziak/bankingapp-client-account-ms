package com.ihomziak.clientaccountms.exception;

public class NonSufficientFundsException extends RuntimeException {

    public NonSufficientFundsException(String message) {
        super(message);
    }
}