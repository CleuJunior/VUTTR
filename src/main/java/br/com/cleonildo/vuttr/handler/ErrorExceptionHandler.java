package br.com.cleonildo.vuttr.handler;

import br.com.cleonildo.vuttr.handler.excpetion.ExcpetionResponse;
import br.com.cleonildo.vuttr.handler.excpetion.NotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ErrorExceptionHandler {
    private static final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;
    private static final HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExcpetionResponse> entityNotFound(NotFoundException notFoundException) {

        var err = new ExcpetionResponse(
                NOT_FOUND.value(),
                NOT_FOUND.getReasonPhrase(),
                notFoundException.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(NOT_FOUND).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExcpetionResponse> constraintErrosMessages(MethodArgumentNotValidException validException) {

        var errorMessages = this.getSingleMessageError(validException);

        var err = new ExcpetionResponse(
                BAD_REQUEST.value(),
                BAD_REQUEST.getReasonPhrase(),
                errorMessages,
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(err);
    }

    private String getSingleMessageError(BindException exception) {
        return exception.getBindingResult().getAllErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Unknown error");
    }
}