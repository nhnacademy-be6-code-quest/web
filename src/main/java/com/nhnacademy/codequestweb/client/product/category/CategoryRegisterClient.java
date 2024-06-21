package com.nhnacademy.codequestweb.client.product.category;

import com.nhnacademy.codequestweb.request.bookProduct.CategoryRegisterRequestDto;
import com.nhnacademy.codequestweb.response.product.Category;
import com.nhnacademy.codequestweb.response.product.CategoryGetResponseDto;
import com.nhnacademy.codequestweb.response.product.CategoryRegisterResponseDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ADMIN-SERVICE2", url = "http://localhost:8001/api/admin/category")
public interface CategoryRegisterClient {

    @PostMapping("/register")
    ResponseEntity<CategoryRegisterResponseDto> saveCategory(@RequestBody CategoryRegisterRequestDto categoryRegisterRequestDto);

    @GetMapping("/list/all")
    ResponseEntity<List<CategoryGetResponseDto>> getAllCategories();
}
