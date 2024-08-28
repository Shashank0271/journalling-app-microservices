package com.example.authservice.dtos;

import com.example.authservice.entities.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private String email;
    private String password;
    private String username;
    private String id;
    private String about;
    private Image profilePicture;
}
