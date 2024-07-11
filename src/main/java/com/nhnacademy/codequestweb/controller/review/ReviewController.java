package com.nhnacademy.codequestweb.controller.review;

import com.nhnacademy.codequestweb.request.review.WriteReviewRequestDto;
import com.nhnacademy.codequestweb.response.review.WriteReviewResponseDto;
import com.nhnacademy.codequestweb.service.review.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/write/review")
    public String writeReview(HttpServletRequest req) {
        WriteReviewResponseDto info = reviewService.writeReview();
        req.setAttribute("view", "writeReview");
        req.setAttribute("writeReviewRequiredInfo", info);
        return "index";
    }

    @PostMapping("/write/review")
    public String writeReviewPost(@ModelAttribute WriteReviewRequestDto writeReviewRequestDto, HttpServletRequest req) {

        return null;
    }
}
