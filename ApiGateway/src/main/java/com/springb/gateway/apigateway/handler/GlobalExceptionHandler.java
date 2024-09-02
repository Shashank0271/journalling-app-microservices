package com.springb.gateway.apigateway.handler;

import com.springb.gateway.apigateway.exceptions.InvalidJwtException;
import com.springb.gateway.apigateway.exceptions.MissingAuthHeaderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingAuthHeaderException.class)
    public ResponseEntity<GatewayError>
    missingAuthHeaderExceptionHandler(MissingAuthHeaderException missingAuthHeaderException) {
        GatewayError gatewayError = new GatewayError(missingAuthHeaderException.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(gatewayError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidJwtException.class)
    public ResponseEntity<GatewayError>
    invalidJwtExceptionHandler(InvalidJwtException invalidJwtException) {
        GatewayError gatewayError = new GatewayError(invalidJwtException.getLocalizedMessage(), HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(gatewayError, HttpStatus.FORBIDDEN);
    }
}
