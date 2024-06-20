package com.nhnacademy.codequestweb.client.coupon;

import com.nhnacademy.codequestweb.response.auth.coupon.CouponTypeResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name="couponTypeClient", url = "http://localhost:8006")
public interface CouponTypeClient {

    @GetMapping("/admin/type")
    List<CouponTypeResponseDto> findAllType();
}
