package com.example.nftcam.exception.custom;

import org.springframework.http.HttpStatus;

public class InvalidParameterException extends CustomException {

    public InvalidParameterException(String message) {
        super(message);
    }

    public InvalidParameterException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
