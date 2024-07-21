package com.nhnacademy.codequestweb.exception.review;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileSaveExceptionTest {

    @Test
    void testExceptionCreation() {
        String errorMessage = "Failed to save file";
        Throwable cause = new IOException("Disk full");
        FileSaveException exception = new FileSaveException(errorMessage, cause);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testExceptionInheritance() {
        FileSaveException exception = new FileSaveException("Test message", null);

        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testExceptionThrowing() {
        String errorMessage = "Error occurred while saving file";
        Throwable cause = new IllegalArgumentException("Invalid file name");

        Exception exception = assertThrows(FileSaveException.class, () -> {
            throw new FileSaveException(errorMessage, cause);
        });

        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testExceptionWithEmptyMessage() {
        Throwable cause = new NullPointerException();
        FileSaveException exception = new FileSaveException("", cause);

        assertNotNull(exception);
        assertEquals("", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testExceptionWithNullMessage() {
        Throwable cause = new IllegalStateException();
        FileSaveException exception = new FileSaveException(null, cause);

        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testExceptionWithNullCause() {
        String errorMessage = "File save failed";
        FileSaveException exception = new FileSaveException(errorMessage, null);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testExceptionStackTrace() {
        FileSaveException exception = new FileSaveException("Test", new RuntimeException());

        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }
}