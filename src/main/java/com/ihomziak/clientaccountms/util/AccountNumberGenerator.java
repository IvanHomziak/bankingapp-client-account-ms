package com.ihomziak.clientaccountms.util;

import java.util.Random;

public class AccountNumberGenerator {

    private static final int ACCOUNT_NUMBER_LENGTH = 18;

    public static String generateBankAccountNumber() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder();

        while (accountNumber.length() < ACCOUNT_NUMBER_LENGTH) {
            int digit = random.nextInt(10); // Generates a digit between 0 and 9
            accountNumber.append(digit);
        }

        return accountNumber.toString();
    }
}