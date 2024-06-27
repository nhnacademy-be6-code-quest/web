package com.nhnacademy.codequestweb.client.coupon;

import com.nhnacademy.codequestweb.request.coupon.CouponPolicyRequestDto;
import com.nhnacademy.codequestweb.response.coupon.CouponPolicyResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "couponPolicyClient", url="http://10.220.222.49:8001")
public interface CouponPolicyClient {

    @GetMapping("/api/coupon/policy")
    Page<CouponPolicyResponseDto> getAllCouponPolices(Pageable pageable);

    @PostMapping("/api/coupon/policy/register")
    ResponseEntity<CouponPolicyRequestDto> savePolicy(@RequestBody CouponPolicyRequestDto couponPolicyRequestDto);
}
