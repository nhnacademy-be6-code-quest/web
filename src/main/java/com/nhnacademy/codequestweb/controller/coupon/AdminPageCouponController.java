package com.nhnacademy.codequestweb.controller.coupon;

import com.nhnacademy.codequestweb.domain.Status;
import com.nhnacademy.codequestweb.response.coupon.CouponAdminPageCouponResponseDto;
import com.nhnacademy.codequestweb.service.coupon.CouponService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/user/coupons")
public class AdminPageCouponController {

    private final CouponService couponService;
    @GetMapping
    public String viewAdminCoupon(HttpServletRequest req, @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "6") int size, @RequestParam(defaultValue = "AVAILABLE") Status status) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));

        Page<CouponAdminPageCouponResponseDto> adminCoupons = couponService.findUsersCoupons(headers, page, size, status);
        req.setAttribute("view", "adminPage");
        req.setAttribute("adminPage", "adminCoupons");
        req.setAttribute("activeSection", "coupon");
        req.setAttribute("adminCoupons", adminCoupons);
        req.setAttribute("totalPages", adminCoupons.getTotalPages());
        req.setAttribute("currentPage", adminCoupons.getNumber());
        req.setAttribute("pageSize", adminCoupons.getSize());
        req.setAttribute("status",status);
        return "index";
    }

    @GetMapping("/AVAILABLE")
    public String viewActiveUserCoupons(HttpServletRequest req,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "6") int size) {
        return viewAdminCoupon(req, page, size, Status.AVAILABLE);
    }

    @GetMapping("/expired")
    public String viewExpiredUserCoupons(HttpServletRequest req,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "6") int size) {
        return viewAdminCoupon(req, page, size, Status.UNAVAILABLE);
    }

    @GetMapping("/used")
    public String viewUsedUserCoupons(HttpServletRequest req,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "6") int size) {
        return viewAdminCoupon(req, page, size, Status.USED);
    }
}
