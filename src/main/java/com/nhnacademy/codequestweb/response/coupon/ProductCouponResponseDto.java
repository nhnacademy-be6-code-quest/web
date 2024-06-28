package com.nhnacademy.codequestweb.response.coupon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@JsonIgnoreProperties(ignoreUnknown = true)

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductCouponResponseDto {
    long productId;
}
