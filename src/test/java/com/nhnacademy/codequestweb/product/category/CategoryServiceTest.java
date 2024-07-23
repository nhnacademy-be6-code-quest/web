package com.nhnacademy.codequestweb.product.category;

import com.nhnacademy.codequestweb.client.product.category.CategoryClient;
import com.nhnacademy.codequestweb.config.CategoryConfig;
import com.nhnacademy.codequestweb.request.product.product_category.CategoryRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.product_category.CategoryUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.productCategory.CategoryGetResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.CategoryRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.CategoryUpdateResponseDto;
import com.nhnacademy.codequestweb.service.product.CategoryService;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryClient categoryClient;

    @Mock
    private CategoryConfig categoryConfig;

    private PageRequest pageRequest;

    private HttpHeaders headers;

    private List<CategoryGetResponseDto> categoryGetResponseDtoList;

    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

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
    void saveCategoryTest() {
        CategoryRegisterRequestDto requestDto = CategoryRegisterRequestDto.builder().build();

        CategoryRegisterResponseDto responseDto = CategoryRegisterResponseDto.builder().build();

        when(categoryClient.saveCategory(any(), eq(requestDto))).thenReturn(ResponseEntity.ok(responseDto));
        when(categoryClient.getCategoriesTree()).thenReturn(ResponseEntity.ok(null));

        ResponseEntity<CategoryRegisterResponseDto> realResponseDto = categoryService.saveCategory(headers, requestDto);

        assertNotNull(realResponseDto);
        assertEquals(responseDto, realResponseDto.getBody());
        verify(categoryClient, times(1)).saveCategory(any(), eq(requestDto));
        verify(categoryConfig, times(1)).update(any());
    }

    @Test
    void updateCategoryTest() {
        CategoryUpdateRequestDto requestDto = CategoryUpdateRequestDto.builder().build();

        CategoryUpdateResponseDto responseDto = CategoryUpdateResponseDto.builder().build();

        when(categoryClient.updateCategory(any(), eq(requestDto))).thenReturn(ResponseEntity.ok(responseDto));
        when(categoryClient.getCategoriesTree()).thenReturn(ResponseEntity.ok(null));

        ResponseEntity<CategoryUpdateResponseDto> realResponseDto = categoryService.updateCategory(headers, requestDto);

        assertNotNull(realResponseDto);
        assertEquals(responseDto, realResponseDto.getBody());
        verify(categoryClient, times(1)).updateCategory(any(), eq(requestDto));
        verify(categoryConfig, times(1)).update(any());
    }

    @Test
    void deleteCategoryTest() {
        when(categoryClient.deleteCategory(any(), eq(1L))).thenReturn(ResponseEntity.ok(null));
        when(categoryClient.getCategoriesTree()).thenReturn(ResponseEntity.ok(null));

        ResponseEntity<Void> realResponseDto = categoryService.deleteCategory(headers, 1L);

        assertNotNull(realResponseDto);
        assertEquals(200, realResponseDto.getStatusCode().value());
        verify(categoryClient, times(1)).deleteCategory(any(), eq(1L));
        verify(categoryConfig, times(1)).update(any());
    }

    @Test
    void getAllCategoriesTest(){
        Page<CategoryGetResponseDto> responseDtoPage = new PageImpl<>(categoryGetResponseDtoList, pageRequest, 2);
        when(categoryClient.getAllCategories(null, null, null)).thenReturn(ResponseEntity.ok(responseDtoPage));

        ResponseEntity<Page<CategoryGetResponseDto>> realResponseDtoPage = categoryService.getCategories(null, null, null);

        assertNotNull(realResponseDtoPage);
        assertEquals(responseDtoPage, realResponseDtoPage.getBody());
        assertEquals(2, Objects.requireNonNull(realResponseDtoPage.getBody()).getTotalElements());

        verify(categoryClient, times(1)).getAllCategories(null, null, null);
    }

    @Test
    void getNameContainingCategoriesTest(){
        Page<CategoryGetResponseDto> responseDtoPage = new PageImpl<>(categoryGetResponseDtoList, pageRequest, 2);
        when(categoryClient.getNameContainingCategories(null, null, null, "test")).thenReturn(ResponseEntity.ok(responseDtoPage));

        ResponseEntity<Page<CategoryGetResponseDto>> realResponseDtoPage = categoryService.getNameContainingCategories(null, null, null, "test");

        assertNotNull(realResponseDtoPage);
        assertEquals(responseDtoPage, realResponseDtoPage.getBody());
        assertEquals(2, Objects.requireNonNull(realResponseDtoPage.getBody()).getTotalElements());

        verify(categoryClient, times(1)).getNameContainingCategories(null, null, null, "test");
    }


    @Test
    void getSubCategoriesTest(){
        Page<CategoryGetResponseDto> responseDtoPage = new PageImpl<>(categoryGetResponseDtoList, pageRequest, 2);
        when(categoryClient.getSubCategories(null, null, null, 1L)).thenReturn(ResponseEntity.ok(responseDtoPage));

        ResponseEntity<Page<CategoryGetResponseDto>> realResponseDtoPage = categoryService.getSubCategories(null, null, null, 1L);

        assertNotNull(realResponseDtoPage);
        assertEquals(responseDtoPage, realResponseDtoPage.getBody());
        assertEquals(2, Objects.requireNonNull(realResponseDtoPage.getBody()).getTotalElements());

        verify(categoryClient, times(1)).getSubCategories(null, null, null, 1L);
    }
}
