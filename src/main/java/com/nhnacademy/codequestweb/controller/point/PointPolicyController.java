package com.nhnacademy.codequestweb.controller.point;

import com.nhnacademy.codequestweb.domain.PointPolicyType;
import com.nhnacademy.codequestweb.domain.PointUsageKind;
import com.nhnacademy.codequestweb.request.point.PointPolicyRegisterRequestDto;
import com.nhnacademy.codequestweb.response.point.PointPolicyAdminListResponseDto;
import com.nhnacademy.codequestweb.response.point.PointUsageMyPageResponseDto;
import com.nhnacademy.codequestweb.service.point.PointPolicyService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PointPolicyController {

    private final PointPolicyService pointPolicyService;

    @GetMapping("/admin/point/policy")
    public String pointPolicy (HttpServletRequest req, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        req.setAttribute("view", "adminPage");
        req.setAttribute("adminPage", "pointPolicy");
        Page<PointPolicyAdminListResponseDto> dto = pointPolicyService.findPointPolicies(headers, page, size);
        req.setAttribute("points", dto);
        List<PointPolicyType> pointPolicyTypes = List.of(PointPolicyType.REVIEW,PointPolicyType.MEMBERSHIP,PointPolicyType.MEMBERSHIP,PointPolicyType.PAYMENT);
        req.setAttribute("pointTypes",pointPolicyTypes);
        return "index";
    }

    @PostMapping("/admin/point/policy/register")
    public String pointPolicyRegister(HttpServletRequest req, @ModelAttribute PointPolicyRegisterRequestDto pointPolicyRegisterRequestDto){
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        pointPolicyService.savePointPolicy(headers, pointPolicyRegisterRequestDto);

        return "redirect:/admin/point/policy";
    }
    @DeleteMapping("/admin/point/policy/{pointPolicyId}")
    public String deletePointPolicy(HttpServletRequest req, @PathVariable long pointPolicyId){
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        pointPolicyService.deletePolicy(headers, pointPolicyId);
        return "redirect:/admin/point/policy";
    }
}
