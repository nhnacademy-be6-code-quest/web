package com.nhnacademy.codequestweb.client.coupon;

import com.nhnacademy.codequestweb.request.coupon.CouponPolicyRegisterRequestDto;
import com.nhnacademy.codequestweb.response.coupon.CouponPolicyListResponseDto;
import com.nhnacademy.codequestweb.response.coupon.CouponProvideTypeResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "couponPolicyClient", url="http://localhost:8001")
public interface CouponPolicyClient {

    @GetMapping("/api/coupon/policy")
    ResponseEntity<Page<CouponPolicyListResponseDto>> getAllCouponPolices(@RequestHeader HttpHeaders headers,
        @RequestParam int page, @RequestParam int size);

    @PostMapping("/api/coupon/policy/register")
    ResponseEntity<CouponPolicyRegisterRequestDto> savePolicy(@RequestHeader HttpHeaders headers, @RequestBody CouponPolicyRegisterRequestDto couponPolicyRegisterRequestDto);

    @GetMapping("/api/coupon/policy/type/{couponPolicyId}")
    CouponProvideTypeResponseDto findCouponType(@RequestHeader HttpHeaders headers, @PathVariable long couponPolicyId);
}


