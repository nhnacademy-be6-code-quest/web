package com.nhnacademy.codequestweb.request.point;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PointPolicyRegisterRequestDtoTest {

    @Test
    void testNoArgsConstructor() {
        PointPolicyRegisterRequestDto dto = new PointPolicyRegisterRequestDto();
        assertNotNull(dto);
    }

    @Test
    void testSetterAndGetter() {
        PointPolicyRegisterRequestDto dto = new PointPolicyRegisterRequestDto();

        dto.setPointAccumulationType("PURCHASE");
        dto.setPointValue(100L);

        assertEquals("PURCHASE", dto.getPointAccumulationType());
        assertEquals(100L, dto.getPointValue());
    }

    @Test
    void testWithNullValues() {
        PointPolicyRegisterRequestDto dto = new PointPolicyRegisterRequestDto();

        assertNull(dto.getPointAccumulationType());
        assertNull(dto.getPointValue());

        dto.setPointAccumulationType(null);
        dto.setPointValue(null);

        assertNull(dto.getPointAccumulationType());
        assertNull(dto.getPointValue());
    }
}