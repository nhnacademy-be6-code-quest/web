package com.nhnacademy.codequestweb.request.point;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PointRewardOrderRequestDtoTest {

    @Test
    void testNoArgsConstructor() {
        PointRewardOrderRequestDto dto = new PointRewardOrderRequestDto();
        assertNotNull(dto);
        assertNull(dto.getAccumulatedPoint());
    }

    @Test
    void testSetterAndGetter() {
        PointRewardOrderRequestDto dto = new PointRewardOrderRequestDto();

        Long accumulatedPoint = 1000L;
        dto.setAccumulatedPoint(accumulatedPoint);

        assertEquals(accumulatedPoint, dto.getAccumulatedPoint());
    }

    @Test
    void testWithNullValue() {
        PointRewardOrderRequestDto dto = new PointRewardOrderRequestDto();

        assertNull(dto.getAccumulatedPoint());

        dto.setAccumulatedPoint(null);
        assertNull(dto.getAccumulatedPoint());
    }

    @Test
    void testWithZeroValue() {
        PointRewardOrderRequestDto dto = new PointRewardOrderRequestDto();

        dto.setAccumulatedPoint(0L);
        assertEquals(0L, dto.getAccumulatedPoint());
    }
}