package br.com.cleonildo.vuttr.controllers;

import br.com.cleonildo.vuttr.dto.LoginRequest;
import br.com.cleonildo.vuttr.dto.LoginResponse;
import br.com.cleonildo.vuttr.dto.UserRequest;
import br.com.cleonildo.vuttr.dto.UserResponse;
import br.com.cleonildo.vuttr.services.AuthService;
import br.com.cleonildo.vuttr.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RestController
@RequestMapping(value = "/auth", produces = "application/json")
@Tag(name = "auth")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @Operation(summary = "Update an existing user", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request from user")
    })
    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        var response = this.service.token(request);

        return ResponseEntity.ok(response);
    }

}
