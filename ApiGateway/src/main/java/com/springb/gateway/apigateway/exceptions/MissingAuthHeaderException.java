package com.springb.gateway.apigateway.exceptions;

public class MissingAuthHeaderException extends RuntimeException {
    public MissingAuthHeaderException(String message) {
        super(message);
    }
}
