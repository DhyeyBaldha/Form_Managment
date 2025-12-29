package com.example.project_interview.mapper;

import com.example.project_interview.dto.UserDto;
import com.example.project_interview.entity.User;
import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Data
public final class UserMapper {
    private UserMapper() {}

    public static UserDto toDto(User user) {
        if (user == null) return null;
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setUsername(user.getDbUsername());
        dto.setEmail(user.getEmail());
        dto.setContactNo(user.getContactNo());
        dto.setGender(user.getGender());
        dto.setValidFrom(user.getValidFrom());
        dto.setValidTo(user.getValidTo());
        dto.setIsActive(user.getIsActive());
        dto.setLanguage(user.getLanguage());
        dto.setDescription(user.getDescription());
        dto.setProfilePicture(user.getProfilePicture());
        dto.setRole(user.getRole());
        return dto;
    }

    public static List<UserDto> toDtoList(List<User> users) {
        if (users == null) return null;
        return users.stream().filter(Objects::nonNull).map(UserMapper::toDto).collect(Collectors.toList());
    }

    public static User toEntity(UserDto dto) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.getId());
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setContactNo(dto.getContactNo());
        user.setGender(dto.getGender());
        user.setValidFrom(dto.getValidFrom());
        user.setValidTo(dto.getValidTo());
        user.setIsActive(dto.getIsActive());
        user.setLanguage(dto.getLanguage());
        user.setDescription(dto.getDescription());
        user.setProfilePicture(dto.getProfilePicture());
        user.setRole(dto.getRole());
        return user;
    }
}
