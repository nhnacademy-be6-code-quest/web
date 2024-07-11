package com.nhnacademy.codequestweb.controller.point;

import com.nhnacademy.codequestweb.request.point.PointRewardOrderRequestDto;
import com.nhnacademy.codequestweb.request.point.PointUsagePaymentRequestDto;
import com.nhnacademy.codequestweb.response.point.PointAccumulationAdminPageResponseDto;
import com.nhnacademy.codequestweb.response.point.PointAccumulationMyPageResponseDto;
import com.nhnacademy.codequestweb.response.point.TotalPointAmountResponseDto;
import com.nhnacademy.codequestweb.service.point.OrderPointService;
import com.nhnacademy.codequestweb.service.point.PointAccumulationService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PointAccumulationController {

    private final PointAccumulationService pointAccumulationService;
    private final OrderPointService orderPointService;



    @GetMapping("/admin/point/reward")
    public String adminPagePoint (HttpServletRequest req, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        req.setAttribute("view", "adminPage");
        req.setAttribute("adminPage", "pointReward");
        Page<PointAccumulationAdminPageResponseDto> dto = pointAccumulationService.userPoint(headers, page, size);
        req.setAttribute("points", dto);
        return "index";
    }

}