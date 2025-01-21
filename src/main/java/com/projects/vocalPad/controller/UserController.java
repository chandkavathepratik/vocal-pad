package com.projects.vocalPad.controller;

import com.projects.vocalPad.dto.UserDetailsDTO;
import com.projects.vocalPad.dto.UserSignupDTO;
import com.projects.vocalPad.entity.User;
import com.projects.vocalPad.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
@Tag(name = "User Account Management APIs", description = "To manage user account operations")
public class UserController {

    @Autowired
    private UserService uService;

    @GetMapping
    @Operation(summary = "Get User Details")
    public ResponseEntity<UserDetailsDTO> getUserDetails(){
        try {
            return new ResponseEntity<>(uService.getUserDetails(), HttpStatus.FOUND);
        } catch (Exception e) {
            log.error("Failed to get user details", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    @Operation(summary = "Update user details")
    public ResponseEntity<String> updateUser(@RequestBody UserSignupDTO userDetails){
        try {
            uService.updateUser(userDetails);
            return new ResponseEntity<>("Account details updated..", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to update user details", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    @Operation(summary = "Delete account")
    public ResponseEntity<String> deleteAccount(){
        try {
            uService.deleteAccount();
            return new ResponseEntity<>("Account deleted successfully...!", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to delete account", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}