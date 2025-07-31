package com.autoever.useradminapplication.global.error;

import com.autoever.useradminapplication.exception.DataNotFoundException;
import com.autoever.useradminapplication.exception.UniqueViolationException;
import com.autoever.useradminapplication.global.GlobalApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 유효성 검사 실패 예외 처리
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalApiResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        // 필드별 에러 메시지 저장
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        // 400 상태 반환
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(GlobalApiResponse.failure("유효성 검사 실패", errors));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GlobalApiResponse<Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return createErrorResponse(e, ErrorCode.INVALID_PARAMETER, e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<GlobalApiResponse<Object>> authorizationDeniedException(AuthorizationDeniedException e) {
        return createErrorResponse(e, ErrorCode.FORBIDDEN, e.getMessage());
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<GlobalApiResponse<Object>> handleNoSuchElementException(DataNotFoundException e) {
        return createErrorResponse(e, ErrorCode.DATA_NOT_FOUND, e.getMessage());
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<GlobalApiResponse<Object>> handleMethodNotSupportedException(Exception e) {
        return createErrorResponse(e, ErrorCode.METHOD_NOT_ALLOWED
                , ErrorCode.METHOD_NOT_ALLOWED.getErrorDescription());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({UniqueViolationException.class})
    public ResponseEntity<GlobalApiResponse<Object>> handleConflictException(Exception e) {
        return createErrorResponse(e, ErrorCode.EXISTS_UNIQUE_VALUE, e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<GlobalApiResponse<Object>> handleUnSupportedMediaType(Exception e) {
        return createErrorResponse(e, ErrorCode.UNSUPPORTED_MEDIA_TYPE
                , ErrorCode.UNSUPPORTED_MEDIA_TYPE.getErrorDescription());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public GlobalApiResponse<Void> handleGeneralException(Exception e) {
        log.error(e.getMessage(), e);
        return GlobalApiResponse.failure("알 수 없는 오류가 발생하였습니다. 관리자에게 문의 바랍니다.", null);
    }

    private ResponseEntity<GlobalApiResponse<Object>> createErrorResponse(Exception e, ErrorCode errorCode, String errorMessage) {

        GlobalApiResponse<Object> errorResponse = GlobalApiResponse.builder()
                .status(errorCode.getStatus())
                .message(errorMessage)
                .data(null)
                .build();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(errorResponse);
    }

}
