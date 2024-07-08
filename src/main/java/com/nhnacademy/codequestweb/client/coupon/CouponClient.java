package com.nhnacademy.codequestweb.client.coupon;

import com.nhnacademy.codequestweb.request.coupon.CouponRegisterRequestDto;
import com.nhnacademy.codequestweb.response.coupon.CouponAdminPageCouponResponseDto;
import com.nhnacademy.codequestweb.response.coupon.CouponMyPageCouponResponseDto;
import com.nhnacademy.codequestweb.response.coupon.CouponOrderResponseDto;
import com.nhnacademy.codequestweb.response.coupon.RefundCouponResponseDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(name = "couponClient", url = "http://localhost:8001")
public interface CouponClient {

    @GetMapping("/api/coupon")
    List<CouponOrderResponseDto> findClientCoupon(@RequestHeader HttpHeaders headers);

    @GetMapping("/api/coupon/myPage")
    ResponseEntity<Page<CouponMyPageCouponResponseDto>> findMyPageCoupons(@RequestHeader HttpHeaders headers, @RequestParam int page, @RequestParam int size);

    @PostMapping("/api/coupon/register/{couponPolicyId}")
    ResponseEntity<CouponRegisterRequestDto> saveCoupon(@RequestHeader HttpHeaders headers, @PathVariable long couponPolicyId, @RequestBody CouponRegisterRequestDto couponRegisterRequestDto);

    @GetMapping("/api/coupon/adminPage")
    ResponseEntity<Page<CouponAdminPageCouponResponseDto>> findUserCoupons(@RequestParam int page, @RequestParam int size);

    @PutMapping("/api/coupon/refund")
    ResponseEntity<String> refundCoupon(@RequestBody RefundCouponResponseDto refundCouponResponseDto);


}