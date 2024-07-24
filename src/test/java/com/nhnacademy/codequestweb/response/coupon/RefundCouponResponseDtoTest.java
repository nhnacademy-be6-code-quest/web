package com.nhnacademy.codequestweb.response.coupon;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RefundCouponResponseDtoTest {

    @Test
    void testNoArgsConstructor() {
        RefundCouponResponseDto dto = new RefundCouponResponseDto();
        assertNotNull(dto);
    }

    @Test
    void testGetter() {
        RefundCouponResponseDto dto = new RefundCouponResponseDto();
        assertEquals(0, dto.getCouponId());
    }
}