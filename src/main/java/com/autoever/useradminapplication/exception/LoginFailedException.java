package com.autoever.useradminapplication.exception;

import com.autoever.useradminapplication.global.error.ErrorCode;

public class LoginFailedException extends CommonException {

    public LoginFailedException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
