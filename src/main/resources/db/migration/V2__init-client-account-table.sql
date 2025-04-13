-- Drop the client table if it exists
DROP TABLE IF EXISTS client;

-- Drop the account table if it exists
DROP TABLE IF EXISTS account;

-- Create the client table with a unique constraint on uuid
CREATE TABLE client
(
    client_id     BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
    first_name    VARCHAR(255) NOT NULL,
    last_name     VARCHAR(255) NOT NULL,
    tax_number    VARCHAR(255) NOT NULL UNIQUE,
    date_of_birth VARCHAR(255) NOT NULL,
    phone_number  VARCHAR(255) NOT NULL UNIQUE,
    address       VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6),
    updated_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    uuid          VARCHAR(255) NOT NULL UNIQUE
);