package com.example.jobSearcher.service;

import com.example.jobSearcher.entity.User;
import com.example.jobSearcher.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository=userRepository;
    }

    public UUID register(String userName, String email) {
        User newUser = new User(userName, email);
        userRepository.save(newUser);
        return newUser.getId();
    }

    public boolean isEmailRegistered(String email) {
        User user = userRepository.findByEmail(email);
        return (user!=null);
    }

    public boolean isUserIdExists(UUID userId) {
        return userRepository.existsById(userId);
    }
}
