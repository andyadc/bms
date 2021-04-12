package com.andyadc.bms.file.exception;

import com.andyadc.bms.common.BaseException;

public class FileSizeLimitExceededException extends BaseException {
    public FileSizeLimitExceededException(String message) {
        super(message);
    }
}
