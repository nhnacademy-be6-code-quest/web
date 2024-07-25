package com.nhnacademy.codequestweb.product.category;

import com.nhnacademy.codequestweb.controller.product.admin.AdminCategoryController;
import com.nhnacademy.codequestweb.request.product.product_category.CategoryRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.product_category.CategoryUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.product_category.CategoryGetResponseDto;
import com.nhnacademy.codequestweb.response.product.product_category.CategoryRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.product_category.CategoryUpdateResponseDto;
import com.nhnacademy.codequestweb.service.product.CategoryService;
import feign.FeignException;
import feign.Request;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class AdminCategoryControllerTest {
    @InjectMocks
    private AdminCategoryController adminCategoryController;

    @Mock
    private CategoryService categoryService;

    private MockMvc mockMvc;

    private PageRequest pageRequest;

    private List<CategoryGetResponseDto> categoryGetResponseDtoList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminCategoryController).build();

        pageRequest = PageRequest.of(0, 10);

        CategoryGetResponseDto categoryGetResponseDto1 = CategoryGetResponseDto.builder()
                .categoryName("test category1")
                .build();

        CategoryGetResponseDto categoryGetResponseDto2 = CategoryGetResponseDto.builder()
                .categoryName("test category2")
                .build();

        categoryGetResponseDtoList = Arrays.asList(
                categoryGetResponseDto1, categoryGetResponseDto2
        );
    }

    @Test
    void getAllCategoriesTest1() throws Exception {

        Page<CategoryGetResponseDto> responseDtoPage = new PageImpl<>(categoryGetResponseDtoList, pageRequest,2);
        when(categoryService.getCategories(null, null, null)).thenReturn(ResponseEntity.ok(responseDtoPage));

        mockMvc.perform(get("/admin/categories"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalCount", 2L))
                .andExpect(model().attribute("totalPage", 1))
                .andExpect(model().attributeDoesNotExist("empty"))
        ;
    }

    @Test
    void getAllCategoriesTest2() throws Exception {

        Page<CategoryGetResponseDto> responseDtoPage = new PageImpl<>(new ArrayList<>(), pageRequest,0);
        when(categoryService.getCategories(null, null, null)).thenReturn(ResponseEntity.ok(responseDtoPage));

        mockMvc.perform(get("/admin/categories"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalCount", 0L))
                .andExpect(model().attribute("totalPage", 0))
                .andExpect(model().attribute("empty", true))
        ;
    }

    @Test
    void getAllCategoriesTest3() throws Exception {

        when(categoryService.getCategories(null, null, null)).thenThrow(FeignException.class);

        mockMvc.perform(get("/admin/categories"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/client/0"))
                .andExpect(flash().attribute("alterMessage", "failed to get categories."));
    }

    @Test
    void getNameContainingCategoriesTest1() throws Exception {

        Page<CategoryGetResponseDto> responseDtoPage = new PageImpl<>(categoryGetResponseDtoList, pageRequest,2);
        when(categoryService.getNameContainingCategories(null, null, null, "test")).thenReturn(ResponseEntity.ok(responseDtoPage));

        mockMvc.perform(get("/admin/categories/containing")
                        .param("categoryName","test"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalCount", 2L))
                .andExpect(model().attribute("totalPage", 1))
                .andExpect(model().attributeDoesNotExist("empty"))
        ;

        verify(categoryService, times(1)).getNameContainingCategories(null, null, null, "test");
    }

    @Test
    void getNameContainingCategoriesTest2() throws Exception {

        Page<CategoryGetResponseDto> responseDtoPage = new PageImpl<>(new ArrayList<>(), pageRequest,0);
        when(categoryService.getNameContainingCategories(null, null, null, "test")).thenReturn(ResponseEntity.ok(responseDtoPage));

        mockMvc.perform(get("/admin/categories/containing")
                        .param("categoryName","test"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalCount", 0L))
                .andExpect(model().attribute("totalPage", 0))
                .andExpect(model().attribute("empty", true))
        ;

        verify(categoryService, times(1)).getNameContainingCategories(null, null, null, "test");
    }

    @Test
    void getNameContainingCategoriesTest3() throws Exception {

        when(categoryService.getNameContainingCategories(null, null, null, "test")).thenThrow(FeignException.class);

        mockMvc.perform(get("/admin/categories/containing")
                        .param("categoryName","test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/client/0"))
                .andExpect(flash().attribute("alterMessage", "failed to get categories."));

        verify(categoryService, times(1)).getNameContainingCategories(null, null, null, "test");
    }

    @Test
    void getSubCategoriesTest1() throws Exception {

        Page<CategoryGetResponseDto> responseDtoPage = new PageImpl<>(categoryGetResponseDtoList, pageRequest,2);
        when(categoryService.getSubCategories(null, null, null, 1L)).thenReturn(ResponseEntity.ok(responseDtoPage));

        mockMvc.perform(get("/admin/categories/1/sub")
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalCount", 2L))
                .andExpect(model().attribute("totalPage", 1))
                .andExpect(model().attributeDoesNotExist("empty"))
        ;

        verify(categoryService, times(1)).getSubCategories(null, null, null, 1L);
    }

    @Test
    void getSubCategoriesTest2() throws Exception {

        Page<CategoryGetResponseDto> responseDtoPage = new PageImpl<>(new ArrayList<>(), pageRequest,0);
        when(categoryService.getSubCategories(null, null, null, 1L)).thenReturn(ResponseEntity.ok(responseDtoPage));

        mockMvc.perform(get("/admin/categories/1/sub")
                )
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalCount", 0L))
                .andExpect(model().attribute("totalPage", 0))
                .andExpect(model().attribute("empty", true))
        ;

        verify(categoryService, times(1)).getSubCategories(null, null, null, 1L);
    }

    @Test
    void getSubCategoriesTest3() throws Exception {

        when(categoryService.getSubCategories(null, null, null, 1L)).thenThrow(FeignException.class);

        mockMvc.perform(get("/admin/categories/1/sub")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/client/0"))
                .andExpect(flash().attribute("alterMessage", "failed to get categories."));

        verify(categoryService, times(1)).getSubCategories(null, null, null, 1L);
    }

    @Test
    void saveCategoryTest1() throws Exception {
        CategoryRegisterRequestDto requestDto = CategoryRegisterRequestDto.builder()
                .categoryName("test category1")
                .build();

        CategoryRegisterResponseDto responseDto = CategoryRegisterResponseDto.builder()
                .build();

        when(categoryService.saveCategory(any(), eq(requestDto))).thenReturn(ResponseEntity.ok(responseDto));

        mockMvc.perform(post("/admin/categories/register")
                        .param("categoryName","test category1"))
                .andExpect(status().isOk());
    }

    @Test
    void saveCategoryTest2() throws Exception {
        CategoryRegisterRequestDto requestDto = CategoryRegisterRequestDto.builder()
                .categoryName("test category1")
                .build();

        when(categoryService.saveCategory(any(), eq(requestDto))).thenThrow(FeignException.class);

        mockMvc.perform(post("/admin/categories/register")
                        .param("categoryName","test category1"))
                .andExpect(status().isInternalServerError());
    }


    @Test
    void saveCategoryTest3() throws Exception {
        CategoryRegisterRequestDto requestDto = CategoryRegisterRequestDto.builder()
                .categoryName("test category1")
                .build();

        Request req = mock(Request.class);
        when(categoryService.saveCategory(any(), eq(requestDto))).thenThrow(new FeignException.BadRequest(null, req,null, null));

        mockMvc.perform(post("/admin/categories/register")
                        .param("categoryName","test category1"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void updateCategoryTest1() throws Exception {
        CategoryUpdateRequestDto requestDto = CategoryUpdateRequestDto.builder()
                .currentCategoryName("current category1")
                .newCategoryName("new category1")
                .build();

        CategoryUpdateResponseDto responseDto = CategoryUpdateResponseDto.builder()
                .build();

        when(categoryService.updateCategory(any(), eq(requestDto))).thenReturn(ResponseEntity.ok(responseDto));

        mockMvc.perform(put("/admin/categories/update")
                        .param("currentCategoryName","current category1")
                        .param("newCategoryName","new category1"))
                .andExpect(status().isOk());
    }

    @Test
    void updateCategoryTest2() throws Exception {
        CategoryUpdateRequestDto requestDto = CategoryUpdateRequestDto.builder()
                .currentCategoryName("current category1")
                .newCategoryName("new category1")
                .build();

        when(categoryService.updateCategory(any(), eq(requestDto))).thenThrow(FeignException.class);

        mockMvc.perform(put("/admin/categories/update")
                        .param("currentCategoryName","current category1")
                        .param("newCategoryName","new category1"))
                .andExpect(status().isInternalServerError());
    }


    @Test
    void updateCategoryTest3() throws Exception {
        CategoryUpdateRequestDto requestDto = CategoryUpdateRequestDto.builder()
                .currentCategoryName("current category1")
                .newCategoryName("new category1")
                .build();

        Request req = mock(Request.class);
        when(categoryService.updateCategory(any(), eq(requestDto))).thenThrow(new FeignException.BadRequest(null, req,null, null));

        mockMvc.perform(put("/admin/categories/update")
                        .param("currentCategoryName","current category1")
                        .param("newCategoryName","new category1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteCategoryTest1() throws Exception {
        when(categoryService.deleteCategory(any(), eq(1L))).thenReturn(ResponseEntity.ok(null));

        mockMvc.perform(delete("/admin/categories/delete/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCategoryTest2() throws Exception {
        when(categoryService.deleteCategory(any(), eq(1L))).thenThrow(FeignException.class);

        mockMvc.perform(delete("/admin/categories/delete/1"))
                .andExpect(status().isInternalServerError());
    }


    @Test
    void deleteCategoryTest3() throws Exception {
        Request req = mock(Request.class);
        when(categoryService.deleteCategory(any(), eq(1L))).thenThrow(new FeignException.BadRequest(null, req,null, null));

        mockMvc.perform(delete("/admin/categories/delete/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllCategoryModalTest1() throws Exception {
        Page<CategoryGetResponseDto> responseDtoPage = new PageImpl<>(categoryGetResponseDtoList, pageRequest,2);
        when(categoryService.getCategories(null, null, null)).thenReturn(ResponseEntity.ok(responseDtoPage));

        mockMvc.perform(get("/categories/all"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("register",  false))
                .andExpect(model().attribute("url",  "/categories/all?page="))
        ;
    }

    @Test
    void getAllCategoryModalTest2() throws Exception {
        when(categoryService.getCategories(null, null, null)).thenThrow(FeignException.class);

        mockMvc.perform(get("/categories/all"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("url", "register"))
                .andExpect(view().name("view/product/window.close"));
        ;
    }


    @Test
    void getNameContainingCategoryModalTest1() throws Exception {
        Page<CategoryGetResponseDto> responseDtoPage = new PageImpl<>(categoryGetResponseDtoList, pageRequest,2);
        when(categoryService.getNameContainingCategories(null, null, null, "test")).thenReturn(ResponseEntity.ok(responseDtoPage));

        mockMvc.perform(get("/categories/containing")
                        .param("categoryName","test"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("register",  false))
                .andExpect(model().attribute("url",  "/categories/containing?categoryName=test&page="))
        ;
    }

    @Test
    void getNameContainingCategoryModalTest2() throws Exception {
        when(categoryService.getNameContainingCategories(null, null, null, "test")).thenThrow(FeignException.class);

        mockMvc.perform(get("/categories/containing")
                        .param("categoryName","test"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("url", "register"))
                .andExpect(view().name("view/product/window.close"));
        ;
    }

    @Test
    void getSubCategoryModalTest1() throws Exception {
        Page<CategoryGetResponseDto> responseDtoPage = new PageImpl<>(categoryGetResponseDtoList, pageRequest,2);
        when(categoryService.getSubCategories(null, null, null, 1L)).thenReturn(ResponseEntity.ok(responseDtoPage));

        mockMvc.perform(get("/categories/1/sub"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("register",  true))
                .andExpect(model().attribute("url",  "/categories/1/sub?page="))
        ;
    }

    @Test
    void getSubCategoryModalTest2() throws Exception {
        when(categoryService.getSubCategories(null, null, null, 1L)).thenThrow(FeignException.class);

        mockMvc.perform(get("/categories/1/sub"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("url", "register"))
                .andExpect(view().name("view/product/window.close"));
        ;
    }

}
