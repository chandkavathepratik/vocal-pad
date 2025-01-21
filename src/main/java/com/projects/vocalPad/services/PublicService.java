package com.projects.vocalPad.services;

import com.projects.vocalPad.dto.UserSignupDTO;
import com.projects.vocalPad.entity.User;
import com.projects.vocalPad.dto.ResetPassDTO;
import com.projects.vocalPad.repo.UserRepository;
import com.projects.vocalPad.services.other.ResetMail;
import com.projects.vocalPad.services.other.WelcomeMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PublicService {

    @Autowired
    private UserRepository uRepo;

    @Autowired
    private ResetMail rMail;

    @Autowired
    private WelcomeMail wMail;

    private static final PasswordEncoder passEncoder = new BCryptPasswordEncoder();

    @Transactional
    public String createUser(UserSignupDTO loginDetails) throws IOException {
        if (uRepo.existsByUsername(loginDetails.getUsername())) {
            return ("Username already taken..!");
        }

        if (uRepo.existsByEmail(loginDetails.getEmail())) {
            return("Email already associated with another account..!");
        }
        User user = new User();

        user.setUsername(loginDetails.getUsername());
        user.setFirstname(loginDetails.getFirstname());
        user.setLastname(loginDetails.getLastname());
        user.setEmail(loginDetails.getEmail());
        user.setPassword(passEncoder.encode(loginDetails.getPassword()));
        user.getUserRoles().add("User");
        uRepo.save(user);
        wMail.greetEmail(user.getFirstname()+" "+ user.getLastname(), user.getEmail());
        return "Account created Successfully";
    }

    @Transactional
    public String forgotPassword(String username) throws IOException {
        User user = uRepo.findByUsername(username);
        if(user==null){
            return "Enter correct username";
        }

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setTokenExpiry(LocalDateTime.now().plusMinutes(30));
        uRepo.save(user);

        rMail.resetEmail(user.getFirstname() +" "+ user.getLastname(), user.getEmail(), token);
        return "Reset pin sent to your registered mail id";

    }

    @Transactional
    public String resetPassword(ResetPassDTO resetDetails) {
        User user = uRepo.findByUsername(resetDetails.getUsername());
        if(user==null){
            return "Enter correct username";
        }
        else if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            user.setTokenExpiry(null);
            user.setResetToken("");
            return "Reset token expired";
        }
        else if(user.getResetToken()!=null && resetDetails.getResettoken().equals(user.getResetToken())){
            user.setPassword(passEncoder.encode(resetDetails.getNewpassword()));
            user.setResetToken("");
            uRepo.save(user);
            return "Password reset successful";
        }
        return "Failed to reset password";
    }
}