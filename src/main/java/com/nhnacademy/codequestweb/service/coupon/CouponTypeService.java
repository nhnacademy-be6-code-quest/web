package com.nhnacademy.codequestweb.service.coupon;

import com.nhnacademy.codequestweb.client.coupon.CouponTypeClient;
import com.nhnacademy.codequestweb.response.coupon.CouponTypeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CouponTypeService {

    private final CouponTypeClient couponTypeClient;
    public List<CouponTypeResponseDto> getAllCouponTypes(){
        return couponTypeClient.findAllType();
    }
}
