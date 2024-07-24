package com.nhnacademy.codequestweb.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PointPolicyTypeTest {

    @Test
    void testEnumValues() {
        assertEquals(4, PointPolicyType.values().length);
        assertEquals(PointPolicyType.REVIEW, PointPolicyType.valueOf("REVIEW"));
        assertEquals(PointPolicyType.MEMBERSHIP, PointPolicyType.valueOf("MEMBERSHIP"));
        assertEquals(PointPolicyType.REFUND, PointPolicyType.valueOf("REFUND"));
        assertEquals(PointPolicyType.PAYMENT, PointPolicyType.valueOf("PAYMENT"));
    }

    @Test
    void testReviewEnum() {
        assertEquals(0, PointPolicyType.REVIEW.getCode());
        assertEquals("리뷰", PointPolicyType.REVIEW.getValue());
    }

    @Test
    void testMembershipEnum() {
        assertEquals(1, PointPolicyType.MEMBERSHIP.getCode());
        assertEquals("회원가입", PointPolicyType.MEMBERSHIP.getValue());
    }

    @Test
    void testRefundEnum() {
        assertEquals(2, PointPolicyType.REFUND.getCode());
        assertEquals("환불", PointPolicyType.REFUND.getValue());
    }

    @Test
    void testPaymentEnum() {
        assertEquals(3, PointPolicyType.PAYMENT.getCode());
        assertEquals("결제", PointPolicyType.PAYMENT.getValue());
    }

    @Test
    void testEnumOrder() {
        PointPolicyType[] types = PointPolicyType.values();
        assertEquals(PointPolicyType.REVIEW, types[0]);
        assertEquals(PointPolicyType.MEMBERSHIP, types[1]);
        assertEquals(PointPolicyType.REFUND, types[2]);
        assertEquals(PointPolicyType.PAYMENT, types[3]);
    }

    @Test
    void testInvalidEnumValue() {
        assertThrows(IllegalArgumentException.class, () -> PointPolicyType.valueOf("INVALID"));
    }
}