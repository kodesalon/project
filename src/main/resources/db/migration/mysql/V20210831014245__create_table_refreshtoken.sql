CREATE TABLE refresh_token
(
    refresh_token_id BIGINT AUTO_INCREMENT NOT NULL,
    member_id        BIGINT NULL,
    token            VARCHAR(255) NOT NULL,
    expiry_date      datetime     NOT NULL,
    CONSTRAINT pk_refreshtoken PRIMARY KEY (refresh_token_id)
);

ALTER TABLE refresh_token
    ADD CONSTRAINT uc_refreshtoken_token UNIQUE (token);

ALTER TABLE refresh_token
    ADD CONSTRAINT FK_REFRESHTOKEN_ON_MEMBER FOREIGN KEY (member_id) REFERENCES member (member_id);
