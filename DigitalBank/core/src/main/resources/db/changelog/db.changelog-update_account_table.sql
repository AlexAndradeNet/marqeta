--liquibase formatted sql
--changeset JohnR:03
--comment: update account table user_id should be varchar
ALTER TABLE
    account ALTER COLUMN user_id TYPE VARCHAR
        USING user_id::VARCHAR;