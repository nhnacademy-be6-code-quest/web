package com.nhnacademy.codequestweb.controller.coupon;

import com.nhnacademy.codequestweb.domain.Status;
import com.nhnacademy.codequestweb.request.coupon.CouponRequestDto;
import com.nhnacademy.codequestweb.response.coupon.CouponPolicyResponseDto;
import com.nhnacademy.codequestweb.response.coupon.CouponResponseDto;
import com.nhnacademy.codequestweb.response.coupon.CouponTypeResponseDto;
import com.nhnacademy.codequestweb.service.coupon.CouponPolicyService;
import com.nhnacademy.codequestweb.service.coupon.CouponService;
import com.nhnacademy.codequestweb.service.coupon.CouponTypeService;
import com.nhnacademy.codequestweb.temp.Client;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CouponController {
    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponPolicyService couponPolicyService;
    @Autowired
    private CouponTypeService couponTypeService;

    @GetMapping("/api/client/{clientId}")
    public String viewCoupon(@PathVariable long clientId, Model model){
        List<CouponResponseDto> couponList = couponService.findClientCoupon(clientId);
        model.addAttribute("couponList",couponList);
        return "/view/coupon/client_coupon_view";
    }

    @GetMapping("/api/coupon/register/{couponPolicyId}")
    public String saveCouponView(Model model, @PathVariable long couponPolicyId){
        List<CouponTypeResponseDto> couponTypes = couponTypeService.getAllCouponTypes();
        CouponPolicyResponseDto couponPolicy = couponPolicyService.getCouponPolicy(couponPolicyId);
        List<Status> statuses = List.of(Status.AVAILABLE, Status.USED,Status.UNAVAILABLE);
        List<Client> clients = new ArrayList<>();
        clients.add(new Client(1L,"김채호"));
        clients.add(new Client(2L,"전민선"));
        model.addAttribute("clients",clients);
        model.addAttribute("couponTypes",couponTypes);
        model.addAttribute("couponPolicy",couponPolicy);
        model.addAttribute("status",statuses);
        model.addAttribute("couponPolicyId",couponPolicyId);
        return "/view/coupon/admin_coupon_register";
    }

    @PostMapping("/api/coupon/register/{couponPolicyId}")
    public String saveCoupon(@PathVariable long couponPolicyId, @ModelAttribute CouponRequestDto couponRequestDto){
        couponService.saveCoupon(couponRequestDto,couponPolicyId);
        return "redirect:/api/coupon/policy";

    }
}