package com.nhnacademy.codequestweb.controller.common;

import com.nhnacademy.codequestweb.service.common.CommonService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CommonController {
    private final CommonService commonService;

    @GetMapping("/page/route")
    public String headerPageRoute(HttpServletRequest request) {
        String access = CookieUtils.getCookieValue(request, "access");
        if (access == null) {
            return "redirect:/auth";
        } else if (commonService.isAdmin(access)) {
            return "redirect:/admin";
        }
        return "redirect:/mypage";
    }
}
