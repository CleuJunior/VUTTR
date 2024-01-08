package br.com.cleonildo.vuttr.handler;

import br.com.cleonildo.vuttr.handler.responsehandler.ExcpetionResponse;
import br.com.cleonildo.vuttr.handler.excpetion.NotFoundException;
import br.com.cleonildo.vuttr.handler.excpetion.PasswordDontMatchException;
import org.postgresql.util.PSQLException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Objects;

import static br.com.cleonildo.vuttr.handler.utils.DataIntegrityMessageUtils.getDataIntegrityMessage;

/**
 * This class handles all exceptions thrown by the controllers.
 * It provides a standardized way to return errors to the client.
 */
@ControllerAdvice
public class ErrorExceptionHandler {
    private static final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;
    private static final HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;

    /**
     * This method handles {@link NotFoundException}s.
     * It returns a {@link ResponseEntity} with a {@link ExcpetionResponse}
     * that contains the error details.
     *
     * @param notFoundException the exception to handle
     * @return the response entity with the error details
     */
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

    /**
     * This method handles {@link MethodArgumentNotValidException}s.
     * It returns a {@link ResponseEntity} with a {@link ExcpetionResponse}
     * that contains the error details.
     *
     * @param validException the exception to handle
     * @return the response entity with the error details
     */
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

    /**
     * This method extracts the first error message from a {@link BindException}.
     *
     * @param exception the exception to extract the error message from
     * @return the first error message
     */
    private String getSingleMessageError(BindException exception) {
        return exception.getBindingResult().getAllErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Unknown error");
    }

    /**
     * This method handles {@link PasswordDontMatchException}s.
     * It returns a {@link ResponseEntity} with a {@link ExcpetionResponse}
     * that contains the error details.
     *
     * @param exception the exception to handle
     * @return the response entity with the error details
     */
    @ExceptionHandler(PasswordDontMatchException.class)
    public ResponseEntity<ExcpetionResponse> constraintErrosMessages(PasswordDontMatchException exception) {

        var err = new ExcpetionResponse(
                BAD_REQUEST.value(),
                BAD_REQUEST.getReasonPhrase(),
                exception.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(err);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExcpetionResponse> dataIntegrityViolationHandler(PSQLException exception) {
        var constraintName = Objects
                .requireNonNull(exception.getServerErrorMessage())
                .getConstraint();

        var err = new ExcpetionResponse(
                BAD_REQUEST.value(),
                BAD_REQUEST.getReasonPhrase(),
                getDataIntegrityMessage(constraintName),
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(err);
    }
}