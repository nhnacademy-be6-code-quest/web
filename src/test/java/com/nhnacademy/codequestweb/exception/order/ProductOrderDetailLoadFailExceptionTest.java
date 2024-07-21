package com.nhnacademy.codequestweb.exception.order;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductOrderDetailLoadFailExceptionTest {

    @Test
    void testExceptionCreation() {
        String errorMessage = "Failed to load product order detail";
        ProductOrderDetailLoadFailException exception = new ProductOrderDetailLoadFailException(errorMessage);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testExceptionInheritance() {
        ProductOrderDetailLoadFailException exception = new ProductOrderDetailLoadFailException("Test message");

        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testExceptionThrowing() {
        String errorMessage = "Error occurred while loading order detail";

        Exception exception = assertThrows(ProductOrderDetailLoadFailException.class, () -> {
            throw new ProductOrderDetailLoadFailException(errorMessage);
        });

        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testExceptionWithEmptyMessage() {
        ProductOrderDetailLoadFailException exception = new ProductOrderDetailLoadFailException("");

        assertNotNull(exception);
        assertEquals("", exception.getMessage());
    }

    @Test
    void testExceptionWithNullMessage() {
        ProductOrderDetailLoadFailException exception = new ProductOrderDetailLoadFailException(null);

        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    void testExceptionStackTrace() {
        ProductOrderDetailLoadFailException exception = new ProductOrderDetailLoadFailException("Test");

        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }
}