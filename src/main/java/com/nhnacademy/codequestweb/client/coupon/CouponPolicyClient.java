package com.nhnacademy.codequestweb.client.coupon;

import com.nhnacademy.codequestweb.response.auth.coupon.CouponPolicyResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "couponPolicyClient", url="http://localhost:8006")
public interface CouponPolicyClient {

    @GetMapping("/admin/policy")
    List<CouponPolicyResponseDto> getAllCouponPolices();

}
