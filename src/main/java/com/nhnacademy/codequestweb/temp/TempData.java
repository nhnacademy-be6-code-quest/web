package com.nhnacademy.codequestweb.temp;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TempData {
    Long remainingPoints;
    List<Coupon> coupons;
    Long originalAmount;
    Long finalAmount;
    Long expectedPoints;
}
