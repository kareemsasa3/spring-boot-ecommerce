package com.kareem.ecommerce.service;

import com.kareem.ecommerce.model.Role;
import com.kareem.ecommerce.model.User;
import com.kareem.ecommerce.repository.RoleRepository;
import com.kareem.ecommerce.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    /**
     * Retrieves all users.
     * @return A list of all users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by ID.
     * @param id The ID of the user to retrieve.
     * @return An Optional containing the user if found.
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User createNewUser(String firstName, String lastName, String phoneNumber,
                              String email, String rawUsername, String password) {
        // Check for existing users with the same normalized username
        if (userRepository.findByNormalizedUsername(rawUsername.toLowerCase()) != null) {
            throw new IllegalArgumentException("Username is already in use");
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setNormalizedUsername(rawUsername);
        user.setPassword(passwordEncoder.encode(password));

        Role userRole = roleRepository.findByName("USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }

        user.setRoles(Set.of(userRole));

        return userRepository.save(user);
    }

    /**
     * Updates an existing user.
     * @param userId The ID of the user to update.
     * @param updatedUserDetails The updated user data.
     * @return The updated user, or null if the user does not exist.
     */
    @Transactional // Ensure transactional behavior for the update operation
    public User updateUser(Long userId, User updatedUserDetails) {
        // Retrieve the existing user
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        User existingUser = userOpt.get();

        // Update fields as necessary
        if (updatedUserDetails.getNormalizedUsername() != null) {
            existingUser.setNormalizedUsername(updatedUserDetails.getNormalizedUsername());
        }

        if (updatedUserDetails.getFirstName() != null) {
            existingUser.setFirstName(updatedUserDetails.getFirstName());
        }

        if (updatedUserDetails.getLastName() != null) {
            existingUser.setLastName(updatedUserDetails.getLastName());
        }

        if (updatedUserDetails.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(updatedUserDetails.getPhoneNumber());
        }

        if (updatedUserDetails.getEmail() != null) {
            existingUser.setEmail(updatedUserDetails.getEmail());
        }

        // Update password if provided (ensure it's hashed)
        if (updatedUserDetails.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(updatedUserDetails.getPassword()));
        }

        // Save the updated user
        return userRepository.save(existingUser);
    }

    /**
     * Deletes a user by ID.
     * @param id The ID of the user to delete.
     */
    @Transactional
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            logger.warn("User with ID {} not found for deletion", id);
        }
    }
}
