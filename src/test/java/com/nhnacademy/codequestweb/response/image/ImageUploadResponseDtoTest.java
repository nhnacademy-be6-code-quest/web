package com.nhnacademy.codequestweb.response.image;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ImageUploadResponseDtoTest {

    @Test
    void testNoArgsConstructor() {
        ImageUploadResponseDto dto = new ImageUploadResponseDto();
        assertNotNull(dto);
    }

    @Test
    void testAllArgsConstructor() {
        ImageUploadResponseDto.ImageProperty.Coordinate coordinate = new ImageUploadResponseDto.ImageProperty.Coordinate(37.7749, -122.4194);
        ImageUploadResponseDto.ImageProperty imageProperty = new ImageUploadResponseDto.ImageProperty(800, 600, "2023-07-08T12:00:00Z", coordinate);
        ImageUploadResponseDto.Queue queue = new ImageUploadResponseDto.Queue("q1", "upload", "pending", 1, "2023-07-08T12:10:00Z", "op123", "http://example.com", "image.jpg", "path/to/image.jpg");
        List<ImageUploadResponseDto.Queue> queues = Collections.singletonList(queue);

        ImageUploadResponseDto dto = new ImageUploadResponseDto(true, "id123", "http://example.com/image.jpg", "image.jpg", "path/to/image.jpg", 2048, "user123", "2023-07-08T12:05:00Z", "op123", imageProperty, queues);

        assertEquals(true, dto.isFolder());
        assertEquals("id123", dto.getId());
        assertEquals("http://example.com/image.jpg", dto.getUrl());
        assertEquals("image.jpg", dto.getName());
        assertEquals("path/to/image.jpg", dto.getPath());
        assertEquals(2048, dto.getBytes());
        assertEquals("user123", dto.getCreatedBy());
        assertEquals("2023-07-08T12:05:00Z", dto.getUpdatedAt());
        assertEquals("op123", dto.getOperationId());
        assertEquals(imageProperty, dto.getImageProperty());
        assertEquals(queues, dto.getQueues());
    }

    @Test
    void testGettersAndSetters() {
        ImageUploadResponseDto dto = new ImageUploadResponseDto();
        dto.setFolder(true);
        dto.setId("id123");
        dto.setUrl("http://example.com/image.jpg");
        dto.setName("image.jpg");
        dto.setPath("path/to/image.jpg");
        dto.setBytes(2048);
        dto.setCreatedBy("user123");
        dto.setUpdatedAt("2023-07-08T12:05:00Z");
        dto.setOperationId("op123");

        ImageUploadResponseDto.ImageProperty.Coordinate coordinate = new ImageUploadResponseDto.ImageProperty.Coordinate(37.7749, -122.4194);
        ImageUploadResponseDto.ImageProperty imageProperty = new ImageUploadResponseDto.ImageProperty(800, 600, "2023-07-08T12:00:00Z", coordinate);
        dto.setImageProperty(imageProperty);

        ImageUploadResponseDto.Queue queue = new ImageUploadResponseDto.Queue("q1", "upload", "pending", 1, "2023-07-08T12:10:00Z", "op123", "http://example.com", "image.jpg", "path/to/image.jpg");
        List<ImageUploadResponseDto.Queue> queues = Collections.singletonList(queue);
        dto.setQueues(queues);

        assertEquals(true, dto.isFolder());
        assertEquals("id123", dto.getId());
        assertEquals("http://example.com/image.jpg", dto.getUrl());
        assertEquals("image.jpg", dto.getName());
        assertEquals("path/to/image.jpg", dto.getPath());
        assertEquals(2048, dto.getBytes());
        assertEquals("user123", dto.getCreatedBy());
        assertEquals("2023-07-08T12:05:00Z", dto.getUpdatedAt());
        assertEquals("op123", dto.getOperationId());
        assertEquals(imageProperty, dto.getImageProperty());
        assertEquals(queues, dto.getQueues());
    }

    @Test
    void testImagePropertyGettersAndSetters() {
        ImageUploadResponseDto.ImageProperty imageProperty = new ImageUploadResponseDto.ImageProperty();
        imageProperty.setWidth(800);
        imageProperty.setHeight(600);
        imageProperty.setCreatedAt("2023-07-08T12:00:00Z");

        ImageUploadResponseDto.ImageProperty.Coordinate coordinate = new ImageUploadResponseDto.ImageProperty.Coordinate();
        coordinate.setLat(37.7749);
        coordinate.setLng(-122.4194);
        imageProperty.setCoordinate(coordinate);

        assertEquals(800, imageProperty.getWidth());
        assertEquals(600, imageProperty.getHeight());
        assertEquals("2023-07-08T12:00:00Z", imageProperty.getCreatedAt());
        assertEquals(coordinate, imageProperty.getCoordinate());
    }

    @Test
    void testQueueGettersAndSetters() {
        ImageUploadResponseDto.Queue queue = new ImageUploadResponseDto.Queue();
        queue.setQueueId("q1");
        queue.setQueueType("upload");
        queue.setStatus("pending");
        queue.setTryCount(1);
        queue.setQueuedAt("2023-07-08T12:10:00Z");
        queue.setOperationId("op123");
        queue.setUrl("http://example.com");
        queue.setName("image.jpg");
        queue.setPath("path/to/image.jpg");

        assertEquals("q1", queue.getQueueId());
        assertEquals("upload", queue.getQueueType());
        assertEquals("pending", queue.getStatus());
        assertEquals(1, queue.getTryCount());
        assertEquals("2023-07-08T12:10:00Z", queue.getQueuedAt());
        assertEquals("op123", queue.getOperationId());
        assertEquals("http://example.com", queue.getUrl());
        assertEquals("image.jpg", queue.getName());
        assertEquals("path/to/image.jpg", queue.getPath());
    }
}