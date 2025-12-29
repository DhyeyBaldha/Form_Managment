package com.example.project_interview.entity;

import com.example.project_interview.dto.LoginDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "User")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User extends LoginDto implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "first name")
    private String firstname;

    @Column(name = "last name")
    private String lastname;

    @Column(unique = true, name = "username")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    private String contactNo;

    @Column(name = "gender")
    private String gender;

    @Column(name = "valid from")
    private LocalDate validFrom;

    @Column(name = "valid to")
    private LocalDate validTo;

    @Column(name = "is active")
    @Enumerated(value = EnumType.STRING)
    private IsActive isActive;

    @Column(name = "language")
    private String language;

    @Column(name = "description")
    private String description;

    @Column(name = "profile_picture",columnDefinition = "TEXT")
    private String profilePicture;


    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Form> forms;
    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<FilledForm> filledForms;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }

    @JsonProperty("username")
    public String getDbUsername() {
        return username;
    }



}
