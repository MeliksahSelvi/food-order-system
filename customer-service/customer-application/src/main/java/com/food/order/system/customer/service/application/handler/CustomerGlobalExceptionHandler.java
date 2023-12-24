package com.food.order.system.customer.service.application.handler;

import com.food.order.system.application.handler.ErrorDTO;
import com.food.order.system.application.handler.GlobalExceptionHandler;
import com.food.order.system.customer.service.domain.exception.CustomerDomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @Author mselvi
 * @Created 24.12.2023
 */

/*
 * Customer domain layer'da fırlatılabilecek exception'ları handle ettiğimiz handler.
 * */
@ControllerAdvice
@Slf4j
public class CustomerGlobalExceptionHandler extends GlobalExceptionHandler {

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
}
