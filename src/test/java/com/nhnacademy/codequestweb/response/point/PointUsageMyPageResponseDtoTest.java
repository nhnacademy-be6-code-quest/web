package com.nhnacademy.codequestweb.response.point;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PointUsageMyPageResponseDtoTest {
    @Test
    void testNoArgsConstructor() {
        // Given
        PointUsageMyPageResponseDto dto;
        // When
        dto = new PointUsageMyPageResponseDto();
        // Then
        assertNotNull(dto);
        assertEquals(null, dto.getPointUsageHistoryDate()); // Check default value for String (null)
        assertEquals(null, dto.getPointUsageAmount()); // Check default value for Long (null)
        assertEquals(null, dto.getPointUsageType()); // Check default value for String (null)
    }
    @Test
    void testSetterAndGetter() {
        // Given
        String pointUsageHistoryDate = "2023-01-15";
        Long pointUsageAmount = 50L;
        String pointUsageType = "Payment";
        // When
        PointUsageMyPageResponseDto dto = new PointUsageMyPageResponseDto();
        dto.setPointUsageHistoryDate(pointUsageHistoryDate);
        dto.setPointUsageAmount(pointUsageAmount);
        dto.setPointUsageType(pointUsageType);
        // Then
        assertEquals(pointUsageHistoryDate, dto.getPointUsageHistoryDate());
        assertEquals(pointUsageAmount, dto.getPointUsageAmount());
        assertEquals(pointUsageType, dto.getPointUsageType());
    }
}