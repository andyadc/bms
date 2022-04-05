package com.andyadc.bms.security.exception;

import com.andyadc.bms.exception.BaseException;

public class RequestException extends BaseException {
    public RequestException(String message) {
        super(message);
    }

    public RequestException(String errCode, String message, Throwable cause) {
        super(errCode, message, cause);
    }
}
