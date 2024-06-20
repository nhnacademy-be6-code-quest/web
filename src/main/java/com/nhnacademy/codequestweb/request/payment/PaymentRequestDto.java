package com.nhnacademy.codequestweb.request.payment;

import com.nhnacademy.codequestweb.temp.Coupon;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentRequestDto {
    @NotNull
    private Long pointToUse;        // 이번 결제에서 사용할 포인트
    private Long couponId;          // 이번 결제에서 사용할 쿠폰의 아이디
    private Long paymentMethodId;   // 결제 수단의 아이디
}
