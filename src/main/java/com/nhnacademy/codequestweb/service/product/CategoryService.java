package com.nhnacademy.codequestweb.service.product;

import com.nhnacademy.codequestweb.client.product.category.CategoryClient;
import com.nhnacademy.codequestweb.request.product.productCategory.CategoryRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.productCategory.CategoryUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.productCategory.CategoryGetResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.CategoryRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.CategoryUpdateResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryClient categoryClient;

    public ResponseEntity<CategoryRegisterResponseDto> saveCategory(HttpHeaders headers, CategoryRegisterRequestDto categoryRegisterRequestDto) {
        return categoryClient.saveCategory(headers, categoryRegisterRequestDto);
    }

    public ResponseEntity<CategoryUpdateResponseDto> updateCategory(HttpHeaders headers, CategoryUpdateRequestDto categoryUpdateRequestDto) {
        return categoryClient.updateCategory(headers, categoryUpdateRequestDto);
    }

    public ResponseEntity<Page<CategoryGetResponseDto>> getCategories(Integer page, Boolean desc, String sort) {
        return categoryClient.getAllCategories(page, desc, sort);
    }

    public ResponseEntity<Page<CategoryGetResponseDto>> getNameContainingCategories(Integer page, Boolean desc, String sort, String categoryName){
        return categoryClient.getNameContainingCategories(page, desc, sort, categoryName);
    }

    public ResponseEntity<Page<CategoryGetResponseDto>> getSubCategories(Integer page, Boolean desc, String sort, Long categoryId){
        return categoryClient.getSubCategories(page, desc, sort, categoryId);
    }

    public ResponseEntity<List<CategoryGetResponseDto>> getAllSubCategories(Long categoryId){
        return categoryClient.getAllSubCategories(categoryId);
    }
}
