package com.nhnacademy.codequestweb.service.image;

import com.nhnacademy.codequestweb.client.image.ImageClient;
import com.nhnacademy.codequestweb.exception.review.FileSaveException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageClient imageClient;

    public String uploadImage(MultipartFile file) {
        try {
            return imageClient.uploadImages(file).getBody().getUrl();
        } catch (Exception e) {
            throw new FileSaveException("Failed to upload image", e);
        }
    }
}
