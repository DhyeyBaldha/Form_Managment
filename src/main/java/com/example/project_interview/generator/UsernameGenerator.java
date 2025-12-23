package com.example.project_interview.generator;

import com.example.project_interview.repository.UserRepository;
import lombok.Data;

@Data
public class UsernameGenerator {
    private static UserRepository userRepository;


    public static String generateUsername(String firstname, String lastname) {
        int randomNumber = (int) (Math.random() * 100);

        if(userRepository.existsByUsername(firstname+lastname)){
            return firstname + lastname;
        }
        else {
            return firstname + lastname + randomNumber;
        }
    }
}
