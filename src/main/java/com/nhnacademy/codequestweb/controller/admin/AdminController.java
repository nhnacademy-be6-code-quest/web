package com.nhnacademy.codequestweb.controller.admin;

import com.nhnacademy.codequestweb.response.mypage.ClientPrivacyResponseDto;
import com.nhnacademy.codequestweb.response.order.common.OrderResponseDto;
import com.nhnacademy.codequestweb.response.point.PointAccumulationAdminPageResponseDto;
import com.nhnacademy.codequestweb.response.point.PointPolicyAdminListResponseDto;
import com.nhnacademy.codequestweb.response.point.PointUsageAdminPageResponseDto;
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
    private static final String ACCESS = "access";
    private static final String ADMIN_PAGE = "adminPage";
    private static final String ACTIVE_SECTION = "activeSection";
    private static final String INDEX = "index";
    private static final String POINT = "point";
    private static final String POINTS = "points";

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
        Page<ClientPrivacyResponseDto> privacyPage = adminService.privacyList(page, 4, "clientId", false, CookieUtils.getCookieValue(request, ACCESS));

        request.setAttribute("clients", privacyPage.getContent());
        request.setAttribute("page", page + 1);
        request.setAttribute("totalPage", privacyPage.getTotalPages());
        request.setAttribute(ADMIN_PAGE, "clientManager");
        request.setAttribute("view", ADMIN_PAGE);
        request.setAttribute(ACTIVE_SECTION,"client");
        return INDEX;
    }

    @ExceptionHandler(FeignException.Unauthorized.class)
    public String unauthorized(FeignException.Unauthorized e, HttpServletRequest request) {
        if (CookieUtils.getCookieValue(request, ACCESS) == null) {
            return "redirect:/auth";
        }
        return "redirect:/";
    }

    @GetMapping("/admin/point/reward")
    public String adminPageRewardPoint (HttpServletRequest req, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS, CookieUtils.getCookieValue(req, ACCESS));

        req.setAttribute("view", ADMIN_PAGE);
        req.setAttribute(ADMIN_PAGE, "pointReward");
        req.setAttribute(ACTIVE_SECTION, POINT);
        Page<PointAccumulationAdminPageResponseDto> dto = pointAccumulationService.userPoint(headers, page, size);
        req.setAttribute(POINTS, dto);
        return INDEX;
    }

    @GetMapping("/admin/point/use")
    public String adminPagUsePoint (HttpServletRequest req, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS, CookieUtils.getCookieValue(req, ACCESS));

        req.setAttribute("view", ADMIN_PAGE);
        req.setAttribute(ADMIN_PAGE, "pointUsed");
        req.setAttribute(ACTIVE_SECTION, POINT);
        Page<PointUsageAdminPageResponseDto> dto = pointUsageService.userUsePoint(headers, page, size);
        req.setAttribute(POINTS, dto);
        return INDEX;
    }

    @GetMapping("/admin/point/policy")
    public String pointPolicy (HttpServletRequest req, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS, CookieUtils.getCookieValue(req, ACCESS));

        req.setAttribute("view", ADMIN_PAGE);
        req.setAttribute(ADMIN_PAGE, "pointPolicy");
        req.setAttribute(ACTIVE_SECTION, POINT);
        Page<PointPolicyAdminListResponseDto> dto = pointPolicyService.findPointPolicies(headers, page, size);
        req.setAttribute(POINTS, dto);
        List<String> pointPolicyTypes = List.of("회원가입", "사진리뷰", "리뷰");
        req.setAttribute("pointTypes",pointPolicyTypes);

        return INDEX;
    }

    @GetMapping("/admin/orders")
    public String adminFindOrder(HttpServletRequest req,
                                 @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize,
                                 @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo){

        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS, CookieUtils.getCookieValue(req, ACCESS));

        req.setAttribute("view", ADMIN_PAGE);
        req.setAttribute(ADMIN_PAGE, "adminFindOrder");

        Page<OrderResponseDto> orderResponseDtoPage = adminOrderService.getAllOrder(pageSize, pageNo, "orderDatetime", "desc");

        req.setAttribute(ACTIVE_SECTION, "order");

        req.setAttribute("orders", orderResponseDtoPage.getContent());
        req.setAttribute("totalPages", orderResponseDtoPage.getTotalPages());
        req.setAttribute("currentPage", orderResponseDtoPage.   getNumber());
        req.setAttribute("pageSize", orderResponseDtoPage.getSize());

        return INDEX;
    }

    @GetMapping("/admin/shipping/policy")
    public String adminShippingPolicy(HttpServletRequest req, Model model){

        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS, CookieUtils.getCookieValue(req, ACCESS));

        req.setAttribute("view", ADMIN_PAGE);
        req.setAttribute(ADMIN_PAGE, "adminShippingPolicy");
        req.setAttribute(ACTIVE_SECTION, "order");

        List<ShippingPolicyGetResponseDto> shippingPolicyGetResponseDtoList = adminOrderService.getShippingPolicies(req);
        req.setAttribute("shippingPolicyList", shippingPolicyGetResponseDtoList);

        return INDEX;
    }

}
