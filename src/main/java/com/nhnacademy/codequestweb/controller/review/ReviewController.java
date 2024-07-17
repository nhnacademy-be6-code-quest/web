package com.nhnacademy.codequestweb.controller.review;

import com.nhnacademy.codequestweb.request.review.ReviewUpdateRequestDto;
import com.nhnacademy.codequestweb.request.review.WriteReviewRequestDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.review.ReviewInfoResponseDto;
import com.nhnacademy.codequestweb.response.review.WriteReviewResponseDto;
import com.nhnacademy.codequestweb.service.review.ReviewService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/write/review")
    public String writeReview(
            @RequestParam("orderId") Long orderId,
            @RequestParam("orderDetailId") Long orderDetailId,
            HttpServletRequest req,
            RedirectAttributes redirectAttributes
    ) {
        if (reviewService.isWrited(CookieUtils.getCookieValue(req, "access"), orderDetailId)) {
            redirectAttributes.addFlashAttribute("alterMessage", "이미 작성된 리뷰입니다.");
            String referer = req.getHeader("Referer");
            return "redirect:" + (referer != null ? referer : "/");
        }
        WriteReviewResponseDto info = reviewService.writeReview(orderId, orderDetailId, CookieUtils.getCookieValue(req, "access"));
        req.setAttribute("view", "writeReview");
        req.setAttribute("writeReviewRequiredInfo", info);
        return "index";
    }

    @PostMapping("/write/review")
    public ResponseEntity<Map<String, String>> writeReviewPost(@ModelAttribute WriteReviewRequestDto writeReviewRequestDto, HttpServletRequest req) {
        log.info("WriteReviewRequestDto: {}", writeReviewRequestDto);
        String message = reviewService.posttingReview(writeReviewRequestDto, CookieUtils.getCookieValue(req, "access"));
        String redirectUrl = "/product/books/" + writeReviewRequestDto.getProductId();

        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        response.put("redirectUrl", redirectUrl);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/review/detail")
    public String reviewDetail(@RequestParam Long reviewId, HttpServletRequest req) {
        log.info("reviewId: {}", reviewId);
        ReviewInfoResponseDto reviewInfo = reviewService.getReviewInfo(CookieUtils.getCookieValue(req, "access"), reviewId);
        BookProductGetResponseDto productInfo = reviewService.getProductInfo(reviewInfo.getProductId());
        req.setAttribute("view", "reviewDetail");
        req.setAttribute("review", reviewInfo);
        req.setAttribute("product", productInfo);
        return "index";
    }

    @PutMapping("/review/update")
    public ResponseEntity<String> updateReview(@ModelAttribute ReviewUpdateRequestDto reviewUpdateRequestDto, HttpServletRequest req) {
        log.info("reviewUpdateRequestDto: {}", reviewUpdateRequestDto);
        return ResponseEntity.ok(reviewService.updateReview(reviewUpdateRequestDto, CookieUtils.getCookieValue(req, "access")));
    }

    @ExceptionHandler(FeignException.BadRequest.class)
    public String handleBadRequest(HttpServletRequest req) {
        return "redirect:/";
    }
}
