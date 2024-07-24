package com.nhnacademy.codequestweb.request.point;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PointPolicyModifyRequestDtoTest {

    @Test
    void testAllArgsConstructor() {
        long pointPolicyId = 1L;
        Long pointValue = 100L;

        PointPolicyModifyRequestDto dto = new PointPolicyModifyRequestDto(pointPolicyId, pointValue);

        assertNotNull(dto);
        assertEquals(pointPolicyId, dto.getPointPolicyId());
        assertEquals(pointValue, dto.getPointValue());
    }

    @Test
    void testGetters() {
        long pointPolicyId = 2L;
        Long pointValue = 200L;

        PointPolicyModifyRequestDto dto = new PointPolicyModifyRequestDto(pointPolicyId, pointValue);

        assertEquals(pointPolicyId, dto.getPointPolicyId());
        assertEquals(pointValue, dto.getPointValue());
    }

    @Test
    void testWithNullPointValue() {
        PointPolicyModifyRequestDto dto = new PointPolicyModifyRequestDto(1L, null);

        assertNotNull(dto);
        assertEquals(1L, dto.getPointPolicyId());
        assertNull(dto.getPointValue());
    }
}