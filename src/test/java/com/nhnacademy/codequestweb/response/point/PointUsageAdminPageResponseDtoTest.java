package com.nhnacademy.codequestweb.response.point;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PointUsageAdminPageResponseDtoTest {
    @Test
    void testNoArgsConstructor() {
        // Given
        PointUsageAdminPageResponseDto dto;
        // When
        dto = new PointUsageAdminPageResponseDto();
        // Then
        assertNotNull(dto);
        assertEquals(null, dto.getPointUsageHistoryDate()); // Check default value for String (null)
        assertEquals(null, dto.getPointUsageAmount()); // Check default value for Long (null)
        assertEquals(0L, dto.getClientId()); // Check default value for long (0L)
        assertEquals(null, dto.getPointUsageType()); // Check default value for String (null)
    }

    @Test
    void testSetterAndGetter() {
        // Given
        String pointUsageHistoryDate = "2023-01-15";
        Long pointUsageAmount = 50L;
        long clientId = 12345L;
        String pointUsageType = "Payment";
        // When
        PointUsageAdminPageResponseDto dto = new PointUsageAdminPageResponseDto();
        dto.setPointUsageHistoryDate(pointUsageHistoryDate);
        dto.setPointUsageAmount(pointUsageAmount);
        dto.setClientId(clientId);
        dto.setPointUsageType(pointUsageType);
        // Then
        assertEquals(pointUsageHistoryDate, dto.getPointUsageHistoryDate());
        assertEquals(pointUsageAmount, dto.getPointUsageAmount());
        assertEquals(clientId, dto.getClientId());
        assertEquals(pointUsageType, dto.getPointUsageType());
    }
}