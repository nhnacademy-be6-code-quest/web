package com.nhnacademy.codequestweb.response.point;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PointAccumulationAdminPageResponseDtoTest {

    @Test
    void testNoArgsConstructor() {
        PointAccumulationAdminPageResponseDto dto = new PointAccumulationAdminPageResponseDto();
        assertNotNull(dto);
        assertNull(dto.getPointAccumulationAmount());
        assertNull(dto.getPointAccumulationHistoryDate());
        assertNull(dto.getPointAccumulationType());
        assertNull(dto.getClientName());
    }

    @Test
    void testSetterAndGetter() {
        PointAccumulationAdminPageResponseDto dto = new PointAccumulationAdminPageResponseDto();

        Long amount = 1000L;
        String date = "2023-05-20";
        String type = "PURCHASE";
        String name = "John Doe";

        dto.setPointAccumulationAmount(amount);
        dto.setPointAccumulationHistoryDate(date);
        dto.setPointAccumulationType(type);
        dto.setClientName(name);

        assertEquals(amount, dto.getPointAccumulationAmount());
        assertEquals(date, dto.getPointAccumulationHistoryDate());
        assertEquals(type, dto.getPointAccumulationType());
        assertEquals(name, dto.getClientName());
    }

    @Test
    void testSetAndGetNullValues() {
        PointAccumulationAdminPageResponseDto dto = new PointAccumulationAdminPageResponseDto();

        dto.setPointAccumulationAmount(null);
        dto.setPointAccumulationHistoryDate(null);
        dto.setPointAccumulationType(null);
        dto.setClientName(null);

        assertNull(dto.getPointAccumulationAmount());
        assertNull(dto.getPointAccumulationHistoryDate());
        assertNull(dto.getPointAccumulationType());
        assertNull(dto.getClientName());
    }
}