-- Active: 1692018317545@@127.0.0.1@5432@postgres@restfullapi

CREATE SCHEMA RestFullAPI;


CREATE TABLE users (
    username VARCHAR(100) NOT NULL PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    token VARCHAR(100),
    token_expired_at BIGINT,
    UNIQUE(token)
);

SELECT * FROM restfullapi.users;

DROP SCHEMA restfull CASCADE;
DROP SCHEMA public CASCADE;


SELECT table_name, column_name, is_nullable, data_type, character_maximum_length
FROM information_schema."columns"
WHERE table_name = 'users';

CREATE TABLE contacts (
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100),
    phone VARCHAR(100),
    email VARCHAR(100),
    CONSTRAINT fk_users_contact
    FOREIGN KEY (username)
    REFERENCES users(username)
);

SELECT table_name, column_name, is_nullable, data_type, character_maximum_length
FROM information_schema."columns"
WHERE table_name = 'contacts';

select * from contacts;

CREATE TABLE addresses (
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    contact_id VARCHAR(100) NOT NULL,
    street VARCHAR(200),
    city VARCHAR(100),
    province VARCHAR(100),
    country VARCHAR(100) NOT NULL,
    postal_code VARCHAR(10),
    CONSTRAINT fk_contact_addresses
    FOREIGN KEY (contact_id) 
    REFERENCES contacts(id)
);

SELECT table_name, column_name, is_nullable, data_type, character_maximum_length
FROM information_schema."columns"
WHERE table_name = 'addresses';

DELETE FROM addresses;

DELETE FROM contacts;

DELETE FROM users;