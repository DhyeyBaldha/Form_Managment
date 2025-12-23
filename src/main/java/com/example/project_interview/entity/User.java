package com.example.project_interview.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "User")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User  implements UserDetails {
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

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "contact no")
    private String contactNo;

    @Column(name = "gender")
    private String gender;

    @Column(name = "valid from")
    private Date validFrom;

    @Column(name = "valid to")
    private Date validTo;

    @Column(name = "is active")
    private boolean isActive;

    @Column(name = "language")
    private String language;

    @Column(name = "description")
    private String description;

    @Column(name = "profile picture")
    private byte[] profilePicture;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @ElementCollection
    @Column(name = "formId")
    private List<String> FormId;

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
}
