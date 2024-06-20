package com.nhnacademy.codequestweb.request.payment;

import com.nhnacademy.codequestweb.temp.Coupon;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class PaymentRequestDto {
    @NotNull
    private Long remainingPoints;   // 사용자가 보유하고 있는 포인트
    private Long pointToUse;        // 이번 결제에서 사용할 포인트
    private List<Coupon> coupons;   // 사용자가 보유하고 있는 쿠폰들
    private Long couponId;          // 이번 결제에서 사용할 쿠폰의 아이디
    private Long originalAmount;    // 원래 결제해야 하는 금액 (주문에서 받아 옴)
    private Long finalAmount;       // 최종으로 결제해야 하는 값 (주문에서 받아 옴 * 쿠폰 - 포인트로 계산)
    private Long expectedPoints;    // 최종으로 결제해야 하는 값 (포인트 정책과 곱해서 계산)
    private Long paymentMethodId;   // 결제 수단의 아이디
}
