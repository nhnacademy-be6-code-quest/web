package com.nhnacademy.codequestweb.controller.review;

import com.nhnacademy.codequestweb.request.review.WriteReviewRequestDto;
import com.nhnacademy.codequestweb.response.review.WriteReviewResponseDto;
import com.nhnacademy.codequestweb.service.review.ReviewService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/write/review/{orderDetail}")
    public String writeReview(@PathVariable Long orderDetailId, HttpServletRequest req) {
        WriteReviewResponseDto info = reviewService.writeReview(orderDetailId, CookieUtils.getCookieValue(req, "access"));
        req.setAttribute("view", "writeReview");
        req.setAttribute("writeReviewRequiredInfo", info);
        return "index";
    }

    @PostMapping("/write/review")
    public String writeReviewPost(@ModelAttribute WriteReviewRequestDto writeReviewRequestDto, HttpServletRequest req) {

        return null;
    }
}
