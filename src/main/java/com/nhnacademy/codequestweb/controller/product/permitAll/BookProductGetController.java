package com.nhnacademy.codequestweb.controller.product.permitAll;


import com.nhnacademy.codequestweb.request.product.ProductLikeRequestDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.ProductCategory;
import com.nhnacademy.codequestweb.service.product.BookProductService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
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

//    @GetMapping("/product/books/{productId}")
//    public String book(
//            HttpServletRequest req,
//            @PathVariable long productId,
//            Model model) {
//        ResponseEntity<BookProductGetResponseDto> response = bookProductService.getSingleBookInfo(CookieUtils.setHeader(req), productId);
//        BookProductGetResponseDto bookProductGetResponseDto = response.getBody();
//        Set<ProductCategory> categorySet = bookProductGetResponseDto.categorySet();
//        List<List<ProductCategory>> allCategoryList = new ArrayList<>();
//        for (ProductCategory category : categorySet) {
//            List<ProductCategory> parentCategoryList = new ArrayList<>();
//            parentCategoryList.add(category);
//            ProductCategory parent = category.parentProductCategory();
//            while(parent != null) {
//                parentCategoryList.add(parent);
//                parent = parent.parentProductCategory();
//            }
//            parentCategoryList.sort(Comparator.comparing(ProductCategory::productCategoryId));
//            allCategoryList.add(parentCategoryList);
//        }
//        model.addAttribute("listOfCategoryList", allCategoryList);
//        model.addAttribute("book", bookProductGetResponseDto);
//        req.setAttribute("view", "singleBook");
//        return "index";
//    }

    @GetMapping("/product/books/{bookId}")
    public String book(
            HttpServletRequest req,
            @PathVariable long bookId,
            Model model) {
        ResponseEntity<BookProductGetResponseDto> response = bookProductService.getSingleBookInfo(CookieUtils.setHeader(req), bookId);
        BookProductGetResponseDto bookProductGetResponseDto = response.getBody();
        Set<ProductCategory> categorySet = bookProductGetResponseDto.categorySet();
        List<List<ProductCategory>> allCategoryList = new ArrayList<>();
        for (ProductCategory category : categorySet) {
            List<ProductCategory> parentCategoryList = new ArrayList<>();
            parentCategoryList.add(category);
            ProductCategory parent = category.parentProductCategory();
            while(parent != null) {
                parentCategoryList.add(parent);
                parent = parent.parentProductCategory();
            }
            parentCategoryList.sort(Comparator.comparing(ProductCategory::productCategoryId));
            allCategoryList.add(parentCategoryList);
        }
        model.addAttribute("view", "productBookDetail");
        model.addAttribute("book", bookProductGetResponseDto);
        model.addAttribute("listOfCategoryList", allCategoryList);
        log.info("header of get : {}", CookieUtils.setHeader(req));
        log.info(response.getBody().toString());
        log.info("has like? {}", response.getBody().hasLike());
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
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getAllBookPage(CookieUtils.setHeader(req), page, size, sort, desc, 0);
        model.addAttribute("books", response.getBody().getContent());
        req.setAttribute("view", "bookPage");
        return "index";
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
                case "product.productViewCount": model.addAttribute("mainText", "조회순 검색");
                    break;
                case "pubDate": model.addAttribute("mainText", "출시순 검색");
                    break;
                default: model.addAttribute("mainText", "전체 검색");
            }
        }else {
            model.addAttribute("mainText", "전체 검색");
        }


        model.addAttribute("sort", sort);
        model.addAttribute("desc", desc);
        model.addAttribute("view", "productList");
        model.addAttribute("productList", response.getBody().getContent());
        page = page == null ? 1 : page;
        model.addAttribute("page", page);
        model.addAttribute("totalPage", response.getBody().getTotalPages());
        model.addAttribute("url", req.getRequestURI());
        return "index";
    }

    @GetMapping("/product/books/containing")
    public String getNameContainingBookPage(
            HttpServletRequest req,
            @RequestParam(name = "page", required = false)Integer page,
//            @RequestParam(name= "size", required = false)Integer size,
            @RequestParam(name = "sort", required = false)String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            @RequestParam("title")String title,
            Model model) {
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getNameContainingBookPage(CookieUtils.setHeader(req), page, 10, sort, desc, title, 0);
        model.addAttribute("sort", sort);
        model.addAttribute("desc", desc);
        model.addAttribute("view", "productList");
        model.addAttribute("productList", response.getBody().getContent());
        page = page == null ? 1 : page;
        model.addAttribute("page", page);
        model.addAttribute("totalPage", response.getBody().getTotalPages());
        log.info("total : {}", response.getBody().getTotalPages());
        model.addAttribute("mainText", "제목 검색");
        model.addAttribute("url", req.getRequestURI() + "?title=" + title);
        return "index";
    }

    @GetMapping("/product/books/tagFilter")
    public String getBookPageFilterByTag(
            HttpServletRequest req,
            @RequestParam(name = "page", required = false)Integer page,
//            @RequestParam(name= "size", required = false)Integer size,
            @RequestParam(name = "sort", required = false)String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            @RequestParam(name = "tagName") Set<String> tagNameSet,
            @RequestParam(name = "isAnd", required = false)Boolean isAnd,
            Model model) {
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getBookPageFilterByTag(CookieUtils.setHeader(req), page, 10, sort, desc, tagNameSet, isAnd, 0);
        model.addAttribute("sort", sort);
        model.addAttribute("desc", desc);
        model.addAttribute("view", "productList");
        model.addAttribute("productList", response.getBody().getContent());
        page = page == null ? 1 : page;
        model.addAttribute("page", page);
        model.addAttribute("totalPage", response.getBody().getTotalPages());
        model.addAttribute("mainText", "태그 검색 - " + tagNameSet);
        StringJoiner stringJoiner = new StringJoiner(",");
        for (String tagName : tagNameSet) {
            stringJoiner.add(tagName);
        }
        model.addAttribute("url", req.getRequestURI()+ "?tagName=" + stringJoiner.toString());
        return "index";
    }

    @GetMapping("/product/books/category/{categoryId}")
    public String getBookPageFilterByCategory(
            HttpServletRequest req,
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name= "size", required = false)Integer size,
            @RequestParam(name = "sort", required = false)String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            @PathVariable("categoryId") Long categoryId,
            Model model) {
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getBookPageFilterByCategory(CookieUtils.setHeader(req), page, 10, sort, desc, categoryId, 0);
        model.addAttribute("sort", sort);
        model.addAttribute("desc", desc);
        model.addAttribute("view", "productList");
        model.addAttribute("productList", response.getBody().getContent());
        page = page == null ? 1 : page;
        model.addAttribute("page", page);
        model.addAttribute("totalPage", response.getBody().getTotalPages());
        model.addAttribute("mainText", "카테고리 검색 - " + "");
        model.addAttribute("url", req.getRequestURI());
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
        model.addAttribute("sort", sort);
        model.addAttribute("desc", desc);
        model.addAttribute("view", "productList");
        model.addAttribute("productList", response.getBody().getContent());
        page = page == null ? 1 : page;
        model.addAttribute("page", page);
        model.addAttribute("totalPage", response.getBody().getTotalPages());
        model.addAttribute("mainText", "좋아요 목록");
        model.addAttribute("url", req.getRequestURI());
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
