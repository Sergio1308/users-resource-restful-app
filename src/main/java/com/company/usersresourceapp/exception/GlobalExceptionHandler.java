package com.company.usersresourceapp.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                            HttpStatusCode status, WebRequest request) {
        String message = "Arguments is not valid";
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        String requestURI = request.getDescription(false);
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST, message, ex.getBody().getDetail(), requestURI.substring(requestURI.indexOf('/'))
        );
        errorResponse.setInvalidParams(errors);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFound(UserNotFoundException ex, HttpServletRequest httpRequest) {
        String message = "Resource not found";
        return new ErrorResponse(HttpStatus.NOT_FOUND,
                message, ex.getLocalizedMessage(), httpRequest.getRequestURI());
    }

    @ExceptionHandler(ParametersValidationException.class)
    public ErrorResponse handleTransactionalError(Exception ex, HttpServletRequest httpRequest) {
        String message = "Bad user request";
        return new ErrorResponse(HttpStatus.BAD_REQUEST,
                message, ex.getLocalizedMessage(), httpRequest.getRequestURI());
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    public ErrorResponse handleMethodNotAllowed(Exception ex, HttpServletRequest httpRequest) {
        String message = "Method not allowed";
        return new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED,
                message, ex.getLocalizedMessage(), httpRequest.getRequestURI());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse handleConstraintViolation(Exception ex, HttpServletRequest httpRequest) {
        String message = "Constraint violation";
        return new ErrorResponse(HttpStatus.BAD_REQUEST,
                message, ex.getLocalizedMessage(), httpRequest.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleInternalError(Exception ex, HttpServletRequest httpRequest) {
        String message = "Internal server error";
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                message, ex.getLocalizedMessage(), httpRequest.getRequestURI());
    }
}
