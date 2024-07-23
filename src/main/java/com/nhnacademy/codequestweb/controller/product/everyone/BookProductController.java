package com.nhnacademy.codequestweb.controller.product.everyone;


import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.review.ReviewInfoResponseDto;
import com.nhnacademy.codequestweb.service.product.BookProductService;
import com.nhnacademy.codequestweb.service.review.ReviewService;
import com.nhnacademy.codequestweb.utils.BookUtils;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.StringJoiner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookProductController {

    private final ReviewService reviewService;
    private final BookProductService bookProductService;

    private static final String INDEX = "index";

    private static final String MAIN_TEXT = "mainText";

    @GetMapping("/product/books/{productId}")
    public String book(
            HttpServletRequest req,
            @PathVariable("productId") long productId,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        ResponseEntity<BookProductGetResponseDto> response = bookProductService.getSingleBookInfo(CookieUtils.setHeader(req), productId);

        BookUtils.setSingleBookInfo(response, model);

        Double totalReviewScore = reviewService.getReviewScore(productId);
        Page<ReviewInfoResponseDto> reviewPage = reviewService.getProductReviewPage(0, 10, productId);

        model.addAttribute("admin", false);
        model.addAttribute("view", "productBookDetail");


        model.addAttribute("averageScore", totalReviewScore);
        model.addAttribute("page", page);
        model.addAttribute("totalPage", reviewPage.getTotalPages());
        model.addAttribute("reviews", reviewPage.getContent());

        if (CookieUtils.isGuest(req)){
            model.addAttribute("orderURL", "/non-client/order");
        }else{
            model.addAttribute("orderURL", "/client/order");
        }
        return INDEX;
    }


    @GetMapping("/product/books")
    public String getAllBookPage(
            HttpServletRequest req,
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name = "sort", required = false)String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            Model model) {
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getAllBookPage(CookieUtils.setHeader(req), page, 10, sort, desc, 0);

        if(sort != null){
            switch (sort) {
                case "product.productViewCount": model.addAttribute(MAIN_TEXT, "조회순 검색");
                    break;
                case "pubDate": model.addAttribute(MAIN_TEXT, "출시순 검색");
                    break;
                default: model.addAttribute(MAIN_TEXT, "전체 검색");
            }
        }else {
            model.addAttribute(MAIN_TEXT, "전체 검색");
        }

        BookUtils.setBookPage(response, page, sort, desc, model);
        model.addAttribute("url", req.getRequestURI() + "?");
        return INDEX;
    }

    @GetMapping("/product/books/containing")
    public String getNameContainingBookPage(
            HttpServletRequest req,
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name = "sort", required = false)String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            @RequestParam("title")String title,
            Model model) {
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getNameContainingBookPage(CookieUtils.setHeader(req), page, 10, sort, desc, title, 0);
        BookUtils.setBookPage(response, page, sort, desc, model);
        model.addAttribute(MAIN_TEXT, "제목 검색");
        model.addAttribute("url", req.getRequestURI() + "?title=" + title + "&");
        return INDEX;
    }

    @GetMapping("/product/books/tagFilter")
    public String getBookPageFilterByTag(
            HttpServletRequest req,
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name = "sort", required = false)String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            @RequestParam(name = "tagName") Set<String> tagNameSet,
            @RequestParam(name = "isAnd", required = false)Boolean isAnd,
            Model model) {
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getBookPageFilterByTag(CookieUtils.setHeader(req), page, 10, sort, desc, tagNameSet, isAnd, 0);
        BookUtils.setBookPage(response, page, sort, desc, model);
        model.addAttribute(MAIN_TEXT, "태그 검색 - " + tagNameSet);
        StringJoiner stringJoiner = new StringJoiner(",");
        for (String tagName : tagNameSet) {
            stringJoiner.add(tagName);
        }
        model.addAttribute("url", req.getRequestURI()+ "?tagName=" + stringJoiner.toString() + "&");
        return INDEX;
    }

    @GetMapping("/product/books/category/{categoryId}")
    public String getBookPageFilterByCategory(
            HttpServletRequest req,
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name = "sort", required = false)String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            @PathVariable("categoryId") Long categoryId,
            Model model) {
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getBookPageFilterByCategory(CookieUtils.setHeader(req), page, 10, sort, desc, categoryId, 0);
        BookUtils.setBookPage(response, page, sort, desc, model);
        model.addAttribute(MAIN_TEXT, "카테고리 검색");
        model.addAttribute("url", req.getRequestURI() + "?");
        return INDEX;
    }

    @GetMapping("/product/books/like")
    public String getLikeBookPage(
            HttpServletRequest req,
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name = "sort", required = false)String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            Model model) {
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getLikeBookPage(CookieUtils.setHeader(req), page, 10, sort, desc);
        BookUtils.setBookPage(response, page, sort, desc, model);
        model.addAttribute(MAIN_TEXT, "좋아요 목록");
        model.addAttribute("url", req.getRequestURI() + "?");
        return INDEX;
    }
}
