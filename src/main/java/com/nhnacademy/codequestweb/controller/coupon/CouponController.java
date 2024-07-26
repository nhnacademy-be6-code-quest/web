package com.nhnacademy.codequestweb.controller.coupon;

import com.nhnacademy.codequestweb.domain.Status;
import com.nhnacademy.codequestweb.request.coupon.CouponRegisterRequestDto;
import com.nhnacademy.codequestweb.request.review.WriteReviewRequestDto;
import com.nhnacademy.codequestweb.response.coupon.ClientCouponPaymentResponseDto;
import com.nhnacademy.codequestweb.response.coupon.CouponProvideTypeResponseDto;
import com.nhnacademy.codequestweb.response.coupon.CouponRewardMethodRequestDto;
import com.nhnacademy.codequestweb.response.coupon.CouponTypeResponseDto;
import com.nhnacademy.codequestweb.service.coupon.CouponService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
public class CouponController {
    private static final String ACCESS = "access";

    private final CouponService couponService;

    @GetMapping("/admin/coupon/client")
    public String view(HttpServletRequest req,
            Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){

        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS, CookieUtils.getCookieValue(req, ACCESS));

        Page<ClientCouponPaymentResponseDto> coupons = couponService.getClient(headers, page, size);
        model.addAttribute("couponPayments",coupons);
        return "view/coupon/coupon_client";
    }

    @GetMapping("/admin/coupon/register/{couponPolicyId}")
    public String saveCouponView(HttpServletRequest req, Model model, @PathVariable long couponPolicyId){
        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS, CookieUtils.getCookieValue(req, ACCESS));

        List<CouponTypeResponseDto> couponTypes = couponService.getAllCouponTypes(headers);
        CouponProvideTypeResponseDto name = couponService.findCouponType(headers, couponPolicyId);
        List<Status> statuses = List.of(Status.AVAILABLE, Status.USED, Status.UNAVAILABLE);
        model.addAttribute("couponTypes",couponTypes);
        model.addAttribute("status",statuses);
        model.addAttribute("couponPolicyId",couponPolicyId);
        model.addAttribute("name", name);
        return "view/coupon/admin_coupon_register";
    }

    @PostMapping("/api/coupon/register/{couponPolicyId}")
    public String saveCoupon(HttpServletRequest req, @Valid @PathVariable long couponPolicyId, @ModelAttribute CouponRegisterRequestDto couponRegisterRequestDto){
        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS, CookieUtils.getCookieValue(req, ACCESS));
        couponService.saveCoupon(headers, couponRegisterRequestDto,couponPolicyId);
        return "redirect:/admin/coupon/policy";
    }

    @PostMapping("/coupon/reward")
    public String couponReward(@ModelAttribute CouponRewardMethodRequestDto couponRewardMethodRequestDto, HttpServletRequest req, RedirectAttributes redirectAttributes
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ACCESS, CookieUtils.getCookieValue(req, ACCESS));
        String message = couponService.rewardCoupon(headers, couponRewardMethodRequestDto);

        redirectAttributes.addFlashAttribute("alterMessage", message);
        return "redirect:/";
    }

}
