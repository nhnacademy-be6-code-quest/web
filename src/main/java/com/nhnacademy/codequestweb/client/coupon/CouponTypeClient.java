package com.nhnacademy.codequestweb.client.coupon;

import com.nhnacademy.codequestweb.response.coupon.CouponTypeResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name="couponTypeClient", url = "http://localhost:8001")
public interface CouponTypeClient {

    @GetMapping("/api/coupon/type")
    List<CouponTypeResponseDto> findAllType();
}
