package com.nhnacademy.codequestweb.controller.product.admin;


import com.nhnacademy.codequestweb.request.product.book_product.BookProductRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.book_product.BookProductUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.book.AladinBookResponseDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductUpdateResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.ProductCategory;
import com.nhnacademy.codequestweb.response.product.tag.Tag;
import com.nhnacademy.codequestweb.response.review.ReviewInfoResponseDto;
import com.nhnacademy.codequestweb.service.image.ImageService;
import com.nhnacademy.codequestweb.service.product.BookProductService;
import com.nhnacademy.codequestweb.service.review.ReviewService;
import com.nhnacademy.codequestweb.utils.BookUtils;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/product/book")
public class AdminBookController {
    private final BookProductService bookProductService;

    private final ImageService imageService;

    private final ReviewService reviewService;

    private final FlexmarkHtmlConverter flexmarkHtmlConverter = FlexmarkHtmlConverter.builder().build();

    private static final String ALTER_MESSAGE = "alterMessage";

    private static final String VIEW = "view";

    private static final String ADMIN_PAGE = "adminPage";

    private static final String REGISTER = "register";

    private static final String ACTION = "action";

    private static final String BOOK_REGISTER_FORM = "bookProductRegisterForm";

    private static final String ACTIVE_SECTION = "activeSection";

    private static final String PRODUCT = "product";

    private static final String INDEX = "index";

    private static final String ADMIN = "admin";

    private static final String PRODUCT_LIST_PAGE = "productListPage";

    private static final String MAIN_TEXT ="mainText";

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/register/aladin")
    public String registerAladinForm(HttpServletRequest req){
        req.setAttribute(VIEW, ADMIN_PAGE);
        req.setAttribute(ADMIN_PAGE,  BOOK_REGISTER_FORM);
        req.setAttribute(REGISTER, true);
        req.setAttribute("aladin", true);
        req.setAttribute(ACTION, REGISTER);
        req.setAttribute(ACTIVE_SECTION, PRODUCT);
        return INDEX;
    }

    @GetMapping("/register/self")
    public String registerSelfForm(HttpServletRequest req){
        req.setAttribute(VIEW, ADMIN_PAGE);
        req.setAttribute(ADMIN_PAGE,  BOOK_REGISTER_FORM);
        req.setAttribute(REGISTER, true);
        req.setAttribute("self", true);
        req.setAttribute(ACTION, REGISTER);
        req.setAttribute(ACTIVE_SECTION, PRODUCT);
        return INDEX;
    }


    @GetMapping("/update/{productId}")
    public String updateForm(HttpServletRequest req, @PathVariable("productId") Long productId, Model model){
        ResponseEntity<BookProductGetResponseDto> response = bookProductService.getSingleBookInfo(CookieUtils.setHeader(req), productId);
        BookProductGetResponseDto bookProductGetResponseDto = response.getBody();
        String description = Objects.requireNonNull(bookProductGetResponseDto).productDescription();
        log.info("description: {}", description);
        String markdown = flexmarkHtmlConverter.convert(description);
        List<String> categoryNameSet = bookProductGetResponseDto.categorySet().stream().map(ProductCategory::categoryName).toList();
        List<String> tagNameSet = bookProductGetResponseDto.tagSet().stream().map(Tag::tagName).toList();

        if (bookProductGetResponseDto.isbn() != null && !bookProductGetResponseDto.isbn().isBlank()){
            model.addAttribute("aladin", true);
        }

        model.addAttribute("update", true);
        model.addAttribute("productId", productId);
        model.addAttribute(ACTION, "update");
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
        model.addAttribute("categorySet", categoryNameSet);
        model.addAttribute("tagSet", tagNameSet);
        model.addAttribute("initialValue", markdown);
        model.addAttribute(VIEW, ADMIN_PAGE);
        model.addAttribute(ADMIN_PAGE,  BOOK_REGISTER_FORM);
        model.addAttribute("state", bookProductGetResponseDto.productState());
        return INDEX;
    }

    @GetMapping("/isbnCheck")
    public ResponseEntity<Void> checkIsbnExists(@RequestParam("isbn") String isbn){
        try {
            return bookProductService.isbnCheck(isbn);
        } catch (FeignException e) {
            return ResponseEntity.status(e.status()).body(null);
        }
    }

    @GetMapping("/aladinList")
    public String getAladinBookList(@RequestParam(name = "page", required = false)Integer page, @RequestParam("title")String title, Model model) {

        ResponseEntity<Page<AladinBookResponseDto>> aladinBookPageResponse = bookProductService.getBookList(page, title);

        page = page == null ? 1 : page;
        log.info("page : {}", page);
        Page<AladinBookResponseDto> aladinBooks = aladinBookPageResponse.getBody();
        if(aladinBooks != null){
            long totalElements = aladinBooks.getTotalElements();
            int totalPages = aladinBooks.getTotalPages();

            List<AladinBookResponseDto> aladinBookList = aladinBooks.getContent();
            model.addAttribute("bookList", aladinBookList);
            Set<Integer> pageNumSet = new LinkedHashSet<>();

            for (int i = 1; i <= totalPages; i++){
                if (i == 1 || (page - 2 <= i && i <= page + 2) || i == totalPages){
                    pageNumSet.add(i);
                }
            }
            model.addAttribute("pageNumSet", pageNumSet);
            if (totalElements == 100){
                model.addAttribute("warning", true);
            }
        }

        model.addAttribute("title",title);
        return "view/product/aladinBookList";
    }


    @PostMapping("/register")
    public String saveBook(@RequestParam(name = "coverImage", required = false) MultipartFile file, @ModelAttribute @Valid BookProductRegisterRequestDto dto, HttpServletRequest req, RedirectAttributes redirectAttributes){
        boolean imageSaved = true;
        if (file != null && !file.isEmpty()){
            imageSaved = false;
            try {
                String imageUrl = imageService.uploadImage(file);
                dto.setCover(imageUrl);
                imageSaved = true;
            }catch (FeignException e){
                log.warn("error occurred while uploading image. message : {}", e.getMessage());
            }
        }
        try {
            ResponseEntity<ProductRegisterResponseDto> responseEntity = bookProductService.saveBook(CookieUtils.setHeader(req), dto);
            if (responseEntity.getStatusCode().is2xxSuccessful()){
                if (imageSaved){
                    redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "성공적으로 등록되었습니다.");
                }else {
                    redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "도서는 등록되었지만, 표지 이미지를 저장하는 과정에서 문제가 발생했습니다.\n 자세한 정보는 로그를 확인하세요.");
                }
            }
        }catch (FeignException e){
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "도서를 등록하는 데 실패했습니다.\n자세한 정보는 로그를 확인하세요.");
            log.warn("error occurred while registering book product : {}", e.getMessage());
        }
        return "redirect:/admin/product/book/all";
    }

    @PutMapping("/update")
    public String updateBook(HttpServletRequest req, @ModelAttribute @Valid BookProductUpdateRequestDto dto, RedirectAttributes redirectAttributes){
        try {
            ResponseEntity<ProductUpdateResponseDto> responseEntity = bookProductService.updateBook(CookieUtils.setHeader(req), dto);
            if (responseEntity.getStatusCode().is2xxSuccessful()){
                redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "성공적으로 수정되었습니다.");
            }
        }catch (FeignException e){
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "상품 정보를 수정하는 데 실패했습니다.\n자세한 정보는 로그를 확인하세요.");
            log.warn("error occurred while updating book product : {}", e.getMessage());
        }
        return "redirect:/admin/product/book/all";
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
        BookUtils.setBookPage(response, page, sort, desc, model);
        model.addAttribute(MAIN_TEXT, "관리자 페이지");
        model.addAttribute("url", req.getRequestURI() + "?");
        model.addAttribute(VIEW, ADMIN_PAGE);
        model.addAttribute(ADMIN_PAGE, PRODUCT_LIST_PAGE);
        model.addAttribute(ADMIN, true);
        model.addAttribute(ACTIVE_SECTION, PRODUCT);
        return INDEX;
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

        BookUtils.setBookPage(response, page, sort, desc, model);
        model.addAttribute(MAIN_TEXT, "관리자 페이지 - 제목 : " + title);
        model.addAttribute("url", req.getRequestURI()+ "?title=" + title + "&");
        model.addAttribute(VIEW, ADMIN_PAGE);
        model.addAttribute(ADMIN_PAGE, PRODUCT_LIST_PAGE);
        model.addAttribute(ADMIN, true);
        return INDEX;
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
        BookUtils.setBookPage(response, page, sort, desc, model);
        model.addAttribute(MAIN_TEXT, "관리자 페이지 - 태그 : " + tagNameSet);
        model.addAttribute("url", req.getRequestURI() + "?");
        model.addAttribute(VIEW, ADMIN_PAGE);
        model.addAttribute(ADMIN_PAGE, PRODUCT_LIST_PAGE);
        model.addAttribute(ADMIN, true);
        return INDEX;
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
        BookUtils.setBookPage(response, page, sort, desc, model);
        model.addAttribute(MAIN_TEXT, "관리자 페이지 - 카테고리 필터");
        model.addAttribute("url", req.getRequestURI()+ "?");
        model.addAttribute(VIEW, ADMIN_PAGE);
        model.addAttribute(ADMIN_PAGE, PRODUCT_LIST_PAGE);
        model.addAttribute(ADMIN, true);
        return INDEX;
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
        BookUtils.setBookPage(response, page, sort, desc, model);
        model.addAttribute(MAIN_TEXT, "관리자 페이지 - 즐겨찾기");
        model.addAttribute("url", req.getRequestURI() + "?");
        model.addAttribute(VIEW, ADMIN_PAGE);
        model.addAttribute(ADMIN_PAGE, PRODUCT_LIST_PAGE);
        model.addAttribute(ADMIN, true);
        return INDEX;
    }

    @GetMapping("/{productId}")
    public String book(
            HttpServletRequest req,
            @PathVariable("productId") long productId,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        ResponseEntity<BookProductGetResponseDto> response = bookProductService.getSingleBookInfo(CookieUtils.setHeader(req), productId);

        BookUtils.setSingleBookInfo(response, model);

        Double totalReviewScore = reviewService.getReviewScore(productId);
        Page<ReviewInfoResponseDto> reviewPage = reviewService.getProductReviewPage(0, 10, productId);

        model.addAttribute(ADMIN, true);
        model.addAttribute(VIEW, ADMIN_PAGE);
        model.addAttribute(ADMIN_PAGE, "productBookDetail");
        model.addAttribute("averageScore", totalReviewScore);
        model.addAttribute("page", page);
        model.addAttribute("totalPage", reviewPage.getTotalPages());
        model.addAttribute("reviews", reviewPage.getContent());

        return INDEX;
    }
}
