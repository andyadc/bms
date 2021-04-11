package com.andyadc.bms.common;

public enum RespCode {

    SUCC("000", "Success"),
    UNKNOWN("500", "Unknown error"),
    PENDING("999", "Please wait"),
    AUTHENTICATION_FAIL("010", "Invalid username or password"),
    JWT_TOKEN_EXPIRED("020", "Token has expired"),
    PERMISSION_MISSED("030", "User doesn't have any privileges"),
    ;

    private final String code;
    private final String message;

    RespCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
