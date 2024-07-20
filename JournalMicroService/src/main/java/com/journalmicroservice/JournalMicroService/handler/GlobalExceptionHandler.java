package com.journalmicroservice.JournalMicroService.handler;

import com.journalmicroservice.JournalMicroService.Exceptions.JournalEntryNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(JournalEntryNotFoundException.class)
    public ResponseEntity<Map<String, ?>> handleUserNotFoundException(JournalEntryNotFoundException entryNotFoundException) {
        logger.trace(entryNotFoundException.getMessage());
        return new ResponseEntity<>(
                new LinkedHashMap<>() {{
                    put("message", entryNotFoundException.getMessage());
                    put("success", false);
                    put("status", HttpStatus.NOT_FOUND);
                }},
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, ?>> handleConstraintViolationException(ConstraintViolationException constraintViolationException) {
        List<String> violations = constraintViolationException.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new LinkedHashMap<>() {{
                    put("message", violations);
                    put("success", false);
                    put("status", HttpStatus.BAD_REQUEST);
                }},
                HttpStatus.BAD_REQUEST
        );
    }

}
