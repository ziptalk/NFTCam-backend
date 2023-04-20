package com.example.nftcam.exception.handler;

import com.example.nftcam.exception.ApiException;
import com.example.nftcam.exception.custom.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<ApiException> handlerCustomException(CustomException e) {
        log.error("Status: {}, Message: {}", e.getHttpStatus(), e.getMessage());

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ApiException
                        .builder()
                        .message(e.getMessage())
                        .httpStatus(e.getHttpStatus())
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}
