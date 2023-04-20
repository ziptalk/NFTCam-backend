package com.example.nftcam.exception.custom;

import org.springframework.http.HttpStatus;

public class InvalidMaxPeopleNumberException extends CustomException {

    public InvalidMaxPeopleNumberException(String message) {
        super(message);
    }

    public InvalidMaxPeopleNumberException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
