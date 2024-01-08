package br.com.cleonildo.vuttr.dto;

import br.com.cleonildo.vuttr.entities.Role;
import br.com.cleonildo.vuttr.entities.User;

import java.util.Set;

public record UserResponse(Integer id, String email, String username, Role role) {

    public UserResponse(User user) {
        this(user.getId(), user.getEmail(), user.getUsername(), user.getRole());
    }
}

