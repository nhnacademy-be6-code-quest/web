package com.nhnacademy.codequestweb.service.product;

import com.nhnacademy.codequestweb.client.product.category.CategoryClient;
import com.nhnacademy.codequestweb.config.CategoryConfig;
import com.nhnacademy.codequestweb.request.product.product_category.CategoryRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.product_category.CategoryUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.productCategory.CategoryGetResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.CategoryRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.CategoryUpdateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryClient categoryClient;
    private final CategoryConfig categoryConfig;

    public ResponseEntity<CategoryRegisterResponseDto> saveCategory(HttpHeaders headers, CategoryRegisterRequestDto categoryRegisterRequestDto) {
        ResponseEntity<CategoryRegisterResponseDto> response = categoryClient.saveCategory(headers, categoryRegisterRequestDto);
        categoryConfig.update(categoryClient.getCategoriesTree().getBody());
        return response;
    }

    public ResponseEntity<CategoryUpdateResponseDto> updateCategory(HttpHeaders headers, CategoryUpdateRequestDto categoryUpdateRequestDto) {
        ResponseEntity<CategoryUpdateResponseDto> response = categoryClient.updateCategory(headers, categoryUpdateRequestDto);
        categoryConfig.update(categoryClient.getCategoriesTree().getBody());
        return response;
    }

    public ResponseEntity<Void> deleteCategory(HttpHeaders headers, Long categoryId) {
        ResponseEntity<Void> response = categoryClient.deleteCategory(headers, categoryId);
        categoryConfig.update(categoryClient.getCategoriesTree().getBody());
        return response;
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
}
