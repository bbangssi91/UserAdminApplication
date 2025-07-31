package com.autoever.useradminapplication.exception;

import com.autoever.useradminapplication.global.error.ErrorCode;

public class UnAuthorizedUserException extends CommonException {

    public UnAuthorizedUserException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
