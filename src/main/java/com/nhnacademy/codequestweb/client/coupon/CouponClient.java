package com.nhnacademy.codequestweb.client.coupon;

import com.nhnacademy.codequestweb.request.coupon.CouponRequestDto;
import com.nhnacademy.codequestweb.response.coupon.CouponResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@FeignClient(name = "couponClient", url = "http://localhost:8001")
//@FeignClient(name = "couponClient", url = "http://10.220.222.13:8001")
public interface CouponClient {

    @GetMapping("/api/coupon")
    List<CouponResponseDto> viewCoupons(long clientId);

    @PostMapping("/api/coupon/register/{couponPolicyId}")
    ResponseEntity<CouponRequestDto> saveCoupon(@PathVariable long couponPolicyId, @RequestBody CouponRequestDto couponRequestDto);
    // @RequestBody : Object -> Json

}