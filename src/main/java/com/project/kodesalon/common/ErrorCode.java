package com.project.kodesalon.common;

public class ErrorCode {

    // Member
    public static final String NOT_EXIST_MEMBER_ALIAS = "M001";
    public static final String ALREADY_EXIST_MEMBER_ALIAS = "M002";
    public static final String INVALID_MEMBER_PASSWORD = "M003";
    public static final String INVALID_MEMBER_ALIAS = "M004";
    public static final String INVALID_MEMBER_EMAIL = "M005";
    public static final String INVALID_MEMBER_NAME = "M006";
    public static final String INVALID_MEMBER_PHONE = "M007";
    public static final String NOT_EXIST_MEMBER = "M008";
    public static final String PASSWORD_DUPLICATION = "M009";
    public static final String PASSWORD_NOT_CORRECT = "M010";

    // Board
    public static final String INVALID_BOARD_TITLE = "B001";
    public static final String INVALID_BOARD_CONTENT = "B002";

    // JWT
    public static final String EXPIRED_JWT_TOKEN = "J001";
    public static final String INVALID_JWT_TOKEN = "J002";
    public static final String INVALID_HEADER = "J003";

    // Date Time
    public static final String INVALID_DATE_TIME = "D001";
}
