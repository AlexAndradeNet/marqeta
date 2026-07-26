--liquibase formatted sql
--changeset JohnR:01
--comment: create initial tables
CREATE
    TABLE
        account(
            account_id SERIAL PRIMARY KEY,
            user_id BIGINT NOT NULL,
            card_id VARCHAR(255) NOT NULL,
            account_balance DECIMAL(
                10,
                2
            ) NOT NULL,
            reservation_balance DECIMAL(
                10,
                2
            ) NOT NULL
        );

CREATE
    INDEX idx_card_id ON
    account(card_id);

CREATE
    TABLE
        TRANSACTION(
            transaction_id SERIAL PRIMARY KEY,
            transaction_token VARCHAR(50),
            transaction_type VARCHAR(50),
            account_id SERIAL,
            amount DECIMAL(
                10,
                2
            ),
            transaction_status VARCHAR(50),
            payload text,
            FOREIGN KEY(account_id) REFERENCES account(account_id)
        );

CREATE
    INDEX idx_transaction_token ON
    TRANSACTION(transaction_token);