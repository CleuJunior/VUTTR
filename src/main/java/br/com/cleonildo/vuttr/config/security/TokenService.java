package br.com.cleonildo.vuttr.config.security;

import br.com.cleonildo.vuttr.entities.User;
import br.com.cleonildo.vuttr.handler.excpetion.TokenGenerationExcepiton;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static br.com.cleonildo.vuttr.log.LogConstants.TOKEN_GENERATION_ERROR;

/**
 * Service class responsible for generating and validating JWT tokens.
 */
@Service
public class TokenService {

    /** Secret key used to sign and verify tokens. */
    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Generates a JSON Web Token (JWT) for the given user.
     *
     * @param user the user for whom the token should be generated
     * @return the JWT token
     * @throws TokenGenerationExcepiton if an error occurs while generating the token
     */
    public String generateToken(User user){
        try{

            return JWT
                    .create()
                    .withIssuer("auth-api")
                    .withSubject(user.getUsername())
                    .withExpiresAt(genExpirationDate())
                    .sign(Algorithm.HMAC256(secret));

        } catch (JWTCreationException exception) {
            throw new TokenGenerationExcepiton(TOKEN_GENERATION_ERROR);
        }
    }

    /**
     * Validates the given JWT token and returns the subject of the token.
     *
     * @param token the JWT token to be validated
     * @return the subject of the token, or an empty string if the token is invalid
     */
    public String validateToken(String token){
        try {

            return JWT
                    .require(Algorithm.HMAC256(secret))
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException exception){
            return "";
        }
    }

    /**
     * Generates an expiration date for the JWT token.
     *
     * @return the expiration date as an Instant
     */
    private Instant genExpirationDate(){
        return LocalDateTime
                .now()
                .plusHours(2)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}
