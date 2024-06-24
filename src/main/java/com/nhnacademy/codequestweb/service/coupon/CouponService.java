package com.nhnacademy.codequestweb.service.coupon;

import com.nhnacademy.codequestweb.client.coupon.CouponClient;
import com.nhnacademy.codequestweb.request.coupon.CouponRequestDto;
import com.nhnacademy.codequestweb.response.coupon.CouponResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CouponService {

    @Autowired
    private CouponClient couponClient;

    public ResponseEntity<CouponRequestDto> saveCoupon(CouponRequestDto couponRequestDto, long couponPolicyId){
        return couponClient.saveCoupon(couponPolicyId, couponRequestDto);
    }
    public List<CouponResponseDto> findClientCoupon(long clientId){
        return couponClient.viewCoupons(clientId);
    }
}
