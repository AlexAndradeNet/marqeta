--liquibase formatted sql
--changeset MartinP:07
--comment: create user table
CREATE
    TABLE
        "users"(
            user_id VARCHAR(255) PRIMARY KEY,
            active BOOLEAN,
            first_name VARCHAR(255),
            last_name VARCHAR(255),
            email VARCHAR(255),
            address1 VARCHAR(255),
            city VARCHAR(255),
            state VARCHAR(255),
            country VARCHAR(255),
            postal_code VARCHAR(255),
            birth_date VARCHAR(10),
            uses_parent_account BOOLEAN,
            corporate_card_holder BOOLEAN,
            created_time TIMESTAMP,
            last_modified_time TIMESTAMP,
            account_holder_group_token VARCHAR(255),
            status VARCHAR(255)
        );