package com.autoever.useradminapplication.exception;

import com.autoever.useradminapplication.global.error.ErrorCode;

public class UniqueViolationException extends CommonException {

    public UniqueViolationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
