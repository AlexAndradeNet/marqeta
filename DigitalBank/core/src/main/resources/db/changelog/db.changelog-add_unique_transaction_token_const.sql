--liquibase formatted sql
--changeset CarlosB:06
--comment: add unique constraint for transaction token

TRUNCATE TABLE
    TRANSACTION;

ALTER TABLE
    TRANSACTION ADD CONSTRAINT uk_transaction_token UNIQUE(transaction_token);