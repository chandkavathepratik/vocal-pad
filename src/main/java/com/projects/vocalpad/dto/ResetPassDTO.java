package com.projects.vocalpad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class ResetPassDTO {

    @NonNull
    private String username;

    @NonNull
    private String resettoken;

    @NonNull
    private String newpassword;
}
