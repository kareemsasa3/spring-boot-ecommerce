package com.kareem.ecommerce.controller;

import com.kareem.ecommerce.model.User;
import com.kareem.ecommerce.model.dto.UserDTO;
import com.kareem.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint to retrieve all users.
     * @return A list of all users.
     */
    @GetMapping("/")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Endpoint to retrieve a user by ID.
     * @param id The user ID.
     * @return The user, or a 404 status if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    @PostMapping("/")
    public ResponseEntity<?> createNewUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        try {
            // Assuming that userService.registerUser creates a new user
            User newUser = userService.createNewUser(
                    userDTO.getFirstName(),
                    userDTO.getLastName(),
                    userDTO.getPhoneNumber(),
                    userDTO.getEmail(),
                    userDTO.getRawUsername(),
                    userDTO.getPassword() // Ensure this is hashed in the service
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(newUser); // HTTP 201
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
        }
    }

    /**
     * Endpoint to update an existing user.
     * @param id The ID of the user to update.
     * @param updatedUser The updated user data.
     * @return The updated user, or a 404 status if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(404).body(null); // Return not found status if user doesn't exist
        }
    }

    /**
     * Endpoint to delete a user by ID (Optional).
     * @param id The user ID.
     * @return A 200 OK status if successful, or 404 if the user doesn't exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build(); // Return 200 OK even if user doesn't exist
    }
}
