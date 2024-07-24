package com.nhnacademy.codequestweb.controller.auth;

import com.nhnacademy.codequestweb.exception.auth.PaycoRedirectException;
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
    private static final String ACCESS = "access";
    private static final String REFRESH = "refresh";
    private static final String SAMESITE = "SameSite";
    private static final String INDEX = "index";
    private static final String ALTER_MESSAGE = "alterMessage";
    private static final String REDIRECT = "redirect:/";

    private final AuthService authService;

    @GetMapping("/auth")
    public String auth(HttpServletRequest req) {
        if (CookieUtils.getCookieValue(req, ACCESS) != null) {
            return REDIRECT;
        }
        if (req.getParameter(ALTER_MESSAGE) != null) {
            req.setAttribute(ALTER_MESSAGE, req.getParameter(ALTER_MESSAGE));
        }
        req.setAttribute("view", "auth");
        return INDEX;
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
            Cookie cookie = new Cookie(ACCESS, response.getAccess());
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 14);
            cookie.setAttribute(SAMESITE, "Lax");
            res.addCookie(cookie);

            cookie = new Cookie(REFRESH, response.getRefresh());
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 14);
            cookie.setAttribute(SAMESITE, "Lax");
            res.addCookie(cookie);

            CookieUtils.deleteCookieValue(res, "cart");
        }
        return REDIRECT;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest req, HttpServletResponse res) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS, CookieUtils.getCookieValue(req, ACCESS));
        headers.set(REFRESH, CookieUtils.getCookieValue(req, REFRESH));
        authService.logout(headers);
        Cookie access = new Cookie(ACCESS, null);
        Cookie refresh = new Cookie(REFRESH, null);
        access.setMaxAge(0);
        refresh.setMaxAge(0);
        access.setPath("/");
        refresh.setPath("/");
        res.addCookie(access);
        res.addCookie(refresh);
        CookieUtils.deleteCookieValue(res, "cart");
        return REDIRECT;
    }

    @GetMapping("/payco/login")
    public void paycoLogin(HttpServletRequest req, HttpServletResponse res) {
        try {
            res.sendRedirect(authService.getPaycoLoginURL());
        } catch (IOException e) {
            throw new PaycoRedirectException(e.getMessage());
        }
    }

    @GetMapping("/payco/recovery")
    public void paycoRecovery(HttpServletRequest req, HttpServletResponse res) {
        try {
            res.sendRedirect(authService.getPaycoRecoveryURL());
        } catch (IOException e) {
            throw new PaycoRedirectException(e.getMessage());
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
                req.setAttribute(ALTER_MESSAGE, "휴면/삭제된 계정 입니다.");
                req.setAttribute("view", "auth");
                req.setAttribute("isDeleted", true);
                return INDEX;
            }

            cookie = new Cookie(REFRESH, tokenInfo.getRefresh());
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 14);
            cookie.setAttribute(SAMESITE, "Lax");
            res.addCookie(cookie);

            if (tokenInfo.getAccess() == null) {
                req.setAttribute("view", "oauth");
                return INDEX;
            }

            cookie = new Cookie(ACCESS, tokenInfo.getAccess());
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 14);
            cookie.setAttribute(SAMESITE, "Lax");
            res.addCookie(cookie);
        } else {
            log.warn("No code received from Payco");
        }
        return REDIRECT;
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
        oAuthRegisterRequestDto.setAccess(CookieUtils.getCookieValue(req, REFRESH));
        TokenResponseDto response = authService.oAuthRegister(oAuthRegisterRequestDto).getBody();
        if (response != null) {
            Cookie cookie = new Cookie(ACCESS, response.getAccess());
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 14);
            cookie.setAttribute(SAMESITE, "Lax");
            res.addCookie(cookie);

            cookie = new Cookie(REFRESH, response.getRefresh());
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 14);
            cookie.setAttribute(SAMESITE, "Lax");
            res.addCookie(cookie);
        }
        return REDIRECT;
    }

    @ExceptionHandler(FeignException.Unauthorized.class)
    public String loginError(FeignException.Unauthorized e, HttpServletRequest req, HttpServletResponse res) {
        req.setAttribute("login_message", "아이디 또는 비밀번호가 틀렸습니다.");
        req.setAttribute("view", "auth");
        return INDEX;
    }

    @ExceptionHandler(FeignException.Gone.class)
    public String loginError(FeignException.Gone e, HttpServletRequest req, HttpServletResponse res) {
        req.setAttribute("login_message", "삭제/휴면된 계정입니다.");
        req.setAttribute("view", "auth");
        req.setAttribute("isDeleted", true);
        return INDEX;
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
        return INDEX;
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
        return INDEX;
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
