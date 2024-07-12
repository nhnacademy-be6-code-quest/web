package com.nhnacademy.codequestweb.controller.review;

import com.nhnacademy.codequestweb.request.review.WriteReviewRequestDto;
import com.nhnacademy.codequestweb.response.review.WriteReviewResponseDto;
import com.nhnacademy.codequestweb.service.review.ReviewService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
            HttpServletRequest req
    ) {
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
}
