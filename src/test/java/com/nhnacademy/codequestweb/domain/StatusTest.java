package com.nhnacademy.codequestweb.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {
    @Test
    void testStatusEnumValues() {
        // Test values initialization
        assertEquals(0, Status.USED.getCode());
        assertEquals("사용완료", Status.USED.getValue());
        assertEquals(1, Status.AVAILABLE.getCode());
        assertEquals("사용 가능", Status.AVAILABLE.getValue());
        assertEquals(2, Status.UNAVAILABLE.getCode());
        assertEquals("사용 불가", Status.UNAVAILABLE.getValue());
    }
}