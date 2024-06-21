package com.nhnacademy.codequestweb.controller.review;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.codequestweb.exception.review.FileSaveException;
import com.nhnacademy.codequestweb.exception.review.ReviewCreationException;
import com.nhnacademy.codequestweb.exception.review.ReviewPhotoProcessingException;
import com.nhnacademy.codequestweb.exception.review.ReviewUpdateException;
import com.nhnacademy.codequestweb.request.review.PhotoReviewRequestDTO;
import com.nhnacademy.codequestweb.response.review.PhotoReviewResponseDTO;
import com.nhnacademy.codequestweb.service.review.PhotoReviewService;
import jakarta.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequiredArgsConstructor
public class PhotoReviewController {

    private static final int DEFAULT_PAGE_SIZE = 5;
    private final PhotoReviewService photoReviewService;


    @GetMapping("/view/photo-reviews")
    public String getPhotoReviews(Model model, Pageable pageable) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), DEFAULT_PAGE_SIZE, Sort.by(
            Sort.Direction.DESC, "registerDate"));
        ResponseEntity<Page<PhotoReviewResponseDTO>> responseEntity = photoReviewService.getAllReviews(
            pageRequest);
        Page<PhotoReviewResponseDTO> reviews = responseEntity.getBody();
        model.addAttribute("reviews", reviews);
        return "/view/review/photo-reviews";
    }

    @GetMapping("/view/photo-reviews/client/{clientId}")
    public String getPhotoReviewsByClientId(@PathVariable Long clientId, Model model,
        Pageable pageable) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), DEFAULT_PAGE_SIZE, Sort.by(
            Sort.Direction.DESC, "registerDate"));
        ResponseEntity<Page<PhotoReviewResponseDTO>> responseEntity = photoReviewService.getAllReviewsByClientId(
            clientId, pageRequest);
        Page<PhotoReviewResponseDTO> reviews = responseEntity.getBody();
        model.addAttribute("reviews", reviews);
        model.addAttribute("currentPath", "/view/photo-reviews/client/" + clientId);
        return "/view/review/photo-reviews";
    }

    @GetMapping("/view/photo-reviews/product/{productId}")
    public String getPhotoReviewsByProductId(@PathVariable Long productId, Model model,
        Pageable pageable) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), DEFAULT_PAGE_SIZE, Sort.by(
            Sort.Direction.DESC, "registerDate"));
        ResponseEntity<Page<PhotoReviewResponseDTO>> responseEntity = photoReviewService.getAllReviewsByProductId(
            productId, pageRequest);
        Page<PhotoReviewResponseDTO> reviews = responseEntity.getBody();
        model.addAttribute("reviews", reviews);
        model.addAttribute("currentPath", "/view/photo-reviews/product/" + productId);
        return "/view/review/photo-reviews";
    }

    @GetMapping("/view/add-photo-review")
    public String addPhotoReviewForm(Model model) {
        model.addAttribute("review", new PhotoReviewRequestDTO());
        return "/view/review/add-photo-review";
    }

    @PostMapping("/view/add-photo-review")
    public String createReview(
        @Valid
        @ModelAttribute("review") PhotoReviewRequestDTO requestDTO,
        @RequestPart("photoFiles") List<MultipartFile> photoFiles) {

        List<String> photoUrls = photoFiles.stream()
            .map(this::saveFileAndGetUrl)
            .collect(Collectors.toList());

        requestDTO.setPhotoUrls(photoUrls);

        ResponseEntity<PhotoReviewResponseDTO> responseEntity = photoReviewService.createReview(
            requestDTO);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return "redirect:/view/photo-reviews";
        } else {
            throw new ReviewCreationException("리뷰를 생성하는데 실패하였습니다.");
        }
    }

    @GetMapping("/view/edit-photo-review/{id}")
    public String updatePhotoReviewForm(@PathVariable("id") Long id, Model model) {
        ResponseEntity<PhotoReviewResponseDTO> responseEntity = photoReviewService.getReviewById(
            id);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            model.addAttribute("review", responseEntity.getBody());
            return "/view/review/edit-photo-review";
        } else {
            throw new ReviewUpdateException("리뷰를 수정하는데 실패하였습니다.");
        }
    }

    @PostMapping("/view/edit-photo-review/{id}")
    public String updatePhotoReview(
        @PathVariable("id") Long id,
        @Valid @ModelAttribute("review") PhotoReviewRequestDTO requestDTO,
        @RequestPart(value = "photoFiles", required = false) List<MultipartFile> photoFiles,
        @RequestPart("existingPhotoUrls") String existingPhotoUrlsJson) {

        try {
            List<String> existingPhotoUrls = new ObjectMapper().readValue(existingPhotoUrlsJson,
                new TypeReference<List<String>>() {
                });
            List<String> photoUrls = new ArrayList<>();

            if (photoFiles != null && !photoFiles.isEmpty()) {
                photoUrls.addAll(photoFiles.stream()
                    .map(this::saveFileAndGetUrl)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
            }

            if (existingPhotoUrls != null) {
                photoUrls.addAll(0, existingPhotoUrls); // 기존 사진 URL을 맨 앞에 추가
            }

            requestDTO.setPhotoUrls(photoUrls);

            ResponseEntity<PhotoReviewResponseDTO> responseEntity = photoReviewService.updateReview(
                id, requestDTO);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return "redirect:/view/photo-reviews";
            } else {
                throw new ReviewUpdateException("리뷰를 수정하는데 실패하였습니다.");
            }
        } catch (IOException e) {
            throw new ReviewPhotoProcessingException("기존 사진 URL을 처리하는데 실패하였습니다.", e);
        }
    }

    private String saveFileAndGetUrl(MultipartFile file) {
        // 프로젝트 루트 디렉토리 내의 static/uploads 디렉토리에 저장
        String saveDir = new File("src/main/resources/static/uploads").getAbsolutePath() + "/";

        // Ensure the save directory exists
        File directory = new File(saveDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File savedFile = new File(saveDir + filename);
            file.transferTo(savedFile);
            return "/uploads/" + filename;
        } catch (IOException e) {
            throw new FileSaveException("파일 저장 중 오류가 발생했습니다.", e);
        }
    }

}

