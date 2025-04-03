-- Drop the account table if it exists
DROP TABLE IF EXISTS account;

-- Create the account table with a unique constraint on account_number
CREATE TABLE account
(
    account_id     BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(255) NOT NULL UNIQUE,
    account_type   VARCHAR(255),
    balance        FLOAT,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_update    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    client_id      BIGINT(20),
    uuid           VARCHAR(255) NOT NULL UNIQUE,
    CONSTRAINT fk_client FOREIGN KEY (client_id) REFERENCES client (client_id)
);