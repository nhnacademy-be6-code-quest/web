package com.nhnacademy.codequestweb.client.image;

import com.nhnacademy.codequestweb.response.image.ImageUploadResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "review", url = "http://localhost:8001")
public interface ImageClient {
    @PostMapping(path = "/api/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ImageUploadResponseDto> uploadImages(@RequestPart("file") MultipartFile file);
}
