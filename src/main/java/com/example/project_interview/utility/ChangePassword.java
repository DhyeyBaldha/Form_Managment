package com.example.project_interview.utility;

import lombok.Data;

@Data
public class ChangePassword {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
