package com.nhnacademy.codequestweb.request.point;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PointUsagePaymentRequestDtoTest {

    @Test
    void testNoArgsConstructor() {
        PointUsagePaymentRequestDto dto = new PointUsagePaymentRequestDto();
        assertNotNull(dto);
        assertNull(dto.getPointUsagePayment());
    }

    @Test
    void testSetterAndGetter() {
        PointUsagePaymentRequestDto dto = new PointUsagePaymentRequestDto();

        Long pointUsagePayment = 500L;
        dto.setPointUsagePayment(pointUsagePayment);

        assertEquals(pointUsagePayment, dto.getPointUsagePayment());
    }

    @Test
    void testWithNullValue() {
        PointUsagePaymentRequestDto dto = new PointUsagePaymentRequestDto();

        assertNull(dto.getPointUsagePayment());

        dto.setPointUsagePayment(null);
        assertNull(dto.getPointUsagePayment());
    }

    @Test
    void testWithZeroValue() {
        PointUsagePaymentRequestDto dto = new PointUsagePaymentRequestDto();

        dto.setPointUsagePayment(0L);
        assertEquals(0L, dto.getPointUsagePayment());
    }
}