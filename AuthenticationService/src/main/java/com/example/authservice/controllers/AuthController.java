package com.example.authservice.controllers;

import com.example.authservice.dtos.AuthResponseDTO;
import com.example.authservice.dtos.LoginDTO;
import com.example.authservice.dtos.SignupDTO;
import com.example.authservice.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(authService.login(loginDTO));
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDTO> signup
            (@ModelAttribute SignupDTO signupDTO, @RequestParam(value = "file", required = false) MultipartFile userProfilePic) {
        return ResponseEntity.ok(authService.signUp(signupDTO, userProfilePic));
    }

}
