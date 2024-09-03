package com.example.NotificationService.controllers;

import com.example.NotificationService.dtos.MessageDto;
import com.example.NotificationService.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
@Slf4j
public class EmailController {
    private final EmailService emailService;

    @PostMapping()
    public ResponseEntity<String> sendMail(@RequestBody MessageDto messageDto) {
        emailService.sendEmail(messageDto.getEmails(), messageDto.getSubject(), messageDto.getBody());
        return ResponseEntity.ok("emails sent successfully");
    }
}
