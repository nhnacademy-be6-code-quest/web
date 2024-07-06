package com.nhnacademy.codequestweb.controller.product.permitAll;


import com.nhnacademy.codequestweb.request.product.ProductLikeRequestDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.service.product.BookProductService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookProductGetController {

    private final BookProductService bookProductService;

    @GetMapping("/product/books/{bookId}")
    public String book(
            HttpServletRequest req,
            @PathVariable long bookId,
            Model model) {
        ResponseEntity<BookProductGetResponseDto> response;
        if (!CookieUtils.isGuest(req)){
            response = bookProductService.getSingleBookInfo(CookieUtils.setHeader(req), bookId);
        }else {
            response = null;
        }
//        ResponseEntity<BookProductGetResponseDto> response = bookProductService.getSingleBookInfo(CookieUtils.setHeader(req), bookId);
        model.addAttribute("book", response.getBody());
        log.info("header of get : {}", CookieUtils.setHeader(req));
        log.info(response.getBody().toString());
        log.info("has like? {}", response.getBody().hasLike());
        return "/view/product/singleBookInfo";
    }

    @GetMapping("/product/books")
    public String getAllBookPage(
            HttpServletRequest req,
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name = "sort", required = false)String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            Model model) {
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getAllBookPage(null, page, 10, sort, desc);

        switch (sort) {
            case "product.productViewCount": model.addAttribute("mainText", "조회순 검색");
            break;
            case "productRegisterDate": model.addAttribute("mainText", "출시순 검색");
            break;
            default: model.addAttribute("mainText", "전체 검색");
        }

        model.addAttribute("sort", sort);
        model.addAttribute("desc", desc);
        model.addAttribute("view", "productList");
        model.addAttribute("productList", response.getBody().getContent());
        model.addAttribute("page", page);
        model.addAttribute("totalPage", response.getBody().getTotalPages());
        return "index";
    }

    @GetMapping("/product/books/containing")
    public String getNameContainingBookPage(
            HttpServletRequest req,
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name= "size", required = false)Integer size,
            @RequestParam(name = "sort", required = false)String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            @RequestParam("title")String title,
            Model model) {
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getNameContainingBookPage(CookieUtils.setHeader(req), page, size, sort, desc, title);
        model.addAttribute("books", response.getBody().getContent());
        return "/view/product/bookPage";
    }

    @GetMapping("/product/books/tagFilter")
    public String getBookPageFilterByTag(
            HttpServletRequest req,
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name= "size", required = false)Integer size,
            @RequestParam(name = "sort", required = false)String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            @RequestParam(name = "tagName") Set<String> tagNameSet,
            @RequestParam(name = "isAnd", required = false)Boolean isAnd,
            Model model) {
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getBookPageFilterByTag(CookieUtils.setHeader(req), page, size, sort, desc, tagNameSet, isAnd);
        model.addAttribute("books", response.getBody().getContent());
        for(BookProductGetResponseDto book : response.getBody()) {
            log.info("cover : {}",book.cover());
        }
        log.warn("response: {}", response.getBody().getContent());
        return "/view/product/bookPage";
    }

    @GetMapping("/product/books/categoryFilter")
    public String getBookPageFilterByCategory(
            HttpServletRequest req,
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name= "size", required = false)Integer size,
            @RequestParam(name = "sort", required = false)String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            @RequestParam(name = "category") String categoryName,
            Model model) {
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getBookPageFilterByCategory(CookieUtils.setHeader(req), page, size, sort, desc, categoryName);
        model.addAttribute("books", response.getBody().getContent());
        for(BookProductGetResponseDto book : response.getBody()) {
            log.info("cover : {}",book.cover());
        }
        log.warn("response: {}", response.getBody().getContent());
        return "/view/product/bookPage";
    }

    @GetMapping("/product/books/like")
    public String getLikeBookPage(
            HttpServletRequest req,
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name= "size", required = false)Integer size,
            @RequestParam(name = "sort", required = false)String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            Model model) {
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getLikeBookPage(CookieUtils.setHeader(req), page, size, sort, desc);
        model.addAttribute("books", response.getBody().getContent());

        log.warn("response: {}", response.getBody().getContent());
        return "/view/product/bookPage";
    }


    @PostMapping("/product/client/like")
    public String like(HttpServletRequest req, Model model, @ModelAttribute ProductLikeRequestDto productLikeRequestDto) {
        log.info(" header of like:{}", CookieUtils.setHeader(req));

        log.info("like: {}", productLikeRequestDto);
        ResponseEntity<Void> response = bookProductService.saveBookLike(CookieUtils.setHeader(req), productLikeRequestDto);
        return "index";
    }

    @DeleteMapping("/product/client/unlike")
    public String unlike(HttpServletRequest req, Model model, @RequestParam("productId") long productId) {
        ResponseEntity<Void> response = bookProductService.deleteBookLike(CookieUtils.setHeader(req), productId);
        return "index";
    }
}
