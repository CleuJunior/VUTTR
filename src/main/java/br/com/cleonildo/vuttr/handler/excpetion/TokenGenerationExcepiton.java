package br.com.cleonildo.vuttr.handler.excpetion;

public class TokenGenerationExcepiton extends RuntimeException {
    public TokenGenerationExcepiton(String message) {
        super(message);
    }

    public TokenGenerationExcepiton(String message, Throwable cause) {
        super(message, cause);
    }
}