package com.nhnacademy.codequestweb.controller.product.admin;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.codequestweb.exception.review.FileSaveException;
import com.nhnacademy.codequestweb.request.product.book_product.BookProductRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.book_product.BookProductUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.book.AladinBookResponseDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductUpdateResponseDto;
import com.nhnacademy.codequestweb.response.product.product_category.ProductCategory;
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
import java.net.URI;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

    private static final String REDIRECT_ADMIN_MAIN = "redirect:/admin/client/0";

    private static final String REDIRECT_PRODUCT_MAIN = "redirect:/admin/product/book/all";

    private static final String ATTRIBUTE_VALUE = "도서 정보 조회에 실패했습니다.";

    private static final String TEMPORARY_ERROR = "일시적으로 오류가 발생했습니다. \n 문제가 지속된다면 로그를 확인하세요.";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentNotValidException.class})
    public String handleMissingOrNotValidParamException(Exception e, RedirectAttributes redirectAttributes){
        if(e instanceof MissingServletRequestParameterException){
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "파라미터가 존재하지 않습니다.");
        }
        if(e instanceof MethodArgumentNotValidException manve){
            StringBuilder errorMessage = new StringBuilder("입력값 오류: ");
            manve.getBindingResult().getAllErrors().forEach(error -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessageForField = error.getDefaultMessage();
                errorMessage.append(String.format("'%s': %s; ", fieldName, errorMessageForField));
            });
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, errorMessage.toString());
        }
        return REDIRECT_ADMIN_MAIN;
    }

    @GetMapping("/register/aladin")
    public String registerAladinForm(HttpServletRequest req, RedirectAttributes redirectAttributes){
        try {
            ResponseEntity<Void> roleCheck = bookProductService.roleCheck(CookieUtils.setHeader(req));
            if (roleCheck.getStatusCode().is2xxSuccessful()) {
                req.setAttribute(VIEW, ADMIN_PAGE);
                req.setAttribute(ADMIN_PAGE,  BOOK_REGISTER_FORM);
                req.setAttribute(REGISTER, true);
                req.setAttribute("aladin", true);
                req.setAttribute(ACTION, REGISTER);
                req.setAttribute(ACTIVE_SECTION, PRODUCT);
                return INDEX;
            }else {
                log.warn("something went wrong, the status must be 2xx or 4xx or 5xx but {}", roleCheck.getStatusCode().value());
                redirectAttributes.addFlashAttribute(ALTER_MESSAGE, TEMPORARY_ERROR);
                return REDIRECT_ADMIN_MAIN;
            }
        }catch (Exception e){
            if (e instanceof FeignException.Forbidden || e instanceof FeignException.Unauthorized) {throw e;}
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, TEMPORARY_ERROR);
            return REDIRECT_ADMIN_MAIN;
        }
    }

    @GetMapping("/register/self")
    public String registerSelfForm(HttpServletRequest req, RedirectAttributes redirectAttributes){
        try {
            ResponseEntity<Void> roleCheck = bookProductService.roleCheck(CookieUtils.setHeader(req));
            if (roleCheck.getStatusCode().is2xxSuccessful()) {
                req.setAttribute(VIEW, ADMIN_PAGE);
                req.setAttribute(ADMIN_PAGE,  BOOK_REGISTER_FORM);
                req.setAttribute(REGISTER, true);
                req.setAttribute("self", true);
                req.setAttribute(ACTION, REGISTER);
                req.setAttribute(ACTIVE_SECTION, PRODUCT);
                return INDEX;
            }else {
                log.warn("something went wrong, the status must be 2xx or 4xx or 5xx but {}", roleCheck.getStatusCode().value());
                redirectAttributes.addFlashAttribute(ALTER_MESSAGE, TEMPORARY_ERROR);
                return REDIRECT_ADMIN_MAIN;
            }
        }catch (Exception e){
            if (e instanceof FeignException.Forbidden || e instanceof FeignException.Unauthorized) {throw e;}
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, TEMPORARY_ERROR);
            return REDIRECT_ADMIN_MAIN;
        }
    }

    @GetMapping("/update/{productId}")
    public String updateForm(HttpServletRequest req, @PathVariable("productId") Long productId,
                             RedirectAttributes redirectAttributes, Model model){
        try {
            ResponseEntity<BookProductGetResponseDto> response = bookProductService.getSingleBookInfo(CookieUtils.setHeader(req), productId);
            BookProductGetResponseDto bookProductGetResponseDto = response.getBody();
            if (bookProductGetResponseDto == null) {
                redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "응답 본문이 비어있습니다.");
                return REDIRECT_ADMIN_MAIN;
            }else {
                String description = bookProductGetResponseDto.productDescription();
                log.info("description: {}", description);
                String markdown = flexmarkHtmlConverter.convert(description);
                List<String> categoryNameSet = bookProductGetResponseDto.categorySet().stream().map(ProductCategory::categoryName).toList();
                List<String> tagNameSet = bookProductGetResponseDto.tagSet().stream().map(Tag::tagName).toList();

                if (bookProductGetResponseDto.isbn() != null && !bookProductGetResponseDto.isbn().isBlank()){
                    model.addAttribute("aladin", true);
                }else {
                    model.addAttribute("self", true);
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
                model.addAttribute("packable", bookProductGetResponseDto.packable());
                model.addAttribute("categorySet", categoryNameSet);
                model.addAttribute("tagSet", tagNameSet);
                model.addAttribute("initialValue", markdown);
                model.addAttribute(VIEW, ADMIN_PAGE);
                model.addAttribute(ADMIN_PAGE,  BOOK_REGISTER_FORM);
                model.addAttribute("state", bookProductGetResponseDto.productState());
                return INDEX;
            }
        }catch (Exception e){
            if (e instanceof FeignException.Forbidden || e instanceof FeignException.Unauthorized) {throw e;}
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, ATTRIBUTE_VALUE);
            return REDIRECT_ADMIN_MAIN;
        }
    }

    @GetMapping("/isbnCheck")
    public ResponseEntity<Void> checkIsbnExists(
            HttpServletRequest req,
            @RequestParam("isbn") String isbn){
        String header = req.getHeader("X-Requested-With");
        if (header == null || !header.equals("XMLHttpRequest")) {
            return ResponseEntity.status(302).location(URI.create("/")).build();
        }else {
            try {
                return bookProductService.isbnCheckForAdmin(CookieUtils.setHeader(req), isbn);
            }catch (FeignException.Conflict e){
                return ResponseEntity.status(409).build();
            }
        }
    }

    @GetMapping("/aladinList")
    public String getAladinBookList(
            HttpServletRequest req,
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam("title")String title,
            Model model) {
        try {
            ResponseEntity<Page<AladinBookResponseDto>> aladinBookPageResponse = bookProductService.getBookListForAdmin(CookieUtils.setHeader(req), page, title);

            page = page == null ? 1 : page;
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
        }catch (FeignException e){
            if (e instanceof FeignException.Forbidden || e instanceof FeignException.Unauthorized) {throw e;}
            return "view/product/window.close";
        }
    }

    private String imageSave(MultipartFile file){
        if (file != null && !file.isEmpty()){
            try {
                return imageService.uploadImage(file);
            } catch (FileSaveException e){
                log.warn("error occurred while uploading image. message : {}", e.getMessage());
                return null;
            }
        }else{
            return null;
        }
    }

    @PostMapping("/register")
    public String saveBook(
            @RequestParam(name = "coverImage", required = false) MultipartFile file,
            @ModelAttribute @Valid BookProductRegisterRequestDto dto,
            HttpServletRequest req,
            RedirectAttributes redirectAttributes){
        String image = imageSave(file);
        if (image != null){
            dto.setCover(image);
        }
        boolean imageSaved =  !(file != null && !file.isEmpty() && image == null);
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
            if (e instanceof FeignException.Forbidden || e instanceof FeignException.Unauthorized) {throw e;}
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "도서를 등록하는 데 실패했습니다.\n자세한 정보는 로그를 확인하세요.");
            log.warn("error occurred while registering book product : {}", e.getMessage());
        }
        return REDIRECT_PRODUCT_MAIN;
    }

    @PutMapping("/update")
    public String updateBook(
            @RequestParam(name = "coverImage", required = false) MultipartFile file,
            @ModelAttribute @Valid BookProductUpdateRequestDto dto,
            HttpServletRequest req,
            RedirectAttributes redirectAttributes){
        String image = imageSave(file);
        if (image != null){
            dto.setCover(image);
        }
        boolean imageSaved =  !(file != null && !file.isEmpty() && image == null);
        try {
            ResponseEntity<ProductUpdateResponseDto> responseEntity = bookProductService.updateBook(CookieUtils.setHeader(req), dto);
            if (responseEntity.getStatusCode().is2xxSuccessful()){
                if (imageSaved){
                    redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "성공적으로 수정되었습니다.");
                }else {
                    redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "수정은 이루어졌지만, 표지 이미지를 저장하는 과정에서 문제가 발생했습니다.\n 자세한 정보는 로그를 확인하세요.");
                }
            }
        }catch (FeignException e){
            if (e instanceof FeignException.Forbidden || e instanceof FeignException.Unauthorized) {throw e;}
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "상품 정보를 수정하는 데 실패했습니다.\n자세한 정보는 로그를 확인하세요.");
            log.warn("error occurred while updating book product : {}", e.getMessage());
        }
        return REDIRECT_PRODUCT_MAIN;
    }


    private String handlingResponseBody(
            Page<BookProductGetResponseDto> bookPage, RedirectAttributes redirectAttributes, Model model,
            Integer page, String sort, Boolean desc, Integer productState,  String mainText, String url
    ){
        if (bookPage == null){
            log.warn("the response body is null with response");
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, "응답 본문이 비어있습니다.");
            return REDIRECT_ADMIN_MAIN;
        }else {
            BookUtils.setBookPage(bookPage, page, sort, desc, model);
            model.addAttribute(MAIN_TEXT, mainText);
            model.addAttribute("url", url);
            model.addAttribute(VIEW, ADMIN_PAGE);
            model.addAttribute(ADMIN_PAGE, PRODUCT_LIST_PAGE);
            model.addAttribute(ADMIN, true);
            model.addAttribute(ACTIVE_SECTION, PRODUCT);
            model.addAttribute("productState", productState);
            return INDEX;
        }
    }

    private String makeMainText(String mainText, Integer productState){
        StringJoiner joiner = new StringJoiner(" - ");
        joiner.add("관리자 페이지");
        joiner.add(mainText);
        // product_state 라는 테이블을 뒀다면 아마 이렇게 일일이 switch 문에 조건 추가할 필요가 없을 거라 생각하지만 늦어서 일단 이렇게 구현했습니다.
        switch (productState){
            case null:
                joiner.add("전체");
                break;
            case 0 :
                joiner.add("판매 중");
                break;
            case 1 :
                joiner.add("임시 판매 중지");
                break;
            case 2 :
                joiner.add("영구 판매 중지");
                break;
            default:
                joiner.add("오류. (상태가 데이터베이스상 존재하지 않음.)");
                break;
        }
        return joiner.toString();
    }

    @GetMapping("/all")
    public String getAllBookPage(
            HttpServletRequest req,
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name= "size", required = false)Integer size,
            @RequestParam(name = "sort", required = false)String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            @RequestParam(name = "productState", required = false) Integer productState,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getAllBookPageForAdmin(CookieUtils.setHeader(req), page, size, sort, desc, productState);
            Page<BookProductGetResponseDto> bookPage = response.getBody();
            String mainText = makeMainText("모든 도서", productState);
            return handlingResponseBody(bookPage, redirectAttributes, model, page, sort, desc, productState, mainText, req.getRequestURI() + "?");
        }catch (Exception e){
            if (e instanceof FeignException.Forbidden || e instanceof FeignException.Unauthorized) {throw e;}
            log.warn("error occurred while getting all book page, message : {}", e.getMessage());
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, ATTRIBUTE_VALUE);
            return REDIRECT_ADMIN_MAIN;
        }
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
            RedirectAttributes redirectAttributes,
            Model model) {

        try {
            ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getNameContainingBookPageForAdmin(CookieUtils.setHeader(req), page, size, sort, desc, title, productState);
            Page<BookProductGetResponseDto> bookPage = response.getBody();
            String mainText = makeMainText("제목 : " + title, productState);
            return handlingResponseBody(bookPage, redirectAttributes, model, page, sort, desc, productState, mainText, req.getRequestURI()+ "?title=" + title + "&");
        }catch (Exception e){
            if (e instanceof FeignException.Forbidden || e instanceof FeignException.Unauthorized) {throw e;}
            log.warn("error occurred while getting name containing book page, message : {}", e.getMessage());
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, ATTRIBUTE_VALUE);
            return REDIRECT_ADMIN_MAIN;
        }
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
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getBookPageFilterByTagForAdmin(CookieUtils.setHeader(req), page, size, sort, desc, tagNameSet, isAnd, productState);
            Page<BookProductGetResponseDto> bookPage = response.getBody();
            String mainText = makeMainText("태그 : " + tagNameSet, productState);
            return handlingResponseBody(bookPage, redirectAttributes, model, page, sort, desc, productState, mainText, req.getRequestURI() + "?");
        }catch (Exception e){
            if (e instanceof FeignException.Forbidden || e instanceof FeignException.Unauthorized) {throw e;}
            log.warn("error occurred while getting tag filter book page, message : {}", e.getMessage());
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, ATTRIBUTE_VALUE);
            return REDIRECT_ADMIN_MAIN;
        }
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
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            ResponseEntity<Map<String, Page<BookProductGetResponseDto>>> response = bookProductService.getBookPageFilterByCategoryForAdmin(CookieUtils.setHeader(req), page, size, sort, desc, categoryId, productState);
            Map<String, Page<BookProductGetResponseDto>> responseBodyMap = response.getBody();
            Page<BookProductGetResponseDto> bookPage = BookUtils.getBookPageFromMap(objectMapper, responseBodyMap, model);
            String mainText = makeMainText("카테고리", productState);
            return handlingResponseBody(bookPage, redirectAttributes, model, page, sort, desc, productState, mainText, req.getRequestURI() + "?");
        }catch (Exception e){
            if (e instanceof FeignException.Forbidden || e instanceof FeignException.Unauthorized) {throw e;}
            log.warn("error occurred while getting category filter book page, message : {}", e.getMessage());
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, ATTRIBUTE_VALUE);
            return REDIRECT_ADMIN_MAIN;
        }
    }

    @GetMapping("/like")
    public String getLikeBookPage(
            HttpServletRequest req,
            @RequestParam(name = "page", required = false)Integer page,
            @RequestParam(name= "size", required = false)Integer size,
            @RequestParam(name = "sort", required = false)String sort,
            @RequestParam(name = "desc", required = false)Boolean desc,
            @RequestParam(name = "productState", required = false) Integer productState,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            ResponseEntity<Page<BookProductGetResponseDto>> response = bookProductService.getLikeBookPageForAdmin(CookieUtils.setHeader(req), page, size, sort, desc, productState);
            Page<BookProductGetResponseDto> bookPage = response.getBody();
            String mainText = makeMainText("즐겨찾기", productState);
            return handlingResponseBody(bookPage, redirectAttributes, model, page, sort, desc, productState, mainText, req.getRequestURI() + "?");
        }catch (Exception e){
            if (e instanceof FeignException.Forbidden || e instanceof FeignException.Unauthorized) {throw e;}
            log.warn("error occurred while getting like book page, message : {}", e.getMessage());
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, ATTRIBUTE_VALUE);
            return REDIRECT_ADMIN_MAIN;
        }
    }

    private void getReview(long productId, Model model){
        try {
            Double totalReviewScore = reviewService.getReviewScore(productId);
            Page<ReviewInfoResponseDto> reviewPage = reviewService.getProductReviewPage(0, 10, productId);

            model.addAttribute("averageScore", totalReviewScore);
            model.addAttribute("totalPage", reviewPage.getTotalPages());
            model.addAttribute("reviews", reviewPage.getContent());
        }catch (FeignException e){
            log.warn("error occurred while getting book review with id {}, message : {}", productId, e.getMessage());
            model.addAttribute(ALTER_MESSAGE, "리뷰 조회 과정에서 오류가 발생했습니다.");
            model.addAttribute("averageScore", 0.0);
            model.addAttribute("totalPage", 0);
            model.addAttribute("reviews", null);
        }
    }

    @GetMapping("/{productId}")
    public String book(
            HttpServletRequest req,
            @PathVariable("productId") long productId,
            @RequestParam(defaultValue = "0") int page,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            ResponseEntity<BookProductGetResponseDto> response = bookProductService.getSingleBookInfoForAdmin(CookieUtils.setHeader(req), productId);

            BookUtils.setSingleBookInfo(response, model);

            model.addAttribute(ADMIN, true);
            model.addAttribute(VIEW, ADMIN_PAGE);
            model.addAttribute(ADMIN_PAGE, "productBookDetail");
            model.addAttribute("page", page);

            getReview(productId, model);

            return INDEX;
        }catch (FeignException e){
            if (e instanceof FeignException.Forbidden || e instanceof FeignException.Unauthorized) {throw e;}
            redirectAttributes.addFlashAttribute(ALTER_MESSAGE, ATTRIBUTE_VALUE);
            return REDIRECT_PRODUCT_MAIN;
        }
    }
}
