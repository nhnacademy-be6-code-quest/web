package com.nhnacademy.codequestweb.client.coupon;

import com.nhnacademy.codequestweb.request.coupon.CouponPolicyRequestDto;
import com.nhnacademy.codequestweb.response.auth.coupon.CouponPolicyResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "couponPolicyClient", url="http://localhost:8006")
public interface CouponPolicyClient {

    @GetMapping("/admin/coupon/policy")
    Page<CouponPolicyResponseDto> getAllCouponPolices(Pageable pageable);

    @GetMapping("/admin/coupon/policy/{couponPolicyId}")
    CouponPolicyResponseDto getCouponPolicy(@PathVariable long couponPolicyId);

    @PostMapping("/admin/coupon/policy/register")
    ResponseEntity<CouponPolicyRequestDto> savePolicy(@RequestBody CouponPolicyRequestDto couponPolicyRequestDto);



}
