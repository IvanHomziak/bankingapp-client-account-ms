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

