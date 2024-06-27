package com.nhnacademy.codequestweb.controller.review;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.codequestweb.exception.review.FileSaveException;
import com.nhnacademy.codequestweb.exception.review.ReviewCreationException;
import com.nhnacademy.codequestweb.exception.review.ReviewPhotoProcessingException;
import com.nhnacademy.codequestweb.exception.review.ReviewUpdateException;
import com.nhnacademy.codequestweb.request.review.PhotoReviewRequestDTO;
import com.nhnacademy.codequestweb.response.review.PhotoReviewResponseDTO;
import com.nhnacademy.codequestweb.service.order.OrderService;
import com.nhnacademy.codequestweb.service.review.PhotoReviewService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Controller
@RequiredArgsConstructor
public class PhotoReviewController {

    private static final int DEFAULT_PAGE_SIZE = 3;
    private final PhotoReviewService photoReviewService;
    private final OrderService orderService;

    /*
    @GetMapping("/mypage/reviews/photo")
    public String getPhotoReviews(HttpServletRequest req, Model model, Pageable pageable) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));

     */

    @GetMapping("/view/photo-reviews")
    public String getPhotoReviews(HttpServletRequest req, Model model, Pageable pageable) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));

        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), DEFAULT_PAGE_SIZE, Sort.by(
            Sort.Direction.DESC, "registerDate"));
        ResponseEntity<Page<PhotoReviewResponseDTO>> responseEntity = photoReviewService.getAllReviews(headers,
            pageRequest);
        Page<PhotoReviewResponseDTO> reviews = responseEntity.getBody();
        model.addAttribute("reviews", reviews);
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

    @GetMapping("/view/add-photo-review/{orderDetailId}")
    public String addPhotoReviewFormOrderDetailId (HttpServletRequest req, Model model, @PathVariable Long orderDetailId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));

        String status = orderService.getOrderStatus(orderDetailId).getBody();

        if(Boolean.FALSE.equals(photoReviewService.hasWrittenReview(headers, orderDetailId).getBody()) && status.equals("DELIVERY_COMPLETE")){
            model.addAttribute("review", new PhotoReviewRequestDTO());
            return "/view/review/add-photo-review";
        } else {
            if (status.equals("DELIVERY_COMPLETE")) {
                log.error("리뷰가 이미 작성됨");
            } else {
                log.error("status : " + status);
            }

            return "redirect:/index2";
        }
    }

    @PostMapping("/view/add-photo-review")
    public String createReview(HttpServletRequest req,
        @Valid
        @ModelAttribute("review") PhotoReviewRequestDTO requestDTO,
        @RequestPart("photoFiles") List<MultipartFile> photoFiles) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));

        List<String> photoUrls = photoFiles.stream()
            .map(this::saveFileAndGetUrl)
            .collect(Collectors.toList());

        requestDTO.setPhotoUrls(photoUrls);

        ResponseEntity<PhotoReviewResponseDTO> responseEntity = photoReviewService.createReview(headers,
            requestDTO);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return "redirect:/view/photo-reviews";
        } else {
            throw new ReviewCreationException("리뷰를 생성하는데 실패하였습니다.");
        }
    }

    @GetMapping("/view/edit-photo-review/{id}")
    public String updatePhotoReviewForm(HttpServletRequest req, @PathVariable("id") Long id, Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));

        ResponseEntity<PhotoReviewResponseDTO> responseEntity = photoReviewService.getReviewById(headers,
            id);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            model.addAttribute("review", responseEntity.getBody());
            return "/view/review/edit-photo-review";
        } else {
            throw new ReviewUpdateException("리뷰를 수정하는데 실패하였습니다.");
        }
    }

    @PostMapping("/view/edit-photo-review/{id}")
    public String updatePhotoReview(HttpServletRequest req,
        @PathVariable("id") Long id,
        @Valid @ModelAttribute("review") PhotoReviewRequestDTO requestDTO,
        @RequestPart(value = "photoFiles", required = false) List<MultipartFile> photoFiles,
        @RequestPart("existingPhotoUrls") String existingPhotoUrlsJson) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));

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

            ResponseEntity<PhotoReviewResponseDTO> responseEntity = photoReviewService.updateReview(headers,
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

