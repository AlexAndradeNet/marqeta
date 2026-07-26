--liquibase formatted sql
--changeset CarlosB:05
--comment: add preceding token and date to transaction
ALTER TABLE
    TRANSACTION ADD COLUMN preceding_transaction_token VARCHAR(50),
    ADD COLUMN transaction_date TIMESTAMP;