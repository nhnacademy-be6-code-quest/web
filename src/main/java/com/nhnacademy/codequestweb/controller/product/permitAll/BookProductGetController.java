package com.nhnacademy.codequestweb.controller.product.permitAll;


import com.nhnacademy.codequestweb.request.product.PageRequestDto;
import com.nhnacademy.codequestweb.request.product.ProductLikeRequestDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.service.product.BookProductService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookProductGetController {

    private final BookProductService bookProductService;

    @GetMapping("/product/books/all")
    public String books(
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name= "size", required = false)Integer size,
            @RequestParam(name = "sort", required = false)String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            Model model) {
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getAllBooks(page, size, sort, desc);
        model.addAttribute("books", response.getBody().getContent());
        for(BookProductGetResponseDto book : response.getBody()) {
            log.info("cover : {}",book.cover());
        }
        log.warn("response: {}", response.getBody().getContent());
        return "/view/product/bookPage";
    }

    @GetMapping("/product/books/{bookId}")
    public String book(@PathVariable long bookId, Model model) {
        ResponseEntity<BookProductGetResponseDto> response = bookProductService.getSingleBookInfo(bookId);
        model.addAttribute("book", response.getBody());
        return "/view/product/singleBookInfo";
    }

    @PostMapping("/product/client/like")
    public String like(HttpServletRequest req, Model model, @ModelAttribute ProductLikeRequestDto productLikeRequestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req,"access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));

        ResponseEntity<Void> response = bookProductService.saveBookLike(headers, productLikeRequestDto);
        return "index";
    }
}
