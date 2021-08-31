CREATE TABLE board
(
    board_id                BIGINT AUTO_INCREMENT NOT NULL,
    created_date_time       datetime              NOT NULL,
    last_modified_date_time datetime              NOT NULL,
    deleted_date_time       datetime              NULL,
    member_id               BIGINT                NOT NULL,
    deleted                 BOOLEAN DEFAULT FALSE NOT NULL,
    title                   VARCHAR(30)           NOT NULL,
    content                 LONGTEXT              NOT NULL,
    CONSTRAINT pk_board PRIMARY KEY (board_id)
);

ALTER TABLE board
    ADD CONSTRAINT FK_BOARD_ON_MEMBER FOREIGN KEY (member_id) REFERENCES member (member_id);
