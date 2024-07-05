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

    @GetMapping("/product/books/{productId}")
    public String book(
            HttpServletRequest req,
            @PathVariable long productId,
            Model model) {
        ResponseEntity<BookProductGetResponseDto> response;
//        if (!CookieUtils.isGuest(req)){
            response = bookProductService.getSingleBookInfo(CookieUtils.setHeader(req), productId);
//        }else {
//        }
//        ResponseEntity<BookProductGetResponseDto> response = bookProductService.getSingleBookInfo(CookieUtils.setHeader(req), bookId);
        model.addAttribute("book", response.getBody());
        req.setAttribute("view", "singleBook");
        return "index";
    }

    @GetMapping("/product/books/all")
    public String getAllBookPage(
            HttpServletRequest req,
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name= "size", required = false)Integer size,
            @RequestParam(name = "sort", required = false)String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            Model model) {
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getAllBookPage(CookieUtils.setHeader(req), page, size, sort, desc);
        model.addAttribute("books", response.getBody().getContent());
        req.setAttribute("view", "bookPage");
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
        req.setAttribute("view", "bookPage");
        return "index";
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
        req.setAttribute("view", "bookPage");
        return "index";
    }

    @GetMapping("/product/books/category/{categoryId}")
    public String getBookPageFilterByCategory(
            HttpServletRequest req,
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name= "size", required = false)Integer size,
            @RequestParam(name = "sort", required = false)String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            @PathVariable("categoryId") Long categoryId) {
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getBookPageFilterByCategory(CookieUtils.setHeader(req), page, size, sort, desc, categoryId);
//        req.setAttribute("cateogry", );
        req.setAttribute("books", response.getBody().getContent());
//        model.addAttribute("books", response.getBody().getContent());
//        for(BookProductGetResponseDto book : response.getBody()) {
//            log.info("cover : {}",book.cover());
//        }
        log.warn("response: {}", response.getBody().getContent());
        req.setAttribute("view", "bookPage");
        return "index";
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
        req.setAttribute("view", "bookPage");
        return "index";
    }


    @PostMapping("/product/client/like")
    public String like(HttpServletRequest req, Model model, @ModelAttribute ProductLikeRequestDto productLikeRequestDto) {
        log.info(" header of like:{}", CookieUtils.setHeader(req));

        log.info("like: {}", productLikeRequestDto);
        ResponseEntity<Void> response = bookProductService.saveBookLike(CookieUtils.setHeader(req), productLikeRequestDto);
        return "/view/product/refresh";
    }

    @DeleteMapping("/product/client/unlike")
    public String unlike(HttpServletRequest req, Model model, @RequestParam("productId") long productId) {
        ResponseEntity<Void> response = bookProductService.deleteBookLike(CookieUtils.setHeader(req), productId);
        return "/view/product/refresh";
    }

}
