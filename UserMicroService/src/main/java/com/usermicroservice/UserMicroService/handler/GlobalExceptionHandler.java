package com.usermicroservice.UserMicroService.handler;

import com.usermicroservice.UserMicroService.Exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, ?>> handleUserNotFoundException(UserNotFoundException userNotFoundException) {
        logger.trace(userNotFoundException.getMessage());
        return new ResponseEntity<>(
                new LinkedHashMap<>() {{
                    put("message", userNotFoundException.getMessage());
                    put("success", false);
                    put("status", HttpStatus.NOT_FOUND);
                }},
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String , ?>> handleRunTimeException(RuntimeException runtimeException){
        return new ResponseEntity<>(
                new LinkedHashMap<>(){{
                    put("message" , runtimeException.getMessage());
                    put("success" , false);
                    put("status" , HttpStatus.INTERNAL_SERVER_ERROR);
                }},
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
