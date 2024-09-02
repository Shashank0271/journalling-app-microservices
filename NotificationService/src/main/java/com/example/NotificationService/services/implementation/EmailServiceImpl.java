package com.example.NotificationService.services.implementation;

import com.example.NotificationService.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(List<String> toEmails, String subject, String body) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            simpleMailMessage.setTo(toEmails.get(0));

            if (toEmails.size() > 1) {
                //convert the rest to a string[]
                List<String> bccList = toEmails.subList(1, toEmails.size());
                String[] bccArray = bccList.toArray(new String[0]);
                simpleMailMessage.setBcc(bccArray);
            }
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(body);

            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            log.info("Cannot send email , " + e);
        }

    }
}
