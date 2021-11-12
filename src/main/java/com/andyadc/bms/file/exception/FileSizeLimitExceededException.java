package com.andyadc.bms.file.exception;

import com.andyadc.bms.exception.BaseException;

public class FileSizeLimitExceededException extends BaseException {
    public FileSizeLimitExceededException(String message) {
        super(message);
    }
}
