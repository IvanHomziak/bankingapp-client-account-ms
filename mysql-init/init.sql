-- Drop the clients_db database if it exists
DROP DATABASE IF EXISTS clients_db;

-- Create the clients_db database
CREATE DATABASE clients_db;

-- Use the clients_db database
USE clients_db;

-- Drop the client table if it exists
DROP TABLE IF EXISTS client;

-- Drop the account table if it exists
DROP TABLE IF EXISTS account;

-- Create the client table with a unique constraint on uuid
CREATE TABLE client (
                        client_id BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
                        first_name VARCHAR(255) NOT NULL,
                        last_name VARCHAR(255) NOT NULL,
                        tax_number VARCHAR(255) NOT NULL UNIQUE,
                        date_of_birth VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL UNIQUE,
                        phone_number VARCHAR(255) NOT NULL UNIQUE,
                        address VARCHAR(255) NOT NULL,
                        created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6),
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        uuid VARCHAR(255) NOT NULL UNIQUE
);

-- Create the account table with a unique constraint on account_number
CREATE TABLE account (
                         account_id BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
                         account_number VARCHAR(255) NOT NULL UNIQUE,
                         account_type VARCHAR(255),
                         balance FLOAT,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         client_id BIGINT(20),
                         uuid VARCHAR(255) NOT NULL UNIQUE,
                         CONSTRAINT fk_client FOREIGN KEY (client_id) REFERENCES client(client_id)
);

INSERT INTO clients_db.client (first_name, last_name, tax_number, date_of_birth, email, phone_number, address, uuid)
VALUES ('John', 'Doe', '123412352389', '1985-07-24', 'johndoe122323@example.com', '+1234523789', '123 Main St, Warsaw, Poland', 'a2c3d234-e567-890f-gh12-3456789ijkl'),
       ('Jane', 'Smith', '987324231321', '1990-12-15', 'janesmi322322th@example.com', '+98765214321', '456 Oak St, Warsaw, Poland', 'c3d434e5-f678-901g-hi23-4567890mnop');

INSERT INTO clients_db.account (account_number, account_type, balance, client_id, uuid)
VALUES ('PL12345678901234567811123456', 'CHECKING', 1000.50, 1, 'a2c3d234-e567-890f-gh12-3456789ijkl'),
       ('PL98765432109876543220987654', 'SAVINGS', 2500.75, 2, 'c3d434e5-f678-901g-hi23-4567890mnop');

DELIMITER //

CREATE PROCEDURE TransferMoney(
    IN senderAccountUUID VARCHAR(255),
    IN recipientAccountUUID VARCHAR(255),
    IN transferAmount FLOAT,
    OUT transactionStatus VARCHAR(40)
)
BEGIN
    DECLARE senderBalance FLOAT;
    DECLARE recipientBalance FLOAT;

    -- Check if sender account exists and fetch the current balance
    SELECT balance INTO senderBalance
    FROM account
    WHERE uuid = senderAccountUUID;

    -- Check if recipient account exists
    SELECT balance INTO recipientBalance
    FROM account
    WHERE uuid = recipientAccountUUID;

    -- If sender has insufficient funds, fail the transaction
    IF senderBalance < transferAmount THEN
        SET transactionStatus = 'FAILED';
    ELSE
        -- Deduct amount from sender's account
        UPDATE account
        SET balance = balance - transferAmount
        WHERE uuid = senderAccountUUID;

        -- Add amount to recipient's account
        UPDATE account
        SET balance = balance + transferAmount
        WHERE uuid = recipientAccountUUID;

        -- Log transaction as SUCCESS
        SET transactionStatus = 'COMPLETED';
    END IF;
END //

DELIMITER ;

