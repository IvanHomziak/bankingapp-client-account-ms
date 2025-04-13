alter table client
    add column email VARCHAR(255) NOT NULL UNIQUE;
