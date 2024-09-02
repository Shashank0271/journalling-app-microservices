package com.example.NotificationService.controllers;

import com.example.NotificationService.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
@Slf4j
public class EmailController {
    private final EmailService emailService;

    @PostMapping()
    public ResponseEntity<?> sendMail(@RequestBody Map<String, List<String>> body) {
        emailService.sendEmail(body.get("emails"), "dummy subject", "dummy body");
        return ResponseEntity.ok("emails sent successfully");
    }
}
