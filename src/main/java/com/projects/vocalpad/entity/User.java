package com.projects.vocalpad.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Document(collection = "users")
@NoArgsConstructor
public class User {

    @Id
    private ObjectId userId;

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

    @NonNull
    private String password;

    Set<String> userRoles = new HashSet<>();

    @DBRef
    private List<Note> notes = new ArrayList<>();

    @DBRef
    private List<Note> archive = new ArrayList<>();

    @DBRef
    private List<Note> trash = new ArrayList<>();

    private String resetToken = "";

    private LocalDateTime tokenExpiry;

}