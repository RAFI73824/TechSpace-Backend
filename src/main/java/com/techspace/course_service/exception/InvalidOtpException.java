package com.techspace.course_service.exception;

public class InvalidOtpException extends RuntimeException {

    // Constructor that accepts a message
    public InvalidOtpException(String message) {
        super(message);
    }
}