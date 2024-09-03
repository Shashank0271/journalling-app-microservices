package com.example.NotificationService.messaging;

import com.example.NotificationService.dtos.MessageDto;
import com.example.NotificationService.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class InvitationMessageConsumer {
    private final EmailService emailService;

    @RabbitListener(queues = "inviteNotificationQueue")
    public void reviewMessage(MessageDto messageDto) {
        emailService.sendEmail(messageDto.getEmails(), messageDto.getSubject(), messageDto.getBody());
    }
}
