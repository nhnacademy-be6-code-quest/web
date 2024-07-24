package com.nhnacademy.codequestweb.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderStatusTest {

    @Test
    void testEnumValues() {
        assertEquals(6, OrderStatus.values().length);
        assertEquals(OrderStatus.WAIT_PAYMENT, OrderStatus.valueOf("WAIT_PAYMENT"));
        assertEquals(OrderStatus.PAYED, OrderStatus.valueOf("PAYED"));
        assertEquals(OrderStatus.DELIVERING, OrderStatus.valueOf("DELIVERING"));
        assertEquals(OrderStatus.DELIVERY_COMPLETE, OrderStatus.valueOf("DELIVERY_COMPLETE"));
        assertEquals(OrderStatus.REFUND, OrderStatus.valueOf("REFUND"));
        assertEquals(OrderStatus.CANCEL, OrderStatus.valueOf("CANCEL"));
    }

    @Test
    void testEnumOrder() {
        OrderStatus[] statuses = OrderStatus.values();
        assertEquals(OrderStatus.WAIT_PAYMENT, statuses[0]);
        assertEquals(OrderStatus.PAYED, statuses[1]);
        assertEquals(OrderStatus.DELIVERING, statuses[2]);
        assertEquals(OrderStatus.DELIVERY_COMPLETE, statuses[3]);
        assertEquals(OrderStatus.REFUND, statuses[4]);
        assertEquals(OrderStatus.CANCEL, statuses[5]);
    }

    @Test
    void testOrdinal() {
        assertEquals(0, OrderStatus.WAIT_PAYMENT.ordinal());
        assertEquals(1, OrderStatus.PAYED.ordinal());
        assertEquals(2, OrderStatus.DELIVERING.ordinal());
        assertEquals(3, OrderStatus.DELIVERY_COMPLETE.ordinal());
        assertEquals(4, OrderStatus.REFUND.ordinal());
        assertEquals(5, OrderStatus.CANCEL.ordinal());
    }

    @Test
    void testName() {
        assertEquals("WAIT_PAYMENT", OrderStatus.WAIT_PAYMENT.name());
        assertEquals("PAYED", OrderStatus.PAYED.name());
        assertEquals("DELIVERING", OrderStatus.DELIVERING.name());
        assertEquals("DELIVERY_COMPLETE", OrderStatus.DELIVERY_COMPLETE.name());
        assertEquals("REFUND", OrderStatus.REFUND.name());
        assertEquals("CANCEL", OrderStatus.CANCEL.name());
    }

    @Test
    void testInvalidEnumValue() {
        assertThrows(IllegalArgumentException.class, () -> OrderStatus.valueOf("INVALID_STATUS"));
    }

    @Test
    void testCompareTo() {
        assertTrue(OrderStatus.WAIT_PAYMENT.compareTo(OrderStatus.PAYED) < 0);
        assertTrue(OrderStatus.PAYED.compareTo(OrderStatus.DELIVERING) < 0);
        assertTrue(OrderStatus.DELIVERING.compareTo(OrderStatus.DELIVERY_COMPLETE) < 0);
        assertTrue(OrderStatus.DELIVERY_COMPLETE.compareTo(OrderStatus.REFUND) < 0);
        assertTrue(OrderStatus.REFUND.compareTo(OrderStatus.CANCEL) < 0);
    }
}