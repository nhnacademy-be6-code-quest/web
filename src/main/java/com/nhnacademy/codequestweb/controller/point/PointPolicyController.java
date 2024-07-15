package com.nhnacademy.codequestweb.controller.point;

import com.nhnacademy.codequestweb.request.point.PointPolicyActiveRequestDto;
import com.nhnacademy.codequestweb.request.point.PointPolicyModifyRequestDto;
import com.nhnacademy.codequestweb.request.point.PointPolicyRegisterRequestDto;
import com.nhnacademy.codequestweb.service.point.PointPolicyService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
@RequiredArgsConstructor
public class PointPolicyController {

    private final PointPolicyService pointPolicyService;
    private static final String NAME = "access";
    HttpHeaders headers = new HttpHeaders();

    @PostMapping("/admin/point/policy/register")
    public String pointPolicyRegister(HttpServletRequest req,
        @ModelAttribute PointPolicyRegisterRequestDto pointPolicyRegisterRequestDto) {

        headers.set(NAME, CookieUtils.getCookieValue(req, NAME));

        pointPolicyService.savePointPolicy(headers, pointPolicyRegisterRequestDto);

        return "redirect:/admin/point/policy";
    }


    @PutMapping("/admin/point/policy/modify")
    public String pointPolicyRegister(HttpServletRequest req,
        @ModelAttribute PointPolicyModifyRequestDto pointPolicyModifyRequestDto) {
        headers.set(NAME, CookieUtils.getCookieValue(req, NAME));
        pointPolicyService.modifyPolicy(pointPolicyModifyRequestDto);
        return "redirect:/admin/point/policy";
    }
    @PutMapping("/admin/point/policy/active")
    public String pointPolicyActive(HttpServletRequest req, @ModelAttribute
        PointPolicyActiveRequestDto pointPolicyActiveRequestDto) {
        headers.set(NAME, CookieUtils.getCookieValue(req, NAME));
        pointPolicyService.pointActive(pointPolicyActiveRequestDto);
        return "redirect:/admin/point/policy";
    }
}
