package com.projects.vocalpad.services;

import com.projects.vocalpad.dto.UserDetailsDTO;
import com.projects.vocalpad.dto.UserSignupDTO;
import com.projects.vocalpad.entity.User;
import com.projects.vocalpad.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository uRepo;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserDetailsDTO getUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User user = uRepo.findByUsername(userName);
        return new UserDetailsDTO(user.getFirstname(), user.getLastname(), user.getUsername(), user.getEmail(), null);
    }

    @Transactional
    public void updateUser(UserSignupDTO newUserDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User user = uRepo.findByUsername(userName);
        user.setUsername(newUserDetails.getUsername());
        user.setEmail(newUserDetails.getEmail());
        user.setPassword(passwordEncoder.encode(newUserDetails.getPassword()));
        uRepo.save(user);
    }

    public void deleteAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        uRepo.deleteByUsername(userName);
    }
}