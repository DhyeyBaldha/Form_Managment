package com.example.project_interview.utility;

import lombok.Data;

@Data
public class ChangePassword {
    private int id;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
