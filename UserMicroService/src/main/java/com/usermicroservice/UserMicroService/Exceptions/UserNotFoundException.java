package com.usermicroservice.UserMicroService.Exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserNotFoundException extends RuntimeException {
    private String userId;
    private String message;

    public UserNotFoundException(String userId) {
        this.userId = userId;
        this.message = "User with userId = " + userId + " not found";
    }
}
