package com.nhnacademy.codequestweb.client.coupon;

import com.nhnacademy.codequestweb.request.coupon.CouponRequestDto;
import com.nhnacademy.codequestweb.response.coupon.CouponResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@FeignClient(name = "couponClient", url = "http://localhost:8006")
public interface CouponClient {

    @GetMapping("/api/coupon")
    List<CouponResponseDto> viewCoupons(@RequestHeader HttpHeaders httpHeaders);

    @PostMapping("/api/coupon/register/{couponPolicyId}")
    ResponseEntity<CouponRequestDto> saveCoupon(@PathVariable long couponPolicyId, @RequestBody CouponRequestDto couponRequestDto);
}