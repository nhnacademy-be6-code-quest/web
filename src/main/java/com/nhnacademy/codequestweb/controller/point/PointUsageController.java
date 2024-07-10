package com.nhnacademy.codequestweb.controller.point;

import com.nhnacademy.codequestweb.response.point.PointAccumulationAdminPageResponseDto;
import com.nhnacademy.codequestweb.response.point.PointAccumulationMyPageResponseDto;
import com.nhnacademy.codequestweb.response.point.PointUsageAdminPageResponseDto;
import com.nhnacademy.codequestweb.response.point.PointUsageMyPageResponseDto;
import com.nhnacademy.codequestweb.service.point.PointUsageService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PointUsageController {

    private final PointUsageService pointUsageService;

    @GetMapping("/mypage/point/use")
    public String myPagePoint (HttpServletRequest req, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        req.setAttribute("view", "mypage");
        req.setAttribute("mypage", "pointUsed");
        Page<PointUsageMyPageResponseDto> dto = pointUsageService.clientUsePoint(headers, page, size);
        req.setAttribute("points", dto);
        return "index";
    }

    @GetMapping("/admin/point/use")
    public String adminPagePoint (HttpServletRequest req, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        req.setAttribute("view", "adminPage");
        req.setAttribute("adminPage", "pointUsed");
        Page<PointUsageAdminPageResponseDto> dto = pointUsageService.userUsePoint(headers, page, size);
        req.setAttribute("points", dto);
        return "index";
    }
}
