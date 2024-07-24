package com.nhnacademy.codequestweb.exception.auth;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaycoRedirectExceptionTest {

    @Test
    void testExceptionCreation() {
        String errorMessage = "Payco redirect error occurred";
        PaycoRedirectException exception = new PaycoRedirectException(errorMessage);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testExceptionInheritance() {
        PaycoRedirectException exception = new PaycoRedirectException("Test message");

        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testExceptionThrowing() {
        String errorMessage = "Payco redirect failed";

        Exception exception = assertThrows(PaycoRedirectException.class, () -> {
            throw new PaycoRedirectException(errorMessage);
        });

        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testExceptionWithEmptyMessage() {
        PaycoRedirectException exception = new PaycoRedirectException("");

        assertNotNull(exception);
        assertEquals("", exception.getMessage());
    }

    @Test
    void testExceptionWithNullMessage() {
        PaycoRedirectException exception = new PaycoRedirectException(null);

        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    void testStackTrace() {
        PaycoRedirectException exception = new PaycoRedirectException("Test");

        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }
}