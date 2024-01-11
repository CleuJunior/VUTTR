package br.com.cleonildo.vuttr.dto;


import br.com.cleonildo.vuttr.entities.Role;
import br.com.cleonildo.vuttr.entities.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record UserRequest(
        @Email(message = "Email can't be blank")
        String email,
        @NotBlank(message = "Username can't be blank")
        @NotNull(message = "Username can't be null")
        @Size(min = 3, message = "Username must have at least 3 characters.")
        String username,
        @CPF
        String cpf,
        @NotBlank(message = "Password can't be blank")
        @NotNull(message = "Password can't be null")
        @Size(min = 8, message = "Password must have at least 8 characters.")
        String password,
        @NotBlank(message = "Password can't be blank")
        @NotNull(message = "Password can't be null")
        @Size(min = 8, message = "Password must have at least 8 characters.")
        @JsonProperty("confirm_password")
        String confirmPassword,
        @NotNull(message = "Role list can't be null")
        Role role

) {
        public User toUserEntity(String encodedPassword) {
                return new User(
                        email,
                        username,
                        cpf,
                        encodedPassword,
                        role
                );
        }
}

