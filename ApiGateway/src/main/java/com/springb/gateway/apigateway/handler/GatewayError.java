package com.springb.gateway.apigateway.handler;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class GatewayError {
    private String error;
    private HttpStatus httpStatus;
    private LocalDateTime timeStamp;

    public GatewayError() {
        this.timeStamp = LocalDateTime.now();
    }

    public GatewayError(String error, HttpStatus httpStatus) {
        this();
        this.error = error;
        this.httpStatus = httpStatus;
    }

}
