package com.example.authservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {
    UserDTO user; //if I am using User , it includes other fields that are a part of the UserDetails interface
    String jwtToken;
}
