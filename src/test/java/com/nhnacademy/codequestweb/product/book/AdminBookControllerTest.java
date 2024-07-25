package com.nhnacademy.codequestweb.product.book;

import com.nhnacademy.codequestweb.controller.product.admin.AdminBookController;
import com.nhnacademy.codequestweb.exception.review.FileSaveException;
import com.nhnacademy.codequestweb.request.product.book_product.BookProductRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.book_product.BookProductUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.book.AladinBookResponseDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductUpdateResponseDto;
import com.nhnacademy.codequestweb.response.product.product_category.ProductCategory;
import com.nhnacademy.codequestweb.response.product.tag.Tag;
import com.nhnacademy.codequestweb.service.image.ImageService;
import com.nhnacademy.codequestweb.service.product.BookProductService;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class AdminBookControllerTest {

    @InjectMocks
    private AdminBookController adminBookController;

    @Mock
    private BookProductService bookProductService;

    @Mock
    private ImageService imageService;

    private MockMvc mockMvc;

    private static final String ALTER_MESSAGE = "alterMessage";

    private static final String VIEW = "view";

    private static final String ADMIN_PAGE = "adminPage";

    private static final String REGISTER = "register";

    private static final String ACTION = "action";

    private static final String BOOK_REGISTER_FORM = "bookProductRegisterForm";

    private static final String ACTIVE_SECTION = "activeSection";

    private static final String PRODUCT = "product";

    private static final String INDEX = "index";

    private static final String PRODUCT_LIST_PAGE = "productListPage";

    private static final String MAIN_TEXT ="mainText";

    private HttpHeaders headers;


    private BookProductRegisterRequestDto registerRequestDto;

    private BookProductUpdateRequestDto updateRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminBookController).build();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        registerRequestDto = BookProductRegisterRequestDto.builder()
                .title("test title")
                .publisher("test publisher")
                .author("test author")
                .pubDate(LocalDate.of(2000, 12, 25))
                .isbn("123456789a")
                .isbn13("123456789abcd")
                .cover("test cover")
                .productName("test product name")
                .packable(true)
                .productDescription("test decription")
                .productPriceStandard(10L)
                .productPriceSales(8L)
                .productInventory(100L)
                .categories(Set.of("category1", "category2"))
                .tags(Set.of("tag1", "tag2", "tag3"))
                .build();

        updateRequestDto = BookProductUpdateRequestDto.builder()
                .productId(1L)
                .productName("updated name")
                .packable(false)
                .productDescription("updated description")
                .productPriceSales(9L)
                .productInventory(150L)
                .productState(1)
                .categories(Set.of("updated category"))
                .tags(Set.of("updated tag"))
                .build();
    }


    @Test
    void getAladinFormTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/admin/product/book/register/aladin"))
                .andExpect(status().isOk())
                .andExpect(view().name(INDEX))
                .andReturn();

        HttpServletRequest request = mvcResult.getRequest();

        assertThat(request.getAttribute(VIEW)).isEqualTo(ADMIN_PAGE);  // Assuming VIEW is "view" and ADMIN_PAGE is "admin_page"
        assertThat(request.getAttribute(ADMIN_PAGE)).isEqualTo(BOOK_REGISTER_FORM);  // Assuming BOOK_REGISTER_FORM is "book_register_form"
        assertThat(request.getAttribute(REGISTER)).isEqualTo(true);
        assertThat(request.getAttribute("aladin")).isEqualTo(true);
        assertThat(request.getAttribute(ACTION)).isEqualTo(REGISTER);  // Assuming REGISTER is "register"
        assertThat(request.getAttribute(ACTIVE_SECTION)).isEqualTo(PRODUCT);  // Assuming ACTIVE_SECTION is "active_section" and
    }

    @Test
    void getSelfFormTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/admin/product/book/register/self"))
                .andExpect(status().isOk())
                .andExpect(view().name(INDEX))
                .andReturn();

        HttpServletRequest request = mvcResult.getRequest();

        assertThat(request.getAttribute(VIEW)).isEqualTo(ADMIN_PAGE);  // Assuming VIEW is "view" and ADMIN_PAGE is "admin_page"
        assertThat(request.getAttribute(ADMIN_PAGE)).isEqualTo(BOOK_REGISTER_FORM);  // Assuming BOOK_REGISTER_FORM is "book_register_form"
        assertThat(request.getAttribute(REGISTER)).isEqualTo(true);
        assertThat(request.getAttribute("self")).isEqualTo(true);
        assertThat(request.getAttribute(ACTION)).isEqualTo(REGISTER);  // Assuming REGISTER is "register"
        assertThat(request.getAttribute(ACTIVE_SECTION)).isEqualTo(PRODUCT);  // Assuming ACTIVE_SECTION is "active_section" and
    }

    @Test
    void getUpdateFormTest1() throws Exception {
        Set<Tag> tags = Set.of(
                new Tag(1L, "tag1"),
                new Tag(2L, "tag2"));
        Set<ProductCategory> categories = Set.of(
                new ProductCategory(1L, "category1",null),
                new ProductCategory(2L, "category2",null));

        BookProductGetResponseDto responseDto = BookProductGetResponseDto.builder()
                .isbn("test isbn")
                .tagSet(tags)
                .categorySet(categories)
                .productDescription("test description")
                .productState(15)
                .build();

        when(bookProductService.getSingleBookInfo(any(),eq(1L))).thenReturn(ResponseEntity.ok(responseDto));

        mockMvc.perform(get("/admin/product/book/update/1")
                .headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(INDEX))
                .andExpect(model().attribute("initialValue","test description"))
                .andExpect(model().attribute("aladin", true))
                .andExpect(model().attribute("state", 15));
    }

    @Test
    void getUpdateFormTest2() throws Exception {
        BookProductGetResponseDto responseDto = BookProductGetResponseDto.builder()
                .productDescription("test description")
                .productState(15)
                .categorySet(Set.of())
                .tagSet(Set.of())
                .build();

        when(bookProductService.getSingleBookInfo(any(),eq(1L))).thenReturn(ResponseEntity.ok(responseDto));

        mockMvc.perform(get("/admin/product/book/update/1")
                        .headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name(INDEX))
                .andExpect(model().attribute("initialValue","test description"))
                .andExpect(model().attributeDoesNotExist("aladin"));
    }

    @Test
    void checkIsbnExistTest1() throws Exception {
        String isbn = "test isbn";
        when(bookProductService.isbnCheck(isbn)).thenReturn(ResponseEntity.status(409).body(null));

        mockMvc.perform(get("/admin/product/book/isbnCheck")
                        .param("isbn", isbn)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void checkIsbnExistTest2() throws Exception {
        String isbn = "test isbn";
        when(bookProductService.isbnCheck(isbn)).thenReturn(ResponseEntity.ok(null));


        mockMvc.perform(get("/admin/product/book/isbnCheck")
                        .param("isbn", isbn)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void getAladinBookListTest1() throws Exception {
        String title = "test title";
        List<AladinBookResponseDto> responseDtoList = List.of(
                AladinBookResponseDto.builder().build(),
                AladinBookResponseDto.builder().build()
        );

        Page<AladinBookResponseDto> responseDtoPage = new PageImpl<>(responseDtoList);
        when(bookProductService.getBookList(any(), eq(title))).thenReturn(ResponseEntity.ok(responseDtoPage));

        mockMvc.perform(get("/admin/product/book/aladinList")
                        .param("title", title)
                        .headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name("view/product/aladinBookList"))
                .andExpect(model().attribute("bookList", responseDtoList))
                .andExpect(model().attribute("title", title))
                .andExpect(model().attribute("pageNumSet", Set.of(1)))
                .andExpect(model().attributeDoesNotExist("warning"));
    }

    @Test
    void getAladinBookListTest2() throws Exception {
        String title = "test title";
        List<AladinBookResponseDto> responseDtoList = new ArrayList<>();
        for (int i = 0 ; i < 100; i ++){
            responseDtoList.add(AladinBookResponseDto.builder().build());
        }

        Pageable pageable = PageRequest.of(0, 4);
        Page<AladinBookResponseDto> responseDtoPage = new PageImpl<>(responseDtoList, pageable, responseDtoList.size());
        when(bookProductService.getBookList(any(), eq(title))).thenReturn(ResponseEntity.ok(responseDtoPage));

        mockMvc.perform(get("/admin/product/book/aladinList")
                        .param("title", title)
                        .param("page", "10")
                        .headers(headers))
                .andExpect(status().isOk())
                .andExpect(view().name("view/product/aladinBookList"))
                .andExpect(model().attribute("bookList", responseDtoList))
                .andExpect(model().attribute("title", title))
                .andExpect(model().attribute("pageNumSet", Set.of(1, 8, 9, 10, 11, 12, 25)))
                .andExpect(model().attribute("warning", true));
    }

    @Test
    void saveBookTest1() throws Exception {
        MockMultipartFile
                file = new MockMultipartFile("coverImage", "cover.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());

        when(imageService.uploadImage(any(MultipartFile.class))).thenReturn("image_url");
        when(bookProductService.saveBook(any(), any())).thenReturn(ResponseEntity.ok(new ProductRegisterResponseDto(1L, LocalDateTime.now())));

        mockMvc.perform(multipart("/admin/product/book/register")
                        .file(file)
                        .param("title", registerRequestDto.getTitle())
                        .param("publisher", registerRequestDto.getPublisher())
                        .param("author", registerRequestDto.getAuthor())
                        .param("pubDate", registerRequestDto.getPubDate().toString())
                        .param("isbn", registerRequestDto.getIsbn())
                        .param("isbn13", registerRequestDto.getIsbn13())
                        .param("productName", registerRequestDto.getProductName())
                        .param("packable", String.valueOf(registerRequestDto.isPackable()))
                        .param("productDescription", registerRequestDto.getProductDescription())
                        .param("productPriceStandard", String.valueOf(registerRequestDto.getProductPriceStandard()))
                        .param("productPriceSales", String.valueOf(registerRequestDto.getProductPriceSales()))
                        .param("productInventory", String.valueOf(registerRequestDto.getProductInventory()))
                        .param("categories", String.join(",", registerRequestDto.getCategories()))
                        .param("tags", String.join(",", registerRequestDto.getTags())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/product/book/all"))
                .andExpect(flash().attribute(ALTER_MESSAGE, "성공적으로 등록되었습니다."));
    }


    @Test
    void saveBookTest2() throws Exception {
        when(bookProductService.saveBook(any(), any())).thenReturn(ResponseEntity.ok(new ProductRegisterResponseDto(1L, LocalDateTime.now())));

        mockMvc.perform(multipart("/admin/product/book/register")
                        .param("title", registerRequestDto.getTitle())
                        .param("publisher", registerRequestDto.getPublisher())
                        .param("author", registerRequestDto.getAuthor())
                        .param("pubDate", registerRequestDto.getPubDate().toString())
                        .param("isbn", registerRequestDto.getIsbn())
                        .param("isbn13", registerRequestDto.getIsbn13())
                        .param("productName", registerRequestDto.getProductName())
                        .param("packable", String.valueOf(registerRequestDto.isPackable()))
                        .param("productDescription", registerRequestDto.getProductDescription())
                        .param("productPriceStandard", String.valueOf(registerRequestDto.getProductPriceStandard()))
                        .param("productPriceSales", String.valueOf(registerRequestDto.getProductPriceSales()))
                        .param("productInventory", String.valueOf(registerRequestDto.getProductInventory()))
                        .param("categories", String.join(",", registerRequestDto.getCategories()))
                        .param("tags", String.join(",", registerRequestDto.getTags())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/product/book/all"))
                .andExpect(flash().attribute(ALTER_MESSAGE, "성공적으로 등록되었습니다."));
    }

    @Test
    void saveBookTest3() throws Exception {
        MockMultipartFile
                file = new MockMultipartFile("coverImage", "cover.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());

        when(imageService.uploadImage(any(MultipartFile.class))).thenThrow(FeignException.class);
        when(bookProductService.saveBook(any(), any())).thenReturn(ResponseEntity.ok(new ProductRegisterResponseDto(1L, LocalDateTime.now())));

        mockMvc.perform(multipart("/admin/product/book/register")
                        .file(file)
                        .param("title", registerRequestDto.getTitle())
                        .param("publisher", registerRequestDto.getPublisher())
                        .param("author", registerRequestDto.getAuthor())
                        .param("pubDate", registerRequestDto.getPubDate().toString())
                        .param("isbn", registerRequestDto.getIsbn())
                        .param("isbn13", registerRequestDto.getIsbn13())
                        .param("productName", "t")
                        .param("packable", String.valueOf(registerRequestDto.isPackable()))
                        .param("productDescription", registerRequestDto.getProductDescription())
                        .param("productPriceStandard", String.valueOf(registerRequestDto.getProductPriceStandard()))
                        .param("productPriceSales", String.valueOf(registerRequestDto.getProductPriceSales()))
                        .param("productInventory", String.valueOf(registerRequestDto.getProductInventory()))
                        .param("categories", String.join(",", registerRequestDto.getCategories()))
                        .param("tags", String.join(",", registerRequestDto.getTags())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveBookTest4() throws Exception {
        MockMultipartFile
                file = new MockMultipartFile("coverImage", "cover.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());

        when(imageService.uploadImage(any(MultipartFile.class))).thenThrow(FileSaveException.class);
        when(bookProductService.saveBook(any(), any())).thenReturn(ResponseEntity.ok(new ProductRegisterResponseDto(1L, LocalDateTime.now())));

        mockMvc.perform(multipart("/admin/product/book/register")
                        .file(file)
                        .param("title", registerRequestDto.getTitle())
                        .param("publisher", registerRequestDto.getPublisher())
                        .param("author", registerRequestDto.getAuthor())
                        .param("pubDate", registerRequestDto.getPubDate().toString())
                        .param("isbn", registerRequestDto.getIsbn())
                        .param("isbn13", registerRequestDto.getIsbn13())
                        .param("productName", registerRequestDto.getProductName())
                        .param("packable", String.valueOf(registerRequestDto.isPackable()))
                        .param("productDescription", registerRequestDto.getProductDescription())
                        .param("productPriceStandard", String.valueOf(registerRequestDto.getProductPriceStandard()))
                        .param("productPriceSales", String.valueOf(registerRequestDto.getProductPriceSales()))
                        .param("productInventory", String.valueOf(registerRequestDto.getProductInventory()))
                        .param("categories", String.join(",", registerRequestDto.getCategories()))
                        .param("tags", String.join(",", registerRequestDto.getTags())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/product/book/all"))
                .andExpect(flash().attribute(ALTER_MESSAGE, "도서는 등록되었지만, 표지 이미지를 저장하는 과정에서 문제가 발생했습니다.\n 자세한 정보는 로그를 확인하세요."));
    }


    @Test
    void saveBookTest5() throws Exception {
        when(bookProductService.saveBook(any(), any())).thenThrow(FeignException.class);

        mockMvc.perform(multipart("/admin/product/book/register")
                        .param("title", registerRequestDto.getTitle())
                        .param("publisher", registerRequestDto.getPublisher())
                        .param("author", registerRequestDto.getAuthor())
                        .param("pubDate", registerRequestDto.getPubDate().toString())
                        .param("isbn", registerRequestDto.getIsbn())
                        .param("isbn13", registerRequestDto.getIsbn13())
                        .param("productName", registerRequestDto.getProductName())
                        .param("packable", String.valueOf(registerRequestDto.isPackable()))
                        .param("productDescription", registerRequestDto.getProductDescription())
                        .param("productPriceStandard", String.valueOf(registerRequestDto.getProductPriceStandard()))
                        .param("productPriceSales", String.valueOf(registerRequestDto.getProductPriceSales()))
                        .param("productInventory", String.valueOf(registerRequestDto.getProductInventory()))
                        .param("categories", String.join(",", registerRequestDto.getCategories()))
                        .param("tags", String.join(",", registerRequestDto.getTags())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/product/book/all"))
                .andExpect(flash().attribute(ALTER_MESSAGE, "도서를 등록하는 데 실패했습니다.\n자세한 정보는 로그를 확인하세요."));
    }


    @Test
    void updateBookTest1() throws Exception {
        when(bookProductService.updateBook(any(), any())).thenReturn(ResponseEntity.ok(new ProductUpdateResponseDto(LocalDateTime.now())));

        mockMvc.perform(multipart("/admin/product/book/update")
                        .with(request -> {
                            request.setMethod("PUT"); // 강제로 PUT 메서드로 설정
                            return request;
                        })
                        .param("productId", String.valueOf(updateRequestDto.getProductId()))
                        .param("productName", updateRequestDto.getProductName())
                        .param("packable", String.valueOf(updateRequestDto.isPackable()))
                        .param("productDescription", updateRequestDto.getProductDescription())
                        .param("productPriceSales", String.valueOf(updateRequestDto.getProductPriceSales()))
                        .param("productInventory", String.valueOf(updateRequestDto.getProductInventory()))
                        .param("categories", String.join(",", updateRequestDto.getCategories()))
                        .param("productState", String.valueOf(updateRequestDto.getProductState()))
                        .param("tags", String.join(",", updateRequestDto.getTags())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/product/book/all"))
                .andExpect(flash().attribute(ALTER_MESSAGE, "성공적으로 수정되었습니다."));
    }

    @Test
    void updateBookTest2() throws Exception {
        when(bookProductService.updateBook(any(), any())).thenThrow(FeignException.class);

        mockMvc.perform(put("/admin/product/book/update")
                        .param("productId", String.valueOf(updateRequestDto.getProductId()))
                        .param("productName", "t")
                        .param("packable", String.valueOf(updateRequestDto.isPackable()))
                        .param("productDescription", updateRequestDto.getProductDescription())
                        .param("productPriceSales", String.valueOf(updateRequestDto.getProductPriceSales()))
                        .param("productInventory", String.valueOf(updateRequestDto.getProductInventory()))
                        .param("categories", String.join(",", updateRequestDto.getCategories()))
                        .param("productState", String.valueOf(updateRequestDto.getProductState()))
                        .param("tags", String.join(",", updateRequestDto.getTags())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBookTest3() throws Exception {
        when(bookProductService.updateBook(any(), any())).thenThrow(FeignException.class);

        mockMvc.perform(put("/admin/product/book/update")
                        .param("productId", String.valueOf(updateRequestDto.getProductId()))
                        .param("productName", updateRequestDto.getProductName())
                        .param("packable", String.valueOf(updateRequestDto.isPackable()))
                        .param("productDescription", updateRequestDto.getProductDescription())
                        .param("productPriceSales", String.valueOf(updateRequestDto.getProductPriceSales()))
                        .param("productInventory", String.valueOf(updateRequestDto.getProductInventory()))
                        .param("categories", String.join(",", updateRequestDto.getCategories()))
                        .param("productState", String.valueOf(updateRequestDto.getProductState()))
                        .param("tags", String.join(",", updateRequestDto.getTags())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/product/book/all"))
                .andExpect(flash().attribute(ALTER_MESSAGE, "상품 정보를 수정하는 데 실패했습니다.\n자세한 정보는 로그를 확인하세요."));
    }

    @Test
    void getAllBookPageTest() throws Exception {
        List<BookProductGetResponseDto> responseDtoList = new ArrayList<>();
        for (int i = 0 ; i < 10; i ++){
            responseDtoList.add(BookProductGetResponseDto.builder().build());
        }

        Pageable pageable = PageRequest.of(0, 4);
        Page<BookProductGetResponseDto> responseDtoPage = new PageImpl<>(responseDtoList, pageable, responseDtoList.size());
        when(bookProductService.getAllBookPage(any(), eq(1), eq(10), eq("sort"), eq(true), eq(0)))
                .thenReturn(ResponseEntity.ok(responseDtoPage));

        mockMvc.perform(get("/admin/product/book/all")
                        .param("page", String.valueOf(1))
                        .param("size", String.valueOf(10))
                        .param("sort", "sort")
                        .param("desc", String.valueOf(true))
                        .param("productState", String.valueOf(0)))
                .andExpect(status().isOk())
                .andExpect(model().attribute(MAIN_TEXT, "관리자 페이지 - 판매 중"))
                .andExpect(model().attribute(ADMIN_PAGE, PRODUCT_LIST_PAGE))
                .andExpect(model().attribute(ACTIVE_SECTION, PRODUCT));

    }

    @Test
    void getNameContainingBookPageTest() throws Exception {
        List<BookProductGetResponseDto> responseDtoList = new ArrayList<>();
        for (int i = 0 ; i < 10; i ++){
            responseDtoList.add(BookProductGetResponseDto.builder().build());
        }

        Pageable pageable = PageRequest.of(0, 4);
        Page<BookProductGetResponseDto> responseDtoPage = new PageImpl<>(responseDtoList, pageable, responseDtoList.size());
        when(bookProductService.getNameContainingBookPage(any(), eq(1), eq(10), eq("sort"), eq(true), eq("test"), eq(0)))
                .thenReturn(ResponseEntity.ok(responseDtoPage));

        mockMvc.perform(get("/admin/product/book/containing")
                        .param("title", "test")
                        .param("page", String.valueOf(1))
                        .param("size", String.valueOf(10))
                        .param("sort", "sort")
                        .param("desc", String.valueOf(true))
                        .param("productState", String.valueOf(0)))
                .andExpect(status().isOk())
                .andExpect(model().attribute(MAIN_TEXT, "관리자 페이지 - 제목 : test"))
                .andExpect(model().attribute(ADMIN_PAGE, PRODUCT_LIST_PAGE))
                .andExpect(model().attribute(ACTIVE_SECTION, PRODUCT));

    }

    @Test
    void getBookPageFilterByTagTest() throws Exception {
        List<BookProductGetResponseDto> responseDtoList = new ArrayList<>();
        for (int i = 0 ; i < 10; i ++){
            responseDtoList.add(BookProductGetResponseDto.builder().build());
        }

        Pageable pageable = PageRequest.of(0, 4);
        Page<BookProductGetResponseDto> responseDtoPage = new PageImpl<>(responseDtoList, pageable, responseDtoList.size());
        Set<String> tags = Set.of("tag1","tag2");

        when(bookProductService.getBookPageFilterByTag(any(), eq(1), eq(10), eq("sort"), eq(true), eq(tags), any(), eq(0))).thenReturn(ResponseEntity.ok(responseDtoPage));

        mockMvc.perform(get("/admin/product/book/tagFilter")
                        .param("tagName", "tag1,tag2")
                        .param("page", String.valueOf(1))
                        .param("size", String.valueOf(10))
                        .param("sort", "sort")
                        .param("desc", String.valueOf(true))
                        .param("productState", String.valueOf(0)))
                .andExpect(status().isOk())
                .andExpect(model().attribute(MAIN_TEXT, "관리자 페이지 - 태그 : [tag1, tag2]"))
                .andExpect(model().attribute(ADMIN_PAGE, PRODUCT_LIST_PAGE))
                .andExpect(model().attribute(ACTIVE_SECTION, PRODUCT));
    }

    @Test
    void getBookPageFilterByCategoryTest() throws Exception {
        List<BookProductGetResponseDto> responseDtoList = new ArrayList<>();
        for (int i = 0 ; i < 10; i ++){
            responseDtoList.add(BookProductGetResponseDto.builder().build());
        }

        Pageable pageable = PageRequest.of(0, 4);
        Page<BookProductGetResponseDto> responseDtoPage = new PageImpl<>(responseDtoList, pageable, responseDtoList.size());

        Map<String, Page<BookProductGetResponseDto>> responseMap
                = Map.of("{\"productCategoryId\":1, \"categoryName\":\"test\", \"parentProductCategory\": null}", responseDtoPage);

        when(bookProductService.getBookPageFilterByCategory(any(), eq(1), eq(10), eq("sort"), eq(true), eq(1L), eq(0)))
                .thenReturn(ResponseEntity.ok(responseMap));

        mockMvc.perform(get("/admin/product/book/category/1")
                        .param("page", String.valueOf(1))
                        .param("size", String.valueOf(10))
                        .param("sort", "sort")
                        .param("desc", String.valueOf(true))
                        .param("productState", String.valueOf(0)))
                .andExpect(status().isOk())
                .andExpect(model().attribute(MAIN_TEXT, "관리자 페이지 - 카테고리 : "))
                .andExpect(model().attribute(ADMIN_PAGE, PRODUCT_LIST_PAGE))
                .andExpect(model().attribute(ACTIVE_SECTION, PRODUCT));

    }
}

