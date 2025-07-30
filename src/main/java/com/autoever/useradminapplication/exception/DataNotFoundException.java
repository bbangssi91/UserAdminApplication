package com.autoever.useradminapplication.exception;

import com.autoever.useradminapplication.global.error.ErrorCode;

public class DataNotFoundException extends CommonException {

    public DataNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
