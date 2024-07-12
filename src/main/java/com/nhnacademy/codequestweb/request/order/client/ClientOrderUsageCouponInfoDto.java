package com.nhnacademy.codequestweb.request.order.client;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ClientOrderUsageCouponInfoDto {
    private Long productId;
    private Long couponId;

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ClientOrderUsageCouponInfoDto)) return false;
        ClientOrderUsageCouponInfoDto other = (ClientOrderUsageCouponInfoDto) obj;
        if(other.getProductId().equals(productId) && other.getCouponId().equals(couponId)) return true;
        else return false;
    }

}
