package com.smallbiz.inventory.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    // 🔥 pulls from application.yml
    @Value("${inventory.alert.recipients}")
    private String recipients;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAlert(String subject, String message) {

        // 🔥 split multiple emails
        String[] emails = recipients.split(",");

        for (String email : emails) {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(email.trim());
            mail.setSubject(subject);
            mail.setText(message);

            mailSender.send(mail);
        }
    }
}