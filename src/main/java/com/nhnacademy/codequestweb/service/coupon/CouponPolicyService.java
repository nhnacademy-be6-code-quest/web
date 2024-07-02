package com.nhnacademy.codequestweb.service.coupon;

import com.nhnacademy.codequestweb.client.coupon.CouponPolicyClient;
import com.nhnacademy.codequestweb.request.coupon.CouponPolicyRegisterRequestDto;
import com.nhnacademy.codequestweb.response.coupon.CouponPolicyListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CouponPolicyService {


    private final CouponPolicyClient couponPolicyClient;

    public Page<CouponPolicyListResponseDto> getAllCouponPolicies(Pageable pageable) {


       return couponPolicyClient.getAllCouponPolices(pageable).getBody();
    }

    public ResponseEntity<CouponPolicyRegisterRequestDto> savePolicy(CouponPolicyRegisterRequestDto couponPolicyRegisterRequestDto){
       return couponPolicyClient.savePolicy(couponPolicyRegisterRequestDto);
    }

}
