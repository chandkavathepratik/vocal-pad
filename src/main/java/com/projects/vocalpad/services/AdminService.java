package com.projects.vocalpad.services;

import com.projects.vocalpad.dto.UserDetailsDTO;
import com.projects.vocalpad.entity.User;
import com.projects.vocalpad.repo.UserRepository;
import com.projects.vocalpad.services.other.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private EmailService mailService;

    @Autowired
    private UserRepository uRepo;

    public String sendEmail(String subject, String body) {
        try {
            List<User> allUser = uRepo.findAll();
            List<String> emailIds = new ArrayList<>();
            for(User user:allUser){
                emailIds.add(user.getEmail());
            }
            return mailService.sendEmail(emailIds.toArray(new String[0]), subject, body);
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    public List<UserDetailsDTO> getAllUsers() {
        List<User> users = uRepo.findAll();

        List<UserDetailsDTO> allUsers = new ArrayList<>();

        for(User user: users){
            allUsers.add( new UserDetailsDTO(user.getFirstname(), user.getLastname(), user.getUsername(), user.getEmail(), user.getUserRoles()));
        }
        return allUsers;
    }
}