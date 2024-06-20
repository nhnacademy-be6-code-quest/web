package com.nhnacademy.codequestweb.service.coupon;

import com.nhnacademy.codequestweb.client.coupon.CouponPolicyClient;
import com.nhnacademy.codequestweb.response.auth.coupon.CouponPolicyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponPolicyService {

    @Autowired
    private CouponPolicyClient couponPolicyClient;

    public List<CouponPolicyResponseDto> getAllCouponPolicies(){
        return couponPolicyClient.getAllCouponPolices();
    }

}
