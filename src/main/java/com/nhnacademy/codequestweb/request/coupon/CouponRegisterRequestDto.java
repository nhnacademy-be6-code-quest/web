package com.nhnacademy.codequestweb.request.coupon;


import com.nhnacademy.codequestweb.domain.Status;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record CouponRegisterRequestDto(
    @NotNull
    long couponTypeId,
    @NotNull
    long couponPolicyId,
    @NotNull
    List<Long> clientId,
    @NotNull
    LocalDate expirationDate,
    @NotNull
    Status status) {

}
