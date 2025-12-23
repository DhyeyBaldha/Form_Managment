package com.example.project_interview.controller;

import com.example.project_interview.utility.ChangePassword;
import com.example.project_interview.entity.Role;
import com.example.project_interview.entity.User;
import com.example.project_interview.services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserServices userServices;

    @PostMapping("/addUserWithImage")
    public User addUser(@RequestBody User user,@RequestParam("file") MultipartFile file) throws IOException {
        return userServices.addUserWithImage(user,file);
    }
    @PostMapping("/addUser")
    public User addUser(@RequestBody User user) throws IOException {
        return userServices.addUser(user);
    }


    @PostMapping("/editProfilePicture")
    public User editProfilePicture(@RequestParam("id") int id,@RequestParam("file") MultipartFile file) throws IOException {
        return userServices.editProfilePicture(id,file);
    }

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers(){
        return userServices.getAllUsers();
    }

    @PutMapping("/changePassword")
    public void changePassword(@RequestBody ChangePassword changePassword){
        userServices.changePassword(changePassword);
    }

    @PutMapping("/editUser")
    public User editUser(@RequestParam("id") int id,@RequestBody User user){
        return userServices.editUser(id,user);
    }

    @DeleteMapping("/deleteUser")
    public void deleteUser(@RequestParam("id") int id){
        userServices.deleteUser(id);
    }

    @GetMapping("/getProfile")
    public User getProfile(){
        return userServices.getProfile();
    }

    @GetMapping("/getUserByUsernameAndRole")
    public User getUserByUsernameAndRole(@RequestParam("username") String username, @RequestParam("role") Role role){
        return userServices.getUserByUsernameAndRole(username, role);
    }
}
