--liquibase formatted sql
--changeset JohnR:04
--comment: create card table
CREATE
    TABLE
        card(
            card_id VARCHAR PRIMARY KEY,
            user_id VARCHAR(255),
            state VARCHAR(255),
            reason VARCHAR(255),
            fulfillment_status VARCHAR(255),
            pan VARCHAR(255)
        );