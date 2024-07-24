package com.nhnacademy.codequestweb.response.coupon;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class CouponProvideTypeResponseDtoTest {
    @Test
    void testNoArgsConstructor() {
        // Test that the no-args constructor initializes the object
        CouponProvideTypeResponseDto dto = new CouponProvideTypeResponseDto();
        assertThat(dto).isNotNull();
        assertThat(dto.getName()).isNull();
    }
    @Test
    void testGetterAndSetter() {
        // Test that the setter sets the correct value and the getter retrieves it
        CouponProvideTypeResponseDto dto = new CouponProvideTypeResponseDto();
        String name = "SampleName";
        dto.setName(name);
        assertThat(dto.getName()).isEqualTo(name);
    }
}