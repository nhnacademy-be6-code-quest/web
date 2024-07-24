package com.nhnacademy.codequestweb.response.point;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PointPolicyDetailResponseDtoTest {
    @Test
    void testNoArgsConstructor() {
        // Given
        PointPolicyDetailResponseDto dto;
        // When
        dto = new PointPolicyDetailResponseDto();
        // Then
        assertNotNull(dto);
        assertEquals(null, dto.getPointAccumulationType()); // Check default value for String (null)
        assertEquals(null, dto.getPointValue()); // Check default value for Long (null)
    }
    @Test
    void testSetterAndGetter() {
        // Given
        String pointAccumulationType = "TypeA";
        Long pointValue = 100L;
        // When
        PointPolicyDetailResponseDto dto = new PointPolicyDetailResponseDto();
        dto.setPointAccumulationType(pointAccumulationType);
        dto.setPointValue(pointValue);
        // Then
        assertEquals(pointAccumulationType, dto.getPointAccumulationType());
        assertEquals(pointValue, dto.getPointValue());
    }
}