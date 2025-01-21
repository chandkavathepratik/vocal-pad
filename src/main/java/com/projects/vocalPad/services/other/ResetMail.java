package com.projects.vocalPad.services.other;

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
public class ResetMail {

    @Autowired
    private EmailService mailService;

    @Value("${spring.mail.username}")
    private String appMail;

    public void resetEmail(String name, String userEmail, String token) throws IOException {
        try {
            Path templatePath = Paths.get("src/main/resources/templates/password_reset.txt");
            String template = new String(Files.readAllBytes(templatePath));

            String body = template.replace("{name}", name);
            body = body.replace("{reset_token}", token);

            mailService.sendEmail(userEmail, "Reset token for password reset request", body);
        }
        catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }
}
