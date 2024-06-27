package com.nhnacademy.codequestweb.client.product.category;

import com.nhnacademy.codequestweb.request.product.PageRequestDto;
import com.nhnacademy.codequestweb.request.product.productCategory.CategoryRegisterRequestDto;
import com.nhnacademy.codequestweb.response.product.productCategory.CategoryGetResponseDto;
import com.nhnacademy.codequestweb.response.product.productCategory.CategoryRegisterResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "category", url = "http://localhost:8001/api")
public interface CategoryClient {

    @PostMapping("/product/admin/category/register")
    ResponseEntity<CategoryRegisterResponseDto> saveCategory(@RequestHeader HttpHeaders headers, @RequestBody CategoryRegisterRequestDto categoryRegisterRequestDto);

    @GetMapping("/product/categories/all")
    ResponseEntity<Page<CategoryGetResponseDto>> getAllCategories(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "desc", required = false) Boolean desc, @RequestParam(name = "sort", required = false) String sort);

    @GetMapping("/product/categories/containing")
    ResponseEntity<Page<CategoryGetResponseDto>> getNameContainingCategories(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "desc", required = false) Boolean desc, @RequestParam(name = "sort", required = false) String sort, @RequestParam("categoryName") String categoryName);

    @GetMapping("/product/categories/sub")
    ResponseEntity<Page<CategoryGetResponseDto>> getSubCategories(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "desc", required = false) Boolean desc, @RequestParam(name = "sort", required = false) String sort, @RequestParam("categoryName") String categoryName);

}
