package com.nhnacademy.codequestweb.response.point;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TotalPointAmountResponseDtoTest {
    @Test
    void testNoArgsConstructor() {
        // Given
        TotalPointAmountResponseDto dto;
        // When
        dto = new TotalPointAmountResponseDto();
        // Then
        assertNotNull(dto);
        assertEquals(null, dto.getTotalPoint()); // Check default value for Long (null)
        assertEquals(null, dto.getPointAccumulationRate()); // Check default value for Long (null)
    }
    @Test
    void testSetterAndGetter() {
        // Given
        Long totalPoint = 1000L;
        Long pointAccumulationRate = 5L;
        // When
        TotalPointAmountResponseDto dto = new TotalPointAmountResponseDto();
        dto.setTotalPoint(totalPoint);
        dto.setPointAccumulationRate(pointAccumulationRate);
        // Then
        assertEquals(totalPoint, dto.getTotalPoint());
        assertEquals(pointAccumulationRate, dto.getPointAccumulationRate());
    }
}