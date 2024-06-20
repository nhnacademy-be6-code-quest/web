//package com.nhnacademy.codequestweb.controller.auth;
//
//import com.nhnacademy.codequestweb.request.auth.ClientRegisterRequestDto;
//import com.nhnacademy.codequestweb.response.auth.ClientRegisterResponseDto;
//import com.nhnacademy.codequestweb.service.auth.AuthService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//@Controller
//@RequiredArgsConstructor
//public class AuthController {
//    private final AuthService authService;
//
//    @GetMapping("/auth")
//    public String auth(HttpServletRequest req) {
//        req.setAttribute("view", "auth");
//        return "index";
//    }
//
//    @PostMapping("/register")
//    public String authPost(@Valid @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ModelAttribute ClientRegisterRequestDto clientRegisterRequestDto, HttpServletRequest req) {
//        ResponseEntity<ClientRegisterResponseDto> response = authService.register(clientRegisterRequestDto);
//        if (!response.getStatusCode().is2xxSuccessful()) {
//            req.setAttribute("message", "이미 존재하는 이메일입니다.");
//            return "redirect:/auth";
//        }
//        return "index";
//    }
//}
