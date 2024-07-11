package com.nhnacademy.codequestweb.controller.product.adminOnly;


import com.nhnacademy.codequestweb.request.product.ProductStateUpdateRequestDto;
import com.nhnacademy.codequestweb.request.product.bookProduct.BookProductRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.bookProduct.BookProductUpdateRequestDto;
import com.nhnacademy.codequestweb.request.product.common.InventoryIncreaseRequestDto;
import com.nhnacademy.codequestweb.request.product.common.InventorySetRequestDto;
import com.nhnacademy.codequestweb.response.product.book.AladinBookResponseDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductUpdateResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.ProductCategory;
import com.nhnacademy.codequestweb.response.product.tag.Tag;
import com.nhnacademy.codequestweb.service.product.BookProductService;
import com.nhnacademy.codequestweb.utils.BookPageUtils;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/product/book")
public class AdminBookController {
    private final BookProductService bookProductService;

    private final MessageSource messageSource;

    private final FlexmarkHtmlConverter flexmarkHtmlConverter = FlexmarkHtmlConverter.builder().build();

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/register")
    public String registerForm(HttpServletRequest req){
        req.setAttribute("view", "adminPage");
        req.setAttribute("adminPage", "bookProductRegisterForm");
        req.setAttribute("register", true);
        req.setAttribute("action", "register");
        return "index";
    }

    @GetMapping("/update/{productId}")
    public String updateForm(HttpServletRequest req, @PathVariable("productId") Long productId, Model model){
        ResponseEntity<BookProductGetResponseDto> response = bookProductService.getSingleBookInfo(CookieUtils.setHeader(req), productId);
        BookProductGetResponseDto bookProductGetResponseDto = response.getBody();
        String description = bookProductGetResponseDto.productDescription();
        log.info("description: {}", description);
        String markdown = flexmarkHtmlConverter.convert(description);
        log.info("markdown: {}", markdown);
        List<String> categoryNameSet = bookProductGetResponseDto.categorySet().stream().map(ProductCategory::categoryName).toList();
        StringJoiner categoryJoiner = new StringJoiner(",");
        for (String categoryName : categoryNameSet) {
            categoryJoiner.add(categoryName);
        }

        List<String> tagNameSet = bookProductGetResponseDto.tagSet().stream().map(Tag::tagName).toList();
        StringJoiner tagJoiner = new StringJoiner(",");
        for (String tagName : tagNameSet) {
            tagJoiner.add(tagName);
        }

        model.addAttribute("update", true);
        model.addAttribute("productId", productId);
        model.addAttribute("action", "update");
        model.addAttribute("cover", bookProductGetResponseDto.cover());
        model.addAttribute("title", bookProductGetResponseDto.title());
        model.addAttribute("publisher", bookProductGetResponseDto.publisher());
        model.addAttribute("author", bookProductGetResponseDto.author());
        model.addAttribute("pubDate", bookProductGetResponseDto.pubDate());
        model.addAttribute("isbn", bookProductGetResponseDto.isbn());
        model.addAttribute("isbn13", bookProductGetResponseDto.isbn13());
        model.addAttribute("priceStandard", bookProductGetResponseDto.productPriceStandard());
        model.addAttribute("priceSales", bookProductGetResponseDto.productPriceSales());
        model.addAttribute("productName", bookProductGetResponseDto.productName());
        model.addAttribute("inventory", bookProductGetResponseDto.productInventory());
        model.addAttribute("categorySet", categoryJoiner.toString());
        model.addAttribute("tagSet", tagJoiner.toString());
        model.addAttribute("initialValue", markdown);
        model.addAttribute("view", "adminPage");
        model.addAttribute("adminPage", "bookProductRegisterForm");
        return "index";
    }


    //register form 외에서는 호출 불가함. 자바스크립트로 통제해놓음
    @GetMapping("/aladinList")
    public String getAladinBookList(@RequestParam(name = "page", required = false)Integer page, @RequestParam("title")String title, Model model) {

        ResponseEntity<Page<AladinBookResponseDto>> aladinBookPageResponse = bookProductService.getBookList(page, title);

        Page<AladinBookResponseDto> aladinBooks = aladinBookPageResponse.getBody();
        if(aladinBooks != null){
            long totalElements = aladinBooks.getTotalElements();
            int totalPages = aladinBooks.getTotalPages();
            if (totalPages != 0){
                List<AladinBookResponseDto> aladinBookList = aladinBooks.getContent();
                model.addAttribute("bookList", aladinBookList);
                Set<Integer> pageNumSet = new LinkedHashSet<>();
                if (page == null){
                    page = 1;
                }
                for (int i = 1; i <= totalPages; i++){
                    if (i == 1 || (page - 2 <= i && i <= page + 2) || i == totalPages){
                        pageNumSet.add(i);
                    }
                }
                model.addAttribute("pageNumSet", pageNumSet);
            } else {
                model.addAttribute("empty", true);
            }
            if (totalElements == 100){
                model.addAttribute("warning", true);
            }
        }else {
            model.addAttribute("empty", true);
            return "index";
        }

        log.info("page : {}, size: {} , tatal page : {}, total elements : {}", page, aladinBooks.getSize(), aladinBooks.getTotalPages(), aladinBooks.getTotalElements());

        model.addAttribute("title",title);
        return "view/product/aladinBookList";
    }


    @PostMapping("/register")
    public String saveBook(@RequestParam("title")String title, @ModelAttribute @Valid BookProductRegisterRequestDto dto, HttpServletRequest req, Model model){
        log.info("title: {}", title);
        log.info("dto : {}", dto);
        String encodedData = new String(dto.toString().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        log.info("Encoded data: {}", encodedData);
        ResponseEntity<ProductRegisterResponseDto> responseEntity = bookProductService.saveBook(CookieUtils.setHeader(req), dto);
        return "redirect:/";
    }

    @PutMapping("/update")
    public String updateBook(HttpServletRequest req, @ModelAttribute @Valid BookProductUpdateRequestDto dto){
        log.info("update book called save book");
        ResponseEntity<ProductUpdateResponseDto> responseEntity = bookProductService.updateBook(CookieUtils.setHeader(req), dto);
        log.info("status code : {}",responseEntity.getStatusCode().value());
        return "redirect:/";
    }



    @GetMapping("/all")
    public String getAllBookPage(
            HttpServletRequest req,
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name= "size", required = false)Integer size,
            @RequestParam(name = "sort", required = false)String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            @RequestParam(name = "productState", required = false) Integer productState,
            Model model) {
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getAllBookPage(CookieUtils.setHeader(req), page, size, sort, desc, productState);
        BookPageUtils.setBookPage(response, page, sort, desc, model);
        model.addAttribute("mainText", "관리자 페이지");
        model.addAttribute("url", req.getRequestURI() + "?");
        model.addAttribute("view", "adminPage");
        model.addAttribute("adminPage", "productListPage");
        model.addAttribute("admin", true);
        return "index";
    }

    @GetMapping("/containing")
    public String getNameContainingBookPage(
            HttpServletRequest req,
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name= "size", required = false)Integer size,
            @RequestParam(name = "sort", required = false)String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            @RequestParam(name = "productState", required = false) Integer productState,
            @RequestParam("title")String title,
            Model model) {
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getNameContainingBookPage(CookieUtils.setHeader(req), page, size, sort, desc, title, productState);

        BookPageUtils.setBookPage(response, page, sort, desc, model);
        model.addAttribute("mainText", "관리자 페이지 - 제목 : " + title);
        model.addAttribute("url", req.getRequestURI()+ "?title=" + title + "&");
        model.addAttribute("admin", true);
        return "index";
    }

    @GetMapping("/tagFilter")
    public String getBookPageFilterByTag(
            HttpServletRequest req,
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name= "size", required = false)Integer size,
            @RequestParam(name = "sort", required = false)String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            @RequestParam(name = "tagName") Set<String> tagNameSet,
            @RequestParam(name = "isAnd", required = false)Boolean isAnd,
            @RequestParam(name = "productState", required = false) Integer productState,
            Model model) {
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getBookPageFilterByTag(CookieUtils.setHeader(req), page, size, sort, desc, tagNameSet, isAnd, productState);
        BookPageUtils.setBookPage(response, page, sort, desc, model);
        model.addAttribute("mainText", "관리자 페이지 - 태그 : " + tagNameSet);
        model.addAttribute("url", req.getRequestURI() + "?");
        model.addAttribute("admin", true);
        return "index";
    }

    @GetMapping("/category/{categoryId}")
    public String getBookPageFilterByCategory(
            HttpServletRequest req,
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name= "size", required = false)Integer size,
            @RequestParam(name = "sort", required = false)String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            @RequestParam(name = "productState", required = false) Integer productState,
            @PathVariable("categoryId") Long categoryId,
            Model model) {
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getBookPageFilterByCategory(CookieUtils.setHeader(req), page, size, sort, desc, categoryId, productState);
        BookPageUtils.setBookPage(response, page, sort, desc, model);
        model.addAttribute("mainText", "관리자 페이지 - 카테고리 필터");
        model.addAttribute("url", req.getRequestURI()+ "?");
        model.addAttribute("admin", true);
        return "index";
    }

    @GetMapping("/like")
    public String getLikeBookPage(
            HttpServletRequest req,
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name= "size", required = false)Integer size,
            @RequestParam(name = "sort", required = false)String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            Model model) {
        ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getLikeBookPage(CookieUtils.setHeader(req), page, size, sort, desc);
        BookPageUtils.setBookPage(response, page, sort, desc, model);
        model.addAttribute("mainText", "관리자 페이지 - 즐겨찾기");
        model.addAttribute("url", req.getRequestURI() + "?");
        model.addAttribute("admin", true);
        return "index";
    }


}