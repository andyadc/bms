package com.andyadc.bms.security.exception;

import com.andyadc.bms.common.BaseException;

public class InvalidJwtTokenException extends BaseException {
    public InvalidJwtTokenException(String message) {
        super(message);
    }

    public InvalidJwtTokenException(String errCode, String message) {
        super(errCode, message);
    }
}
