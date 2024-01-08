package br.com.cleonildo.vuttr.services;

import br.com.cleonildo.vuttr.config.security.TokenService;
import br.com.cleonildo.vuttr.dto.LoginRequest;
import br.com.cleonildo.vuttr.dto.LoginResponse;
import br.com.cleonildo.vuttr.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository repository;

    public AuthService(AuthenticationManager authenticationManager, TokenService tokenService, UserRepository repository) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.repository = repository;
    }

    public LoginResponse token(LoginRequest request) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var userDetails = (UserDetails) auth.getPrincipal();

        var user = this.repository
                .findByUsername(userDetails.getUsername())
                .orElseThrow();

        return new LoginResponse(this.tokenService.generateToken(user));
    }

}
