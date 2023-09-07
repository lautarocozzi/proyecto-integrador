package com.mrInstruments.backend.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalException extends ResponseEntityExceptionHandler {

    private static final Logger LOG= LogManager.getLogger(GlobalException.class);
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> tratamientoResourceNotFoundException(ResourceNotFoundException rnfe, WebRequest request) {
        LOG.error(rnfe.getMessage());
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.NOT_FOUND.value(), request.getDescription(true), rnfe.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> throwErrorSolicitudIncorrecta(BadRequestException ex, WebRequest request){
        LOG.error(ex.getMessage());
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(), request.getDescription(true), ex.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Object> throwSQLException(SQLException sqlEx, WebRequest request){
        LOG.error(sqlEx.getMessage());
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(), request.getDescription(true), sqlEx.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(), request.getDescription(true), ex.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorInfo errorInfo = new ErrorInfo(status.value(), request.getDescription(true), ex.getMessage());
        logger.error(ex.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorInfo errorInfo = new ErrorInfo(status.value(), request.getDescription(true), ex.getMessage());
        return new ResponseEntity<>(errorInfo, status);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> tratamientoUsernameNotFoundException(UsernameNotFoundException unfex, WebRequest request) {
        LOG.error(unfex.getMessage());
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(), request.getDescription(true), unfex.getMessage());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }
}
