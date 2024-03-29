package com.marco.loginjwt.web.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class HandlerException {

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorMessage> exceptionMessage(DisabledException e) {
        ErrorMessage message = new ErrorMessage();
        message.setMessages(List.of("disabled user."));
        message.setTimestamp(new Date());
        message.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorMessage> exceptionMessage(BadCredentialsException e) {
        ErrorMessage message = new ErrorMessage();
        message.setMessages(List.of("Invalid email or password"));
        message.setTimestamp(new Date());
        message.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ErrorMessage> exceptionMessage(InternalAuthenticationServiceException e) {
        ErrorMessage message = new ErrorMessage();
        message.setMessages(List.of("user not found."));
        message.setTimestamp(new Date());
        message.setStatus(HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> exceptionMessage(Exception e) {
        ErrorMessage message = new ErrorMessage();
        message.setMessages(List.of("an unexpected error occurred while processing your request. please try again later."));
        message.setTimestamp(new Date());
        message.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

    @ExceptionHandler(ExceptionMessage.class)
    public ResponseEntity<ErrorMessage> exceptionMessage(ExceptionMessage e) {
        ErrorMessage message = new ErrorMessage();
        message.setMessages(Collections.singletonList(e.getMessage()));
        message.setTimestamp(new Date());
        message.setStatus(e.getStatusCode().value());
        return ResponseEntity.status(e.getStatusCode().value()).body(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ErrorMessage message = new ErrorMessage();
        message.setMessages(errorMessages);
        message.setTimestamp(new Date());
        message.setStatus(HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
