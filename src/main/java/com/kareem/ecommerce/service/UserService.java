package com.kareem.ecommerce.service;

import com.kareem.ecommerce.model.User;
import com.kareem.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    // Create operation
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Read operation (Retrieve all users)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Read operation (Retrieve user by ID)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Update operation
    public User updateUser(Long id, User updatedUser) {
        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setUsername(updatedUser.getUsername()); // Update fields as needed
            existingUser.setEmail(updatedUser.getEmail());
            // Set other fields as needed
            existingUser.setPassword(updatedUser.getPassword());
            return userRepository.save(existingUser);
        } else {
            // Handle error (user not found)
            return null;
        }
    }

    // Delete operation
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }}
