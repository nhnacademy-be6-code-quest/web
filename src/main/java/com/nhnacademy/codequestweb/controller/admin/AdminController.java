package com.nhnacademy.codequestweb.controller.admin;

import com.nhnacademy.codequestweb.domain.PointPolicyType;
import com.nhnacademy.codequestweb.response.mypage.ClientPrivacyResponseDto;
import com.nhnacademy.codequestweb.response.order.common.OrderResponseDto;
import com.nhnacademy.codequestweb.response.point.PointAccumulationAdminPageResponseDto;
import com.nhnacademy.codequestweb.response.point.PointPolicyAdminListResponseDto;
import com.nhnacademy.codequestweb.response.point.PointUsageAdminPageResponseDto;
import com.nhnacademy.codequestweb.response.shipping.AdminShippingPolicyPutRequestDto;
import com.nhnacademy.codequestweb.response.shipping.ShippingPolicyGetResponseDto;
import com.nhnacademy.codequestweb.service.admin.AdminService;
import com.nhnacademy.codequestweb.service.order.AdminOrderService;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final PointAccumulationService pointAccumulationService;
    private final PointUsageService pointUsageService;
    private final PointPolicyService pointPolicyService;
    private final AdminOrderService adminOrderService;

    @GetMapping("/admin")
    public String admin(HttpServletRequest request) {
        return "redirect:/admin/client/0";
    }

    @GetMapping("/admin/client/{page}")
    public String adminClient(@PathVariable("page") int page, HttpServletRequest request) {
        Page<ClientPrivacyResponseDto> privacyPage = adminService.privacyList(page, 4, "clientId", false, CookieUtils.getCookieValue(request, "access"));

        request.setAttribute("clients", privacyPage.getContent());
        request.setAttribute("page", page + 1);
        request.setAttribute("totalPage", privacyPage.getTotalPages());
        request.setAttribute("adminPage", "clientManager");
        request.setAttribute("view", "adminPage");
        request.setAttribute("activeSection","client");
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
        List<String> pointPolicyTypes = List.of("결제", "환불", "회원가입", "사진리뷰", "리뷰");
        req.setAttribute("pointTypes",pointPolicyTypes);

        return "index";
    }

    @GetMapping("/admin/orders")
    public String adminFindOrder(HttpServletRequest req,
                                 @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
                                 @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo){

        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        req.setAttribute("view", "adminPage");
        req.setAttribute("adminPage", "adminFindOrder");

        Page<OrderResponseDto> orderResponseDtoPage = adminOrderService.getAllOrder(pageSize, pageNo, "orderDatetime", "desc");

        req.setAttribute("activeSection", "order");

        req.setAttribute("orders", orderResponseDtoPage.getContent());
        req.setAttribute("totalPages", orderResponseDtoPage.getTotalPages());
        req.setAttribute("currentPage", orderResponseDtoPage.getNumber());
        req.setAttribute("pageSize", orderResponseDtoPage.getSize());

        return "index";
    }

    @GetMapping("/admin/shipping/policy")
    public String adminShippingPolicy(HttpServletRequest req, Model model){

        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        req.setAttribute("view", "adminPage");
        req.setAttribute("adminPage", "adminShippingPolicy");
        req.setAttribute("activeSection", "order");

        List<ShippingPolicyGetResponseDto> shippingPolicyGetResponseDtoList = adminOrderService.getShippingPolicies(req);
        req.setAttribute("shippingPolicyList", shippingPolicyGetResponseDtoList);

        return "index";

    }



}
