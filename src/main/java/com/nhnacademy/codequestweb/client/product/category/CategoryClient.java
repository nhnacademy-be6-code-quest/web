package com.nhnacademy.codequestweb.client.product.category;

import com.nhnacademy.codequestweb.request.product.product_category.CategoryRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.product_category.CategoryUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.product_category.CategoryGetResponseDto;
import com.nhnacademy.codequestweb.response.product.product_category.CategoryNodeResponseDto;
import com.nhnacademy.codequestweb.response.product.product_category.CategoryRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.product_category.CategoryUpdateResponseDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "category", url = "http://localhost:8001/api")
public interface CategoryClient {

    @PostMapping("/product/admin/category/register")
    ResponseEntity<CategoryRegisterResponseDto> saveCategory(
            @RequestHeader HttpHeaders headers,
            @Valid @RequestBody CategoryRegisterRequestDto categoryRegisterRequestDto);

    @PutMapping("/product/admin/category/update")
    ResponseEntity<CategoryUpdateResponseDto> updateCategory(
            @RequestHeader HttpHeaders headers,
            @Valid @RequestBody CategoryUpdateRequestDto categoryUpdateRequestDto);

    @DeleteMapping("/product/admin/category/delete/{categoryId}")
    ResponseEntity<Void> deleteCategory(
            @RequestHeader HttpHeaders headers,
            @PathVariable("categoryId") Long categoryId);

    @GetMapping("/product/admin/categories/all")
    ResponseEntity<Page<CategoryGetResponseDto>> getAllCategories(
            @RequestHeader HttpHeaders headers,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "desc", required = false) Boolean desc,
            @RequestParam(name = "sort", required = false) String sort);

    @GetMapping("/product/admin/categories/containing")
    ResponseEntity<Page<CategoryGetResponseDto>> getNameContainingCategories(
            @RequestHeader HttpHeaders headers,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "desc", required = false) Boolean desc,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam("categoryName") String categoryName);

    @GetMapping("/product/admin/categories/{categoryId}/sub")
    ResponseEntity<Page<CategoryGetResponseDto>> getSubCategories(
            @RequestHeader HttpHeaders headers,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "desc", required = false) Boolean desc,
            @RequestParam(name = "sort", required = false) String sort,
            @PathVariable("categoryId") Long categoryId);


    @GetMapping("/product/categories/tree")
    ResponseEntity<CategoryNodeResponseDto> getCategoriesTree();
}
