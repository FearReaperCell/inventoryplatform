package com.smallbiz.inventory.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAlert(String subject, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("example@gmail.com");
        mail.setSubject(subject);
        mail.setText(message);

        mailSender.send(mail);
    }
}