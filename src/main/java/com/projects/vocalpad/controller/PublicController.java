package com.projects.vocalpad.controller;

import com.projects.vocalpad.dto.UserLoginDTO;
import com.projects.vocalpad.dto.UserSignupDTO;
import com.projects.vocalpad.dto.ResetPassDTO;
import com.projects.vocalpad.services.PublicService;
import com.projects.vocalpad.services.other.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@Slf4j
@Tag(name = "Login - Signup APIs" , description = "Endpoints to register new user. And sign in and reset password of existing user")
public class PublicController {

    @Autowired
    private PublicService pService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/signup")
    @Operation(summary = "Create new user")
    public ResponseEntity<String> signUp(@RequestBody UserSignupDTO userDetails) {
        String response = null;
        try {
            response = pService.createUser(userDetails);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(response, e);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login to existing user account")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO userLoginDTODetails) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDTODetails.getUsername(), userLoginDTODetails.getPassword()));
            return new ResponseEntity<>("Welcome to Vocal Pad", HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Unable to login to account: ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/forgot-password")
    @Operation(summary = "Get reset token on user registered email to reset password")
    public ResponseEntity<String> forgotPassword(@RequestParam String username){
        try {
            return new ResponseEntity<>(pService.forgotPassword(username), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Please try again..", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password for user account")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPassDTO resetDetails){
        try {
            return new ResponseEntity<>(pService.resetPassword(resetDetails), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Please try again..", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/")
    public RedirectView redirectToSwaggerUI() {
        return new RedirectView("/swagger-ui/index.html");
    }
}