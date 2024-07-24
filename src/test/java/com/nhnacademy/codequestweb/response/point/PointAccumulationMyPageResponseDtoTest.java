package com.nhnacademy.codequestweb.response.point;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PointAccumulationMyPageResponseDtoTest {

    @Test
    void testNoArgsConstructor() {
        PointAccumulationMyPageResponseDto dto = new PointAccumulationMyPageResponseDto();
        assertNotNull(dto);
        assertNull(dto.getPointAccumulationAmount());
        assertNull(dto.getPointAccumulationHistoryDate());
        assertNull(dto.getPointAccumulationType());
    }

    @Test
    void testSetterAndGetter() {
        PointAccumulationMyPageResponseDto dto = new PointAccumulationMyPageResponseDto();

        Long amount = 1000L;
        String date = "2023-05-20";
        String type = "PURCHASE";

        dto.setPointAccumulationAmount(amount);
        dto.setPointAccumulationHistoryDate(date);
        dto.setPointAccumulationType(type);

        assertEquals(amount, dto.getPointAccumulationAmount());
        assertEquals(date, dto.getPointAccumulationHistoryDate());
        assertEquals(type, dto.getPointAccumulationType());
    }

    @Test
    void testSetAndGetNullValues() {
        PointAccumulationMyPageResponseDto dto = new PointAccumulationMyPageResponseDto();

        dto.setPointAccumulationAmount(null);
        dto.setPointAccumulationHistoryDate(null);
        dto.setPointAccumulationType(null);

        assertNull(dto.getPointAccumulationAmount());
        assertNull(dto.getPointAccumulationHistoryDate());
        assertNull(dto.getPointAccumulationType());
    }

    @Test
    void testSetAndGetZeroAmount() {
        PointAccumulationMyPageResponseDto dto = new PointAccumulationMyPageResponseDto();

        dto.setPointAccumulationAmount(0L);

        assertEquals(0L, dto.getPointAccumulationAmount());
    }
}