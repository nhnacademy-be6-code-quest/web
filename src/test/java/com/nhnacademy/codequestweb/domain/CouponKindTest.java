package com.nhnacademy.codequestweb.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CouponKindTest {
    @Test
    void testCouponKindEnumValues() {
        // Test values initialization
        assertEquals(0, CouponKind.WELCOME.getCode());
        assertEquals("회원가입", CouponKind.WELCOME.getValue());
        assertEquals(1, CouponKind.BIRTHDAY.getCode());
        assertEquals("생일", CouponKind.BIRTHDAY.getValue());
        assertEquals(2, CouponKind.BOOK.getCode());
        assertEquals("상품", CouponKind.BOOK.getValue());
        assertEquals(3, CouponKind.CATEGORY.getCode());
        assertEquals("상품 카테고리", CouponKind.CATEGORY.getValue());
        assertEquals(4, CouponKind.DISCOUNT.getCode());
        assertEquals("금액 할인", CouponKind.DISCOUNT.getValue());
    }
}