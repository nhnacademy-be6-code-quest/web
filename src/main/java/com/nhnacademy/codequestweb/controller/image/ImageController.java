package com.nhnacademy.codequestweb.controller.image;

import com.nhnacademy.codequestweb.exception.review.FileSaveException;
import com.nhnacademy.codequestweb.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/save/img")
    public ResponseEntity<String> saveImage(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(imageService.uploadImage(file));
    }

    @ExceptionHandler(FileSaveException.class)
    public ResponseEntity<String> handleFileSaveException(FileSaveException e) {
        return ResponseEntity.internalServerError().build();
    }
}
