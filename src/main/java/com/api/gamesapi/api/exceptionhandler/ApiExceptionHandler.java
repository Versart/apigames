package com.api.gamesapi.api.exceptionhandler;

import com.api.gamesapi.domain.exception.CompanyNotFoundException;
import com.api.gamesapi.domain.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<Input> inputs = new ArrayList<>();
        for(ObjectError objectError : ex.getBindingResult().getAllErrors()) {
            String name = ((FieldError) objectError).getField();
            String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
            inputs.add(new Input(name,message));
        }
        Problem problem = new Problem();
        problem.setStatusCode(status.value());
        problem.setDateTime(OffsetDateTime.now());
        problem.setMessage("Campos inválidos!");
        problem.setInputs(inputs);
        return handleExceptionInternal(ex,problem,headers,status,request);
    }

    @ExceptionHandler(CompanyNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundCompany(NotFoundException ex, WebRequest webRequest) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        Problem problem = new Problem();
        problem.setStatusCode(httpStatus.value());
        problem.setDateTime(OffsetDateTime.now());
        problem.setMessage(ex.getMessage());

        return handleExceptionInternal(ex,problem, new HttpHeaders(),httpStatus,webRequest);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFound(NotFoundException ex, WebRequest webRequest) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        Problem problem = new Problem();
        problem.setStatusCode(httpStatus.value());
        problem.setDateTime(OffsetDateTime.now());
        problem.setMessage(ex.getMessage());

        return handleExceptionInternal(ex,problem, new HttpHeaders(),httpStatus,webRequest);
    }

    @Nullable
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        Problem problem = new Problem();
        problem.setStatusCode(httpStatus.value());
        problem.setDateTime(OffsetDateTime.now());
        problem.setMessage(ex.getMessage());
        return this.createResponseEntity(problem, headers, statusCode, request);
    }




}
