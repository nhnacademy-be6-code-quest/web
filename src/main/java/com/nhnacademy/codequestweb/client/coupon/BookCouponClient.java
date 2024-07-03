package com.nhnacademy.codequestweb.client.coupon;

import com.nhnacademy.codequestweb.test.ProductGetResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name="bookCouponClient", url="http://localhost:8001")
public interface BookCouponClient {


    @GetMapping("/api/product/admin/page/all")
    ResponseEntity<Page<ProductGetResponseDto>> getAllProducts(@RequestHeader HttpHeaders headers,@RequestParam Integer page,@RequestParam String sort,@RequestParam Boolean desc);

}
