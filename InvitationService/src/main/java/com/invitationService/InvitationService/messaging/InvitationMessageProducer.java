package com.invitationService.InvitationService.messaging;

import com.invitationService.InvitationService.dtos.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class InvitationMessageProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(MessageDto messageDto) {
        rabbitTemplate.convertAndSend("inviteNotificationQueue", messageDto);
    }
}
