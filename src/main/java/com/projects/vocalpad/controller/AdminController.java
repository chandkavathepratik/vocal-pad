package com.projects.vocalpad.controller;

import com.projects.vocalpad.dto.UserDetailsDTO;
import com.projects.vocalpad.services.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin")
@Tag(name = "Admin controlled APIs" , description = "Endpoints for Admin")
public class AdminController {

    @Autowired
    private AdminService aService;

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(String subject, String body) {
        try {
            return new ResponseEntity<>(aService.sendEmail(subject, body), HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("Failed to send email", e);
            return new ResponseEntity<>("Failed to send email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserDetailsDTO>> getAllUsers() {
        try {
            return new ResponseEntity<>(aService.getAllUsers(), HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("Failed to get user details", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}