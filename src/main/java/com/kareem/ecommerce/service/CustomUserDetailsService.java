package com.kareem.ecommerce.service;

import com.kareem.ecommerce.model.User;
import com.kareem.ecommerce.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByNormalizedUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + " not found");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getNormalizedUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> (GrantedAuthority) () -> "ROLE_" + role.getName()).toList()
        );
    }
}
