ALTER TABLE refresh_token
DROP
FOREIGN KEY FK_REFRESHTOKEN_ON_MEMBER;

ALTER TABLE refresh_token
DROP
KEY uc_refreshtoken_token;

DROP TABLE refresh_token;
