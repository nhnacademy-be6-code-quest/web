package com.nhnacademy.codequestweb.controller.review;


import com.nhnacademy.codequestweb.exception.review.ReviewCreationException;
import com.nhnacademy.codequestweb.exception.review.ReviewUpdateException;
import com.nhnacademy.codequestweb.request.review.NoPhotoReviewRequestDTO;
import com.nhnacademy.codequestweb.response.review.NoPhotoReviewResponseDTO;
import com.nhnacademy.codequestweb.service.order.OrderService;
import com.nhnacademy.codequestweb.service.review.NoPhotoReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
public class NoPhotoReviewController {

    private static final int DEFAULT_PAGE_SIZE = 3;
    private final NoPhotoReviewService noPhotoReviewService;
    private final OrderService orderService;


    @GetMapping("/view/no-photo-reviews")
    public String getNoPhotoReviews(Model model, Pageable pageable) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), DEFAULT_PAGE_SIZE, Sort.by(
            Sort.Direction.DESC, "registerDate"));
        ResponseEntity<Page<NoPhotoReviewResponseDTO>> responseEntity = noPhotoReviewService.getAllReviews(
            pageRequest);
        Page<NoPhotoReviewResponseDTO> reviews = responseEntity.getBody();
        model.addAttribute("reviews", reviews);
        model.addAttribute("currentPath", "/view/no-photo-reviews");
        return "/view/review/no-photo-reviews";
    }

    @GetMapping("/view/no-photo-reviews/client/{clientId}")
    public String getNoPhotoReviewsByClientId(@PathVariable Long clientId, Model model,
        Pageable pageable) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), DEFAULT_PAGE_SIZE, Sort.by(
            Sort.Direction.DESC, "registerDate"));
        ResponseEntity<Page<NoPhotoReviewResponseDTO>> responseEntity = noPhotoReviewService.getAllReviewsByClientId(
            clientId, pageRequest);
        Page<NoPhotoReviewResponseDTO> reviews = responseEntity.getBody();
        model.addAttribute("reviews", reviews);
        model.addAttribute("currentPath", "/view/no-photo-reviews/client/" + clientId);
        return "/view/review/no-photo-reviews";
    }


    @GetMapping("/view/no-photo-reviews/product/{productId}")
    public String getNoPhotoReviewsByProductId(@PathVariable Long productId, Model model,
        Pageable pageable) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), DEFAULT_PAGE_SIZE, Sort.by(
            Sort.Direction.DESC, "registerDate"));
        ResponseEntity<Page<NoPhotoReviewResponseDTO>> responseEntity = noPhotoReviewService.getAllReviewsByProductId(
            productId, pageRequest);
        Page<NoPhotoReviewResponseDTO> reviews = responseEntity.getBody();
        model.addAttribute("reviews", reviews);
        model.addAttribute("currentPath", "/view/no-photo-reviews/product/" + productId);
        return "/view/review/no-photo-reviews";
    }

    @GetMapping("/view/add-no-photo-review")
    public String addNoPhotoReviewForm(Model model) {
        model.addAttribute("review", new NoPhotoReviewRequestDTO());
        return "/view/review/add-no-photo-review";
    }

    @GetMapping("/view/add-no-photo-review/{orderDetailId}")
    public String addNoPhotoReviewFormOrderDetailId(Model model, @PathVariable Long orderDetailId) {
        String status = orderService.getOrderStatus(orderDetailId).getBody();

        if (Boolean.FALSE.equals(noPhotoReviewService.hasWrittenReview(orderDetailId).getBody())
            && status.equals("DELIVERY_COMPLETE")) {
            model.addAttribute("review", new NoPhotoReviewRequestDTO());
            return "/view/review/add-no-photo-review";
        } else {
            if (status.equals("DELIVERY_COMPLETE")) {
                log.error("리뷰가 이미 작성됨");
            } else {
                log.error("status : " + status);
            }
            return "redirect:/index2";
        }

    }

    @PostMapping("/view/add-no-photo-review")
    public String createNoPhotoReview(
        @Validated @ModelAttribute("review") NoPhotoReviewRequestDTO requestDTO) {
        ResponseEntity<NoPhotoReviewResponseDTO> responseEntity = noPhotoReviewService.createReview(
            requestDTO);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return "redirect:/view/no-photo-reviews";
        } else {
            throw new ReviewCreationException("리뷰를 생성하는데 실패하였습니다.");
        }
    }

    @GetMapping("/view/edit-no-photo-review/{id}")
    public String updateNoPhotoReviewForm(@PathVariable("id") Long id, Model model) {
        ResponseEntity<NoPhotoReviewResponseDTO> responseEntity = noPhotoReviewService.getReviewById(
            id);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("review", responseEntity.getBody());
            return "/view/review/edit-no-photo-review";
        } else {
            throw new ReviewUpdateException("리뷰를 수정하는데 실패하였습니다.");
        }
    }

    @PostMapping("/view/edit-no-photo-review/{id}")
    public String updateNoPhotoReview(
        @PathVariable("id") Long id,
        @Valid @ModelAttribute("review") NoPhotoReviewRequestDTO requestDTO) {

        ResponseEntity<NoPhotoReviewResponseDTO> responseEntity = noPhotoReviewService.updateReview(
            id, requestDTO);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return "redirect:/view/no-photo-reviews";
        } else {
            throw new ReviewUpdateException("리뷰를 수정하는데 실패하였습니다.");
        }
    }

}
