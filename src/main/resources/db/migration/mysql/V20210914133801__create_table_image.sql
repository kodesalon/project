CREATE TABLE image
(
    image_id  BIGINT AUTO_INCREMENT NOT NULL,
    image_url VARCHAR(255)          NOT NULL,
    image_key VARCHAR(255)          NOT NULL,
    board_id  BIGINT                NOT NULL,
    CONSTRAINT pk_image PRIMARY KEY (image_id)
);

ALTER TABLE image
    ADD CONSTRAINT image_unique_constraints UNIQUE (image_url, image_key);

ALTER TABLE image
    ADD CONSTRAINT uc_image_image_key UNIQUE (image_key);

ALTER TABLE image
    ADD CONSTRAINT uc_image_image_url UNIQUE (image_url);

ALTER TABLE image
    ADD CONSTRAINT FK_IMAGE_ON_BOARD FOREIGN KEY (board_id) REFERENCES board (board_id);
