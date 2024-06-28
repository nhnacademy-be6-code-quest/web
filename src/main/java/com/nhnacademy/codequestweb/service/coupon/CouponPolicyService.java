package com.nhnacademy.codequestweb.service.coupon;

import com.nhnacademy.codequestweb.client.coupon.CouponPolicyClient;
import com.nhnacademy.codequestweb.request.coupon.CouponPolicyRegisterRequestDto;
import com.nhnacademy.codequestweb.response.coupon.CouponPolicyResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CouponPolicyService {

    @Autowired
    private CouponPolicyClient couponPolicyClient;

    public Page<CouponPolicyResponseDto> getAllCouponPolicies(Pageable pageable) {
       return couponPolicyClient.getAllCouponPolices(pageable);
    }

    public ResponseEntity<CouponPolicyRegisterRequestDto> savePolicy(CouponPolicyRegisterRequestDto couponPolicyRegisterRequestDto){
       return couponPolicyClient.savePolicy(couponPolicyRegisterRequestDto);
    }

}
