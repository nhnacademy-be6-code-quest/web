package com.nhnacademy.codequestweb.service.product;

import com.nhnacademy.codequestweb.client.product.category.CategoryClient;
import com.nhnacademy.codequestweb.request.product.PageRequestDto;
import com.nhnacademy.codequestweb.request.product.productCategory.CategoryRegisterRequestDto;
import com.nhnacademy.codequestweb.response.product.productCategory.CategoryGetResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.CategoryRegisterResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryClient categoryClient;

    public ResponseEntity<CategoryRegisterResponseDto> saveCategory(HttpHeaders headers, CategoryRegisterRequestDto categoryRegisterRequestDto) {
        return categoryClient.saveCategory(headers, categoryRegisterRequestDto);
    }

    public ResponseEntity<Page<CategoryGetResponseDto>> getCategories(Integer page, Boolean desc, String sort) {
        return categoryClient.getAllCategories(page, desc, sort);
    }

    public ResponseEntity<Page<CategoryGetResponseDto>> getNameContainingCategories(Integer page, Boolean desc, String sort, String categoryName){
        return categoryClient.getNameContainingCategories(page, desc, sort, categoryName);
    }

    public ResponseEntity<Page<CategoryGetResponseDto>> getSubCategories(Integer page, Boolean desc, String sort, String categoryName){
        return categoryClient.getSubCategories(page, desc, sort, categoryName);
    }

}
