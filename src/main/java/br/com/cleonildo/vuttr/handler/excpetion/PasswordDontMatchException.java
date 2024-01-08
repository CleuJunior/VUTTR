package br.com.cleonildo.vuttr.handler.excpetion;

public class PasswordDontMatchException extends RuntimeException {
    public PasswordDontMatchException(String message) {
        super(message);
    }

    public PasswordDontMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
