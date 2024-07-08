package com.nhnacademy.codequestweb.service.image;

import com.nhnacademy.codequestweb.client.image.ImageClient;
import com.nhnacademy.codequestweb.exception.review.FileSaveException;
import com.nhnacademy.codequestweb.response.image.ImageUploadResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageClient imageClient;

    public String uploadImage(MultipartFile file) {
        try {
            ImageUploadResponseDto res = imageClient.uploadImages(file).getBody();
            log.info("Image uploaded successfully : {}", res);
            return res.getUrl();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new FileSaveException("Failed to upload image", e);
        }
    }
}
