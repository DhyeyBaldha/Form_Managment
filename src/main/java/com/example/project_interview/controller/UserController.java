package com.example.project_interview.controller;

import com.example.project_interview.dto.UserDto;
import com.example.project_interview.dto.CommonResponse;
import com.example.project_interview.utility.ChangePassword;
import com.example.project_interview.entity.User;
import com.example.project_interview.services.UserServices;
import com.example.project_interview.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
        import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserServices userServices;

    @PostMapping(
            value = "/addUserWithImage",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<CommonResponse<UserDto>> addUser(
            @RequestPart("user") UserDto user,
            @RequestPart(value = "file", required = false) MultipartFile file,
            HttpServletRequest request
    ) throws IOException {
        User created = userServices.addUserWithImage(UserMapper.toEntity(user), file);
        UserDto dto = UserMapper.toDto(created);
        return ResponseEntity.ok(
                CommonResponse.<UserDto>builder()
                        .timestamp(Instant.now())
                        .status(200)
                        .success(true)
                        .message("User created")
                        .path(request.getRequestURI())
                        .data(dto)
                        .build()
        );
    }

    @PostMapping(value = "/editProfilePicture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse<UserDto>> editProfilePicture(@RequestPart("file") MultipartFile file, HttpServletRequest request) throws IOException {
        UserDto dto = UserMapper.toDto(userServices.editProfilePicture(file));
        return ResponseEntity.ok(
                CommonResponse.<UserDto>builder()
                        .timestamp(Instant.now())
                        .status(200)
                        .success(true)
                        .message("Profile picture updated")
                        .path(request.getRequestURI())
                        .data(dto)
                        .build()
        );
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<CommonResponse<List<UserDto>>> getAllUsers(HttpServletRequest request){
        List<UserDto> list = UserMapper.toDtoList(userServices.getAllUsers());
        return ResponseEntity.ok(
                CommonResponse.<List<UserDto>>builder()
                        .timestamp(Instant.now())
                        .status(200)
                        .success(true)
                        .message("Users fetched")
                        .path(request.getRequestURI())
                        .data(list)
                        .build()
        );
    }

    @PutMapping("/changePassword")
    public ResponseEntity<CommonResponse<String>> changePassword(@RequestBody ChangePassword changePassword, HttpServletRequest request){
        String result = userServices.changePassword(changePassword);
        return ResponseEntity.ok(
                CommonResponse.<String>builder()
                        .timestamp(Instant.now())
                        .status(200)
                        .success(true)
                        .message("Password changed")
                        .path(request.getRequestURI())
                        .data(result)
                        .build()
        );
    }

    @PutMapping(value = "/editUser/{id}")
    public ResponseEntity<CommonResponse<UserDto>> editUser(
            @PathVariable("id") int id,
            @RequestPart("user") UserDto user,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "removeProfilePicture",required = false) String removeProfilePicture,
            HttpServletRequest request
    ) throws IOException {
        boolean removeProfilePicture1 = "true".equalsIgnoreCase(removeProfilePicture);
        UserDto dto = UserMapper.toDto(userServices.editUser(id, UserMapper.toEntity(user), file,removeProfilePicture1));
        return ResponseEntity.ok(
                CommonResponse.<UserDto>builder()
                        .timestamp(Instant.now())
                        .status(200)
                        .success(true)
                        .message("User updated")
                        .path(request.getRequestURI())
                        .data(dto)
                        .build()
        );
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<CommonResponse<Void>> deleteUser(@PathVariable int id, HttpServletRequest request){
        userServices.deleteUser(id);
        return ResponseEntity.ok(
                CommonResponse.<Void>builder()
                        .timestamp(Instant.now())
                        .status(200)
                        .success(true)
                        .message("User deleted")
                        .path(request.getRequestURI())
                        .data(null)
                        .build()
        );
    }

    @GetMapping("/getProfile")
    public ResponseEntity<CommonResponse<UserDto>> getProfile(HttpServletRequest request){
        UserDto dto = UserMapper.toDto(userServices.getProfile());
        return ResponseEntity.ok(
                CommonResponse.<UserDto>builder()
                        .timestamp(Instant.now())
                        .status(200)
                        .success(true)
                        .message("Profile fetched")
                        .path(request.getRequestURI())
                        .data(dto)
                        .build()
        );
    }

    @GetMapping("/getUserByUsernameAndRole")
    public ResponseEntity<CommonResponse<List<UserDto>>> getUserByUsernameAndRole(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "role", required = false) String role,
            HttpServletRequest request
    ){
        List<UserDto> list = UserMapper.toDtoList(userServices.getUserByUsernameAndRole(username, role));
        return ResponseEntity.ok(
                CommonResponse.<List<UserDto>>builder()
                        .timestamp(Instant.now())
                        .status(200)
                        .success(true)
                        .message("Users fetched")
                        .path(request.getRequestURI())
                        .data(list)
                        .build()
        );
    }
}
