package com.nhnacademy.codequestweb.response.point;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class PointPolicyAdminListResponseDtoTest {
    @Test
    void testNoArgsConstructor() {
        // Given
        PointPolicyAdminListResponseDto dto;
        // When
        dto = new PointPolicyAdminListResponseDto();
        // Then
        assertNotNull(dto);
        assertEquals(0, dto.getPointPolicyId()); // Check default value for long (0)
        assertNull(dto.getPointAccumulationType()); // Check default value for String (null)
        assertNull(dto.getPointValue()); // Check default value for Long (null)
        assertNull(dto.getPointPolicyCreationDate()); // Check default value for String (null)
        assertNull(dto.getPointStatus()); // Check default value for String (null)
    }
    @Test
    void testSetterAndGetter() {
        // Given
        long pointPolicyId = 1L;
        String pointAccumulationType = "TypeA";
        Long pointValue = 100L;
        String pointPolicyCreationDate = "2023-07-15";
        String pointStatus = "Active";
        // When
        PointPolicyAdminListResponseDto dto = new PointPolicyAdminListResponseDto();
        dto.setPointPolicyId(pointPolicyId);
        dto.setPointAccumulationType(pointAccumulationType);
        dto.setPointValue(pointValue);
        dto.setPointPolicyCreationDate(pointPolicyCreationDate);
        dto.setPointStatus(pointStatus);
        // Then
        assertEquals(pointPolicyId, dto.getPointPolicyId());
        assertEquals(pointAccumulationType, dto.getPointAccumulationType());
        assertEquals(pointPolicyCreationDate, dto.getPointPolicyCreationDate());
        assertEquals(pointStatus, dto.getPointStatus());
    }
}