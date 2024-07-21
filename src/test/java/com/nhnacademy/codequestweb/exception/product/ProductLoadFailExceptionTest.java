package com.nhnacademy.codequestweb.exception.product;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductLoadFailExceptionTest {

    @Test
    void testExceptionCreation() {
        String errorMessage = "Failed to load product";
        ProductLoadFailException exception = new ProductLoadFailException(errorMessage);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testExceptionInheritance() {
        ProductLoadFailException exception = new ProductLoadFailException("Test message");

        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testExceptionThrowing() {
        String errorMessage = "Error occurred while loading product";

        Exception exception = assertThrows(ProductLoadFailException.class, () -> {
            throw new ProductLoadFailException(errorMessage);
        });

        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testExceptionWithEmptyMessage() {
        ProductLoadFailException exception = new ProductLoadFailException("");

        assertNotNull(exception);
        assertEquals("", exception.getMessage());
    }

    @Test
    void testExceptionWithNullMessage() {
        ProductLoadFailException exception = new ProductLoadFailException(null);

        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    void testExceptionStackTrace() {
        ProductLoadFailException exception = new ProductLoadFailException("Test");

        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }
}