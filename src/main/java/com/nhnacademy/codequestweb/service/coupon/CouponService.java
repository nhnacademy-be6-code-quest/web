package com.nhnacademy.codequestweb.service.coupon;

import com.nhnacademy.codequestweb.client.coupon.CouponClient;
import com.nhnacademy.codequestweb.request.coupon.CouponRequestDto;
import com.nhnacademy.codequestweb.response.coupon.CouponResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;


@Service
@Slf4j
public class CouponService {

    @Autowired
    private CouponClient couponClient;



    public ResponseEntity<CouponRequestDto> saveCoupon(CouponRequestDto couponRequestDto, long couponPolicyId){
        log.error("{}",couponRequestDto);

        return couponClient.saveCoupon(couponPolicyId, couponRequestDto);
    }
    public List<CouponResponseDto> findClientCoupon(HttpHeaders headers){
        return couponClient.viewCoupons(headers);
    }
}