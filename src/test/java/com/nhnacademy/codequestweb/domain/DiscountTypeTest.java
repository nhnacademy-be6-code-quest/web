package com.nhnacademy.codequestweb.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiscountTypeTest {
    @Test
    void testDiscountTypeEnumValues() {
        // Test values initialization
        assertEquals(0, DiscountType.AMOUNTDISCOUNT.getCode());
        assertEquals("금액 할인", DiscountType.AMOUNTDISCOUNT.getValue());
        assertEquals(1, DiscountType.PERCENTAGEDISCOUNT.getCode());
        assertEquals("퍼센트 할인", DiscountType.PERCENTAGEDISCOUNT.getValue());
    }
}