package br.com.cleonildo.vuttr.config.security;

import br.com.cleonildo.vuttr.handler.excpetion.NotFoundException;
import br.com.cleonildo.vuttr.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static br.com.cleonildo.vuttr.log.LogConstants.USERNAME_FOUND;
import static br.com.cleonildo.vuttr.log.LogConstants.USERNAME_NOT_FOUND;


/**
 * This class implements a security filter that is responsible for validating the user's access token and authenticating the user.
 * If the user is authenticated, the security context is updated with the user's details.
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityFilter.class);

    private final TokenService tokenService;
    private final UserRepository userRepository;

    /**
     * Constructs a new instance of the SecurityFilter class.
     *
     * @param tokenService The token service used to validate and retrieve user details from the access token.
     * @param userRepository The user repository used to retrieve user details from the database.
     */
    public SecurityFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    /**
     * This method is responsible for validating the user's access token and authenticating the user. If the user is
     * authenticated, the security context is updated with the user's details.
     *
     * @param request The HTTP request object.
     * @param response The HTTP response object.
     * @param filterChain The filter chain.
     * @throws ServletException If an error occurs during the filter processing.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var token = recoverToken(request);

        if (token != null) {

            var login = tokenService.validateToken(token);

            var user = userRepository.findByUsername(login)
                    .orElseThrow(() -> {
                        LOG.warn(USERNAME_FOUND, login);
                        return new NotFoundException(USERNAME_NOT_FOUND);
                    });

            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * This method retrieves the access token from the HTTP request headers.
     *
     * @param request The HTTP request object.
     * @return The access token, or null if it is not present in the request headers.
     */
    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null) {
            return null;
        }

        return authHeader.replace("Bearer ", "");
    }
}