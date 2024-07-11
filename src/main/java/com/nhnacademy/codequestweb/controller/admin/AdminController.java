package com.nhnacademy.codequestweb.controller.admin;

import com.nhnacademy.codequestweb.response.mypage.ClientPrivacyResponseDto;
import com.nhnacademy.codequestweb.service.admin.AdminService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/admin")
    public String admin(HttpServletRequest request) {
        Page<ClientPrivacyResponseDto> privacyPage = adminService.privacyList(0, 10, "clientId", false, CookieUtils.getCookieValue(request, "access"));
        request.setAttribute("view", "adminPage");
        return "index";
    }

    @ExceptionHandler(FeignException.Unauthorized.class)
    public String unauthorized(FeignException.Unauthorized e, HttpServletRequest request) {
        if (CookieUtils.getCookieValue(request, "access") == null) {
            return "redirect:/auth";
        }
        return "redirect:/";
    }
}
