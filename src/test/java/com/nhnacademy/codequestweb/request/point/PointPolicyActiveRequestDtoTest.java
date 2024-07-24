package com.nhnacademy.codequestweb.request.point;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PointPolicyActiveRequestDtoTest {

    @Test
    void testNoArgsConstructor() {
        PointPolicyActiveRequestDto dto = new PointPolicyActiveRequestDto();
        assertNotNull(dto);
    }

    @Test
    void testSetterAndGetter() {
        PointPolicyActiveRequestDto dto = new PointPolicyActiveRequestDto();

        dto.setPointPolicyId(1L);
        dto.setPointAccumulationType("PURCHASE");

        assertEquals(1L, dto.getPointPolicyId());
        assertEquals("PURCHASE", dto.getPointAccumulationType());
    }
}