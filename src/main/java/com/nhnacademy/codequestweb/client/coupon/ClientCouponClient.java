package com.nhnacademy.codequestweb.client.coupon;

import com.nhnacademy.codequestweb.request.product.PageRequestDto;
import com.nhnacademy.codequestweb.response.coupon.ClientCouponPaymentResponseDto;
import com.nhnacademy.codequestweb.test.ProductGetResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(name="clientCoupon", url="http://10.220.222.13:8001")
@FeignClient(name="clientCoupon", url="http://localhost:8001")
public interface ClientCouponClient {

    @GetMapping("/api/client/coupon-payment")
    ResponseEntity<Page<ClientCouponPaymentResponseDto>> getCouponClient(@RequestHeader HttpHeaders httpHeaders, @RequestParam int page, @RequestParam int size);



}
