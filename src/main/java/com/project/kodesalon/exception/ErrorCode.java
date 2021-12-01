package com.project.kodesalon.exception;

public class ErrorCode {

    private ErrorCode() {
    }

    // Member
    public static final String NOT_EXIST_MEMBER_ALIAS = "M001";
    public static final String ALREADY_EXIST_MEMBER_ALIAS = "M002";
    public static final String INVALID_MEMBER_PASSWORD = "M003";
    public static final String INVALID_MEMBER_ALIAS = "M004";
    public static final String INVALID_MEMBER_EMAIL = "M005";
    public static final String INVALID_MEMBER_NAME = "M006";
    public static final String INVALID_MEMBER_PHONE = "M007";
    public static final String NOT_EXIST_MEMBER = "M008";
    public static final String DUPLICATED_PASSWORD = "M009";
    public static final String INCORRECT_PASSWORD = "M010";

    // Board
    public static final String INVALID_BOARD_TITLE = "B001";
    public static final String INVALID_BOARD_CONTENT = "B002";
    public static final String NOT_EXIST_BOARD = "B003";
    public static final String NOT_AUTHORIZED_MEMBER = "B004";
    public static final String ALREADY_DELETED_BOARD = "B005";
    public static final String INVALID_BOARD_IMAGES_SIZE = "B006";

    // SESSION
    public static final String INVALID_SESSION = "S001";

    // Date Time
    public static final String INVALID_DATE_TIME = "D001";

    // Image
    public static final String INVALID_IMAGE = "I001";
    public static final String NOT_EXIST_IMAGE = "I002";

    // S3, FILE
    public static final String CLOUD_ERROR = "E001";
    public static final String INVALID_FILE = "E002";
}
