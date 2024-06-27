package com.nhnacademy.codequestweb.config.product;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ProductExceptionHandler {

    @ExceptionHandler(FeignException.FeignClientException.class)
    public String exception(FeignException.FeignClientException ex, Model model) {
        log.warn("status : {}, message : {}",ex.status(), ex.getMessage());
        return "index";
    }
}
