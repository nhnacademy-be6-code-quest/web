package com.nhnacademy.codequestweb.controller.image;

import com.nhnacademy.codequestweb.exception.review.FileSaveException;
import com.nhnacademy.codequestweb.service.image.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ImageControllerTest {

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ImageController imageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveImage() {
        // 준비
        MultipartFile file = new MockMultipartFile(
                "image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes());
        String expectedUrl = "http://example.com/images/test.jpg";
        when(imageService.uploadImage(file)).thenReturn(expectedUrl);

        // 실행
        ResponseEntity<String> response = imageController.saveImage(file);

        // 검증
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUrl, response.getBody());
        verify(imageService, times(1)).uploadImage(file);
    }

    @Test
    void testHandleFileSaveException() {
        // 준비
        FileSaveException exception = new FileSaveException("File save failed", new RuntimeException());

        // 실행
        ResponseEntity<String> response = imageController.handleFileSaveException(exception);

        // 검증
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testSaveImageWithException() {
        // 준비
        MultipartFile file = new MockMultipartFile(
                "image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes());
        when(imageService.uploadImage(file)).thenThrow(new FileSaveException("File save failed", new RuntimeException()));

        // 실행 및 검증
        assertThrows(FileSaveException.class, () -> imageController.saveImage(file));
        verify(imageService, times(1)).uploadImage(file);
    }
}