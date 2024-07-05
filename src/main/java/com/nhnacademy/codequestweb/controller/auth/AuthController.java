package com.nhnacademy.codequestweb.controller.auth;

import com.nhnacademy.codequestweb.request.auth.ClientLoginRequestDto;
import com.nhnacademy.codequestweb.request.auth.ClientRegisterRequestDto;
import com.nhnacademy.codequestweb.request.auth.OAuthRegisterRequestDto;
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

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/auth")
    public String auth(HttpServletRequest req) {
        if (CookieUtils.getCookieValue(req, "access") != null) {
            return "redirect:/";
        }
        if (req.getParameter("alterMessage") != null) {
            req.setAttribute("alterMessage", req.getParameter("alterMessage"));
        }
        req.setAttribute("view", "auth");
        return "index";
    }

    @PostMapping("/register")
    public String authPost(@Valid @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ModelAttribute ClientRegisterRequestDto clientRegisterRequestDto, HttpServletRequest req) {
        authService.register(clientRegisterRequestDto);
        return "redirect:/auth" + "?alterMessage=" + "Success";
    }

    @PostMapping("/login")
    public String loginPost(@Valid @ModelAttribute ClientLoginRequestDto clientLoginRequestDto, HttpServletResponse res) {
        TokenResponseDto response = authService.login(clientLoginRequestDto).getBody();
        if (response != null) {
            Cookie cookie = new Cookie("access", response.getAccess());
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 14);
            res.addCookie(cookie);

            cookie = new Cookie("refresh", response.getRefresh());
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 14);
            res.addCookie(cookie);

            CookieUtils.deleteCookieValue(res, "cart");
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest req, HttpServletResponse res) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));
        authService.logout(headers);
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

    @GetMapping("/payco/login")
    public void paycoLogin(HttpServletRequest req, HttpServletResponse res) {
        try {
            res.sendRedirect(authService.getPaycoLoginURL());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/payco/recovery")
    public void paycoRecovery(HttpServletRequest req, HttpServletResponse res) {
        try {
            res.sendRedirect(authService.getPaycoRecoveryURL());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/payco/login/callback")
    public String paycoLoginCallback(@RequestParam(required = false) String code,
                                     @RequestParam(required = false) String error,
                                     HttpServletRequest req, HttpServletResponse res) {
        if (error != null) {
            log.error("Payco login error: {}", error);
        }

        if (code != null) {
            log.info("Received Payco auth code: {}", code);
            Cookie cookie;
            TokenResponseDto tokenInfo = authService.paycoLoginCallback(code).getBody();
            if (tokenInfo == null) {
                req.setAttribute("alterMessage", "휴면/삭제된 계정 입니다.");
                req.setAttribute("view", "auth");
                return "index";
            }

            cookie = new Cookie("refresh", tokenInfo.getRefresh());
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 14);
            res.addCookie(cookie);

            if (tokenInfo.getAccess() == null) {
                req.setAttribute("view", "oauth");
                return "index";
            }

            cookie = new Cookie("access", tokenInfo.getAccess());
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 14);
            res.addCookie(cookie);
        } else {
            log.warn("No code received from Payco");
        }
        return "redirect:/";
    }

    @GetMapping("/payco/recovery/callback")
    public String paycoRecoveryCallback(@RequestParam(required = false) String code,
                                     @RequestParam(required = false) String error,
                                     HttpServletRequest req, HttpServletResponse res) {
        if (error != null) {
            log.error("Payco login error: {}", error);
        }

        if (code != null) {
            log.info("Received Payco auth code: {}", code);
            String email = authService.recover(code).getBody();
            authService.recoverAccount(email);
        } else {
            log.warn("No code received from Payco");
        }
        return "redirect:/auth" + "?alterMessage=" + "Success";
    }

    @PostMapping("/oauth/register")
    public String oauthRegister(@ModelAttribute OAuthRegisterRequestDto oAuthRegisterRequestDto, HttpServletRequest req, HttpServletResponse res) {
        oAuthRegisterRequestDto.setAccess(CookieUtils.getCookieValue(req, "refresh"));
        TokenResponseDto response = authService.oAuthRegister(oAuthRegisterRequestDto).getBody();
        if (response != null) {
            Cookie cookie = new Cookie("access", response.getAccess());
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 14);
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

    @ExceptionHandler(FeignException.Unauthorized.class)
    public String loginError(FeignException.Unauthorized e, HttpServletRequest req, HttpServletResponse res) {
        req.setAttribute("login_message", "아이디 또는 비밀번호가 틀렸습니다.");
        req.setAttribute("view", "auth");
        return "index";
    }

    @ExceptionHandler(FeignException.Gone.class)
    public String loginError(FeignException.Gone e, HttpServletRequest req, HttpServletResponse res) {
        req.setAttribute("login_message", "삭제/휴면된 계정입니다.");
        req.setAttribute("view", "auth");
        req.setAttribute("isDeleted", true);
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
