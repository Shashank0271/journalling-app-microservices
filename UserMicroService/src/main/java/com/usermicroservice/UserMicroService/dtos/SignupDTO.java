package com.usermicroservice.UserMicroService.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupDTO {
    String email;
    String password;
    String username;
}
