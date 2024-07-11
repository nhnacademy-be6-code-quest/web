package com.nhnacademy.codequestweb.controller.admin;

import com.nhnacademy.codequestweb.domain.PointPolicyType;
import com.nhnacademy.codequestweb.response.mypage.ClientPrivacyResponseDto;
import com.nhnacademy.codequestweb.response.point.PointAccumulationAdminPageResponseDto;
import com.nhnacademy.codequestweb.response.point.PointPolicyAdminListResponseDto;
import com.nhnacademy.codequestweb.response.point.PointUsageAdminPageResponseDto;
import com.nhnacademy.codequestweb.service.admin.AdminService;
import com.nhnacademy.codequestweb.service.point.PointAccumulationService;
import com.nhnacademy.codequestweb.service.point.PointPolicyService;
import com.nhnacademy.codequestweb.service.point.PointUsageService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final PointAccumulationService pointAccumulationService;
    private final PointUsageService pointUsageService;
    private final PointPolicyService pointPolicyService;

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

    @GetMapping("/admin/point/reward")
    public String adminPageRewardPoint (HttpServletRequest req, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        req.setAttribute("view", "adminPage");
        req.setAttribute("adminPage", "pointReward");
        req.setAttribute("activeSection", "point");
        Page<PointAccumulationAdminPageResponseDto> dto = pointAccumulationService.userPoint(headers, page, size);
        req.setAttribute("points", dto);
        return "index";
    }

    @GetMapping("/admin/point/use")
    public String adminPagUsePoint (HttpServletRequest req, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        req.setAttribute("view", "adminPage");
        req.setAttribute("adminPage", "pointUsed");
        req.setAttribute("activeSection", "point");
        Page<PointUsageAdminPageResponseDto> dto = pointUsageService.userUsePoint(headers, page, size);
        req.setAttribute("points", dto);
        return "index";
    }

    @GetMapping("/admin/point/policy")
    public String pointPolicy (HttpServletRequest req, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        req.setAttribute("view", "adminPage");
        req.setAttribute("adminPage", "pointPolicy");
        req.setAttribute("activeSection", "point");
        Page<PointPolicyAdminListResponseDto> dto = pointPolicyService.findPointPolicies(headers, page, size);
        req.setAttribute("points", dto);
        List<PointPolicyType> pointPolicyTypes = List.of(PointPolicyType.REVIEW,PointPolicyType.MEMBERSHIP,PointPolicyType.MEMBERSHIP,PointPolicyType.PAYMENT);
        req.setAttribute("pointTypes",pointPolicyTypes);
        return "index";
    }


}
