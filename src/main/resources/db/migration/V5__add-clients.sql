INSERT INTO clients_db.client (first_name, last_name, tax_number, date_of_birth, email, phone_number, address, uuid)
VALUES ('John', 'Doe', '123412352389', '1985-07-24', 'johndoe122323@example.com', '+1234523789', '123 Main St, Warsaw, Poland', 'a2c3d234-e567-890f-gh12-3456789ijkl'),
       ('Jane', 'Smith', '987324231321', '1990-12-15', 'janesmi322322th@example.com', '+98765214321', '456 Oak St, Warsaw, Poland', 'c3d434e5-f678-901g-hi23-4567890mnop');

INSERT INTO clients_db.account (account_number, account_type, balance, client_id, uuid)
VALUES ('PL12345678901234567811123456', 'CHECKING', 1000.50, 1, 'a2c3d234-e567-890f-gh12-3456789ijkl'),
       ('PL98765432109876543220987654', 'SAVINGS', 2500.75, 2, 'c3d434e5-f678-901g-hi23-4567890mnop');