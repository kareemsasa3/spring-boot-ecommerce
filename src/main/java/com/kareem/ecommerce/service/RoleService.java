package com.kareem.ecommerce.service;

import com.kareem.ecommerce.model.Role;
import com.kareem.ecommerce.model.dto.RoleDTO;
import com.kareem.ecommerce.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;


@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role createNewRole(RoleDTO roleDTO) {
        if (roleRepository.findByName(roleDTO.getName()) != null) {
            throw new IllegalArgumentException("Role already exists");
        }

        Role role = new Role();
        role.setName(roleDTO.getName());
        role.setUsers(new HashSet<>());
        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role updateRole(Long id, RoleDTO roleDTO) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        if (optionalRole.isEmpty()) {
            throw new IllegalArgumentException("Role not found");
        }

        Role role = optionalRole.get();
        role.setName(roleDTO.getName());
        return roleRepository.save(role);
    }
}

