package com.nhnacademy.codequestweb.service.image;

import com.nhnacademy.codequestweb.client.image.ImageClient;
import com.nhnacademy.codequestweb.exception.review.FileSaveException;
import com.nhnacademy.codequestweb.response.image.ImageUploadResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ImageServiceTest {

    @Mock
    private ImageClient imageClient;

    @InjectMocks
    private ImageService imageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadImageSuccess() {
        // 준비
        MultipartFile file = new MockMultipartFile("test.jpg", "test image content".getBytes());
        ImageUploadResponseDto responseDto = new ImageUploadResponseDto();
        responseDto.setUrl("http://example.com/images/test.jpg");

        when(imageClient.uploadImages(file)).thenReturn(ResponseEntity.ok(responseDto));

        // 실행
        String result = imageService.uploadImage(file);

        // 검증
        assertEquals("http://example.com/images/test.jpg", result);
        verify(imageClient, times(1)).uploadImages(file);
    }

    @Test
    void testUploadImageFailure() {
        // 준비
        MultipartFile file = new MockMultipartFile("test.jpg", "test image content".getBytes());
        when(imageClient.uploadImages(file)).thenThrow(new RuntimeException("Upload failed"));

        // 실행 및 검증
        assertThrows(FileSaveException.class, () -> imageService.uploadImage(file));
        verify(imageClient, times(1)).uploadImages(file);
    }
}