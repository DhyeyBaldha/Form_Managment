package com.example.project_interview.dto;

import com.example.project_interview.entity.IsActive;
import com.example.project_interview.entity.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Integer id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String contactNo;
    private String gender;
    private LocalDate validFrom;
    private LocalDate validTo;
    // allowed values: "true", "false", "delete"
    private IsActive isActive;
    private String language;
    private String description;
    private String profilePicture;
    private Role role;
}
