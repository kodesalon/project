CREATE TABLE member
(
    member_id               BIGINT AUTO_INCREMENT NOT NULL,
    created_date_time       datetime              NOT NULL,
    last_modified_date_time datetime              NOT NULL,
    deleted_date_time       datetime NULL,
    deleted                 BOOLEAN DEFAULT FALSE NOT NULL,
    alias                   VARCHAR(15)           NOT NULL,
    email                   VARCHAR(255)          NOT NULL,
    phone                   VARCHAR(20) NULL,
    password                VARCHAR(16)           NOT NULL,
    name                    VARCHAR(17)           NOT NULL,
    CONSTRAINT pk_member PRIMARY KEY (member_id)
);

ALTER TABLE member
    ADD CONSTRAINT member_unique_constraint UNIQUE (alias);
