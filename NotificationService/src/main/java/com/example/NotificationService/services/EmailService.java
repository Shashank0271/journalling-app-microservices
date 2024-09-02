package com.example.NotificationService.services;

import java.util.List;

public interface EmailService {
    void sendEmail(List<String> toEmails, String subject, String body);
}
