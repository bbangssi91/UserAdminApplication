package com.autoever.useradminapplication.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    DATA_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Not Found"),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "Unsupported Media Type"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED.value(), "Method Not Allowed"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST.value(), "Invalid parameter"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "Unauthorized Users"),
    EXISTS_UNIQUE_VALUE(HttpStatus.CONFLICT.value(), "Exists unique value"),
    ;

    private final int status;
    private final String errorDescription;

    ErrorCode(int status, String errorDescription) {
        this.status = status;
        this.errorDescription = errorDescription;
    }
}
