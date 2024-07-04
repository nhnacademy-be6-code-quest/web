package com.nhnacademy.codequestweb.response.coupon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@AllArgsConstructor
public class ProductCategoryCouponResponseDto {
    private long productCategoryId;
}
