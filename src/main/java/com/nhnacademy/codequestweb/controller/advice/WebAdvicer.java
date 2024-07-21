package com.nhnacademy.codequestweb.controller.advice;


import feign.FeignException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WebAdvicer {
    @ExceptionHandler(FeignException.Unauthorized.class)
    public String handleBadRequest(FeignException.Unauthorized ex, HttpServletResponse response) {
        Cookie access = new Cookie("access", null);
        Cookie refresh = new Cookie("refresh", null);
        access.setMaxAge(0);
        refresh.setMaxAge(0);
        access.setPath("/");
        refresh.setPath("/");
        response.addCookie(access);
        response.addCookie(refresh);
        return "redirect:/auth";
    }
}
