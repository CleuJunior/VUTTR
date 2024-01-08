package br.com.cleonildo.vuttr.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "Username can't be blank")
        @NotNull(message = "Username can't be null")
        @Size(min = 3, message = "Username must have at least 3 characters.")
        String username,

        @NotBlank(message = "Password can't be blank")
        @NotNull(message = "Password can't be null")
        @Size(min = 8, message = "Password must have at least 8 characters.")
        String password
) { }
