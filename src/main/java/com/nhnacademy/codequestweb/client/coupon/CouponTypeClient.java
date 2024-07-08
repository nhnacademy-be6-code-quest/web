package com.nhnacademy.codequestweb.client.coupon;

import com.nhnacademy.codequestweb.response.coupon.CouponTypeResponseDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="couponTypeClient", url = "http://localhost:8001")
public interface CouponTypeClient {

    @GetMapping("/api/coupon/type")
    List<CouponTypeResponseDto> findAllType(@RequestHeader HttpHeaders headers);
}
