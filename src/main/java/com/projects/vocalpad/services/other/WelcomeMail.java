package com.projects.vocalpad.services.other;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Slf4j
public class WelcomeMail {

    @Autowired
    private EmailService mailService;

    @Value("${spring.mail.username}")
    private String appMail;

    public void greetEmail(String name, String userEmail) throws IOException {
        try {
            Path templatePath = Paths.get("src/main/resources/templates/welcome_template.txt");
            String template = new String(Files.readAllBytes(templatePath));

            String body = template.replace("{name}", name);
             body = body.replace("{contact_email}", appMail);

            mailService.sendEmail(userEmail, "Welcome to Vocal Pad", body);
        } catch (IOException | RuntimeException e) {
            log.error(e.getMessage());
        }
    }
}
