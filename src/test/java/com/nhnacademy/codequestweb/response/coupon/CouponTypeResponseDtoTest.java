package com.nhnacademy.codequestweb.response.coupon;


import com.nhnacademy.codequestweb.domain.CouponKind;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class CouponTypeResponseDtoTest {
    @Test
    void testNoArgsConstructor() {
        // Test that the no-args constructor initializes the object
        CouponTypeResponseDto dto = new CouponTypeResponseDto();
        assertThat(dto).isNotNull();
        assertThat(dto.getCouponTypeId()).isZero();
        assertThat(dto.getCouponKind()).isNull();
    }
    @Test
    void testAllArgsConstructor() {
        // Test that the all-args constructor initializes the object with the given values
        long couponTypeId = 123L;
        CouponKind couponKind = CouponKind.DISCOUNT; // Assuming CouponKind is an enum with a value DISCOUNT
        CouponTypeResponseDto dto = new CouponTypeResponseDto();
        dto.setCouponTypeId(couponTypeId);
        dto.setCouponKind(couponKind);
        assertThat(dto.getCouponTypeId()).isEqualTo(couponTypeId);
        assertThat(dto.getCouponKind()).isEqualTo(couponKind);
    }
}