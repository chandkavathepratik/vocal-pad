package com.projects.vocalpad.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserSignupDTO {

    @NonNull
    private String username;

    @NonNull
    private String firstname;

    @NonNull
    private String lastname;

    @NonNull
    private String email;

    @NonNull
    private String password;
}
