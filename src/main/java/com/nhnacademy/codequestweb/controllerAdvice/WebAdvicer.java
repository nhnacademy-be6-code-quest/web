package com.nhnacademy.codequestweb.controllerAdvice;


import feign.FeignException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WebAdvicer {
    @ExceptionHandler(FeignException.Unauthorized.class)
    public String handleBadRequest(FeignException.Unauthorized ex) {
        return "redirect:/auth";
    }
}
