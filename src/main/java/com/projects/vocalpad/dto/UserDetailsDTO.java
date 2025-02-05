package com.projects.vocalpad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {

    @NonNull
    private String firstname;

    @NonNull
    private String lastname;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    @NonNull
    //@Email(regexp = "[a-z0-9._%+-] + @[a-z]{2,3}" , flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;

    Set<String> userRoles = new HashSet<>();

}