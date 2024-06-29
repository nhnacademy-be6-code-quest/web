package com.nhnacademy.codequestweb.controller.coupon;

import com.nhnacademy.codequestweb.domain.Status;
import com.nhnacademy.codequestweb.request.coupon.CouponRequestDto;
import com.nhnacademy.codequestweb.response.coupon.ClientCouponPaymentResponseDto;
import com.nhnacademy.codequestweb.response.coupon.CouponResponseDto;
import com.nhnacademy.codequestweb.response.coupon.CouponTypeResponseDto;
import com.nhnacademy.codequestweb.service.coupon.ClientCouponService;
import com.nhnacademy.codequestweb.service.coupon.CouponService;
import com.nhnacademy.codequestweb.service.coupon.CouponTypeService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@Controller
public class CouponController {
    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponTypeService couponTypeService;

    @Autowired
    private ClientCouponService clientCouponService;

    @GetMapping("/api/client/coupon")
    public String viewCoupon(Model model,HttpServletRequest req){
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));
        List<CouponResponseDto> couponList = couponService.findClientCoupon(headers);
        model.addAttribute("couponList",couponList);
        return "/view/coupon/client_coupon_view";
    }

    @GetMapping("/processUserSelection")
    public String view(HttpServletRequest req,
            Model model, @RequestParam(defaultValue = "10") int page, @RequestParam(defaultValue = "0") int size){

//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("X-User-Id","1");
//        httpHeaders.set("X-User-Role","ROLE_ADMIN");
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));
        Page<ClientCouponPaymentResponseDto> coupons = clientCouponService.getClient(headers, size, page);
        model.addAttribute("couponPayments",coupons);
        return "/view/coupon/coupon_client";
    }

    @GetMapping("/api/coupon/register/{couponPolicyId}")
    public String saveCouponView(Model model, @PathVariable long couponPolicyId){
        List<CouponTypeResponseDto> couponTypes = couponTypeService.getAllCouponTypes();

        List<Status> statuses = List.of(Status.AVAILABLE, Status.USED,Status.UNAVAILABLE);
        model.addAttribute("couponTypes",couponTypes);
        model.addAttribute("status",statuses);
        model.addAttribute("couponPolicyId",couponPolicyId);
        return "/view/coupon/admin_coupon_register";
    }

    @PostMapping("/api/coupon/register/{couponPolicyId}")
    public String saveCoupon(@Valid @PathVariable long couponPolicyId, @ModelAttribute CouponRequestDto couponRequestDto){

        couponService.saveCoupon(couponRequestDto,couponPolicyId);
        return "redirect:/api/coupon/policy";

    }

}
