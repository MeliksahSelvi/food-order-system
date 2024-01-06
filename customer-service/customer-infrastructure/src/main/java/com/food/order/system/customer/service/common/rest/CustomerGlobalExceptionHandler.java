package com.food.order.system.customer.service.common.rest;

import com.food.order.system.customer.service.exception.CustomerDomainException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 29.12.2023
 */

/*
 * Customer domain layer'da fırlatılabilecek exception'ları handle ettiğimiz handler.
 * */
@ControllerAdvice
@Slf4j
public class CustomerGlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = CustomerDomainException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(CustomerDomainException customerDomainException) {
        log.error(customerDomainException.getMessage(), customerDomainException);
        return ErrorDTO.builder()
                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(customerDomainException.getMessage())
                .build();
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ErrorDTO.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("Unexpected error!")
                .build();
    }
}
