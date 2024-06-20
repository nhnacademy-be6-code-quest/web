package com.nhnacademy.codequestweb.controller.review;


import com.nhnacademy.codequestweb.client.review.NoPhotoReviewClient;
import com.nhnacademy.codequestweb.client.review.PhotoReviewClient;
import com.nhnacademy.codequestweb.request.review.NoPhotoReviewRequestDTO;
import com.nhnacademy.codequestweb.request.review.PhotoReviewRequestDTO;
import com.nhnacademy.codequestweb.response.review.NoPhotoReviewResponseDTO;
import com.nhnacademy.codequestweb.response.review.PhotoReviewResponseDTO;
import jakarta.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final PhotoReviewClient photoReviewClient;
    private final NoPhotoReviewClient noPhotoReviewClient;

    @GetMapping("/index2")
    public String home() {
        return "/view/review/index2";
    }


    @GetMapping("/view/photo-reviews")
    public String getPhotoReviews(Model model) {
        ResponseEntity<List<PhotoReviewResponseDTO>> responseEntity = photoReviewClient.getAllReviews();
        List<PhotoReviewResponseDTO> reviews = responseEntity.getBody();
        model.addAttribute("reviews", reviews);
        return "/view/review/photo-reviews";
    }

    @GetMapping("/view/add-photo-review")
    public String addPhotoReviewForm(Model model) {
        model.addAttribute("review", new PhotoReviewRequestDTO());
        return "/view/review/add-photo-review";
    }

    @PostMapping("/view/add-photo-review")
    public String createReview(
        @Valid @ModelAttribute("review") PhotoReviewRequestDTO requestDTO,
        @RequestPart("photoFiles") List<MultipartFile> photoFiles) {

        List<String> photoUrls = photoFiles.stream()
            .map(this::saveFileAndGetUrl)
            .collect(Collectors.toList());

        requestDTO.setPhotoUrls(photoUrls);

        ResponseEntity<PhotoReviewResponseDTO> responseEntity = photoReviewClient.createReview(
            requestDTO);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return "redirect:/view/photo-reviews";
        } else {
            throw new RuntimeException("리뷰를 생성하는데 실패하였습니다.");
        }
    }

    @GetMapping("/view/edit-photo-review/{id}")
    public String updatePhotoReviewForm(@PathVariable("id") Long id, Model model) {
        ResponseEntity<PhotoReviewResponseDTO> responseEntity = photoReviewClient.getReviewById(id);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            model.addAttribute("review", responseEntity.getBody());
            return "/view/review/edit-photo-review";
        } else {
            throw new RuntimeException("리뷰를 수정하는데 실패하였습니다.");
        }
    }

    @PostMapping("/view/edit-photo-review/{id}")
    public String updatePhotoReview(
        @PathVariable("id") Long id,
        @Valid @ModelAttribute("review") PhotoReviewRequestDTO requestDTO,
        @RequestPart("photoFiles") List<MultipartFile> photoFiles) {

        List<String> photoUrls = photoFiles.stream()
            .map(this::saveFileAndGetUrl)
            .collect(Collectors.toList());

        requestDTO.setPhotoUrls(photoUrls);

        ResponseEntity<PhotoReviewResponseDTO> responseEntity = photoReviewClient.updateReview(id,
            requestDTO);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return "redirect:/view/photo-reviews";
        } else {
            throw new RuntimeException("리뷰를 수정하는데 실패하였습니다.");
        }
    }

    // 여기서부터 사진없는 리뷰

    @GetMapping("/view/no-photo-reviews")
    public String getNoPhotoReviews(Model model) {
        ResponseEntity<List<NoPhotoReviewResponseDTO>> responseEntity = noPhotoReviewClient.getAllReviews();
        List<NoPhotoReviewResponseDTO> reviews = responseEntity.getBody();
        model.addAttribute("reviews", reviews);
        return "/view/review/no-photo-reviews";
    }

    @GetMapping("/view/add-no-photo-review")
    public String addNoPhotoReviewForm(Model model) {
        model.addAttribute("review", new NoPhotoReviewRequestDTO());
        return "/view/review/add-no-photo-review";
    }

    @PostMapping("/view/add-no-photo-review")
    public String createNoPhotoReview(
        @Validated @ModelAttribute("review") NoPhotoReviewRequestDTO requestDTO) {
        ResponseEntity<NoPhotoReviewResponseDTO> responseEntity = noPhotoReviewClient.createReview(
            requestDTO);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return "redirect:/view/no-photo-reviews";
        } else {
            throw new RuntimeException("리뷰를 생성하는데 실패하였습니다.");
        }
    }

    @GetMapping("/view/edit-no-photo-review/{id}")
    public String updateNoPhotoReviewForm(@PathVariable("id") Long id, Model model) {
        ResponseEntity<NoPhotoReviewResponseDTO> responseEntity = noPhotoReviewClient.getReviewById(
            id);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("review", responseEntity.getBody());
            return "/view/review/edit-no-photo-review";
        } else {
            throw new RuntimeException("리뷰를 수정하는데 실패하였습니다.");
        }
    }

    @PostMapping("/view/edit-no-photo-review/{id}")
    public String updateNoPhotoReview(
        @PathVariable("id") Long id,
        @Valid @ModelAttribute("review") NoPhotoReviewRequestDTO requestDTO) {

        ResponseEntity<NoPhotoReviewResponseDTO> responseEntity = noPhotoReviewClient.updateReview(
            id, requestDTO);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return "redirect:/view/no-photo-reviews";
        } else {
            throw new RuntimeException("리뷰를 수정하는데 실패하였습니다.");
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
            throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
        }
    }

}

