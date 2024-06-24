package com.nhnacademy.codequestweb.controller.auth;

import com.nhnacademy.codequestweb.client.auth.AuthClient;
import com.nhnacademy.codequestweb.domain.Status;
import com.nhnacademy.codequestweb.request.auth.ClientLoginRequestDto;
import com.nhnacademy.codequestweb.request.auth.ClientRegisterRequestDto;
import com.nhnacademy.codequestweb.request.coupon.CouponRequestDto;
import com.nhnacademy.codequestweb.response.auth.TokenResponseDto;
import com.nhnacademy.codequestweb.service.auth.AuthService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import feign.FeignException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AuthClient authClient;

    @GetMapping("/auth")
    public String auth(HttpServletRequest req) {
        if (req.getAttribute("isLogin") == null) {
            req.setAttribute("view", "auth");
        }
        return "index";
    }

    @PostMapping("/register")
    public String authPost(@Valid @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ModelAttribute ClientRegisterRequestDto clientRegisterRequestDto, HttpServletRequest req) {
        authService.register(clientRegisterRequestDto);
        req.setAttribute("view", "auth");
        return "index";
    }

    @PostMapping("/login")
    public String loginPost(@Valid @ModelAttribute ClientLoginRequestDto clientLoginRequestDto, HttpServletResponse res) {
        TokenResponseDto response = authService.login(clientLoginRequestDto).getBody();
        if (response != null) {
            Cookie cookie = new Cookie("access", response.getAccess());
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 2);
            res.addCookie(cookie);

            cookie = new Cookie("refresh", response.getRefresh());
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 14);
            res.addCookie(cookie);
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest req, HttpServletResponse res) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));
        authClient.logout(headers);
        Cookie access = new Cookie("access", null);
        Cookie refresh = new Cookie("refresh", null);
        access.setMaxAge(0);
        refresh.setMaxAge(0);
        access.setPath("/");
        refresh.setPath("/");
        res.addCookie(access);
        res.addCookie(refresh);
        return "redirect:/";
    }

    @ExceptionHandler(FeignException.Unauthorized.class)
    public String loginError(FeignException.Unauthorized e, HttpServletRequest req, HttpServletResponse res) {
        req.setAttribute("login_message", "아이디 또는 비밀번호가 틀렸습니다.");
        req.setAttribute("view", "auth");
        return "index";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String validationError(MethodArgumentNotValidException e, HttpServletRequest req) {
        ClientRegisterRequestDto requestDto = getRequestDto(req);
        try {
            requestDto.setClientBirth(LocalDate.parse(req.getParameter("clientBirth"), DateTimeFormatter.ISO_DATE));
        } catch (DateTimeParseException dtpe) {
            requestDto.setClientBirth(null);
        }
        req.setAttribute("prev_data", requestDto);
        req.setAttribute("register_message", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        req.setAttribute("view", "auth");
        req.setAttribute("form", "register");
        return "index";
    }

    @ExceptionHandler(FeignException.Conflict.class)
    public String validationError(FeignException.Conflict e, HttpServletRequest req) {
        ClientRegisterRequestDto requestDto = getRequestDto(req);
        try {
            requestDto.setClientBirth(LocalDate.parse(req.getParameter("clientBirth"), DateTimeFormatter.ISO_DATE));
        } catch (DateTimeParseException dtpe) {
            requestDto.setClientBirth(null);
        }
        req.setAttribute("prev_data", requestDto);
        req.setAttribute("register_message", "이미 가입된 이메일입니다.");
        req.setAttribute("view", "auth");
        req.setAttribute("form", "register");
        return "index";
    }

    private ClientRegisterRequestDto getRequestDto(HttpServletRequest req) {
        ClientRegisterRequestDto requestDto = new ClientRegisterRequestDto();
        requestDto.setClientName(req.getParameter("clientName"));
        requestDto.setClientEmail(req.getParameter("clientEmail"));
        requestDto.setClientPassword(req.getParameter("clientPassword"));
        requestDto.setClientPhoneNumber(req.getParameter("clientPhoneNumber"));
        return requestDto;
    }

}
