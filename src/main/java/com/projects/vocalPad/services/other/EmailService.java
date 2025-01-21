package com.projects.vocalPad.services.other;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    public void sendEmail(String email, String sub, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setReplyTo(username);
        message.setTo(email);
        message.setSubject(sub);
        message.setText(body);
        mailSender.send(message);

    }

    public String sendEmail( String[] cc, String sub, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setReplyTo(username);
        message.setCc(cc);
        message.setSubject(sub);
        message.setText(body);
        mailSender.send(message);
        return "Mail sent";
    }
}