package com.nhnacademy.codequestweb.service.admin;

import com.nhnacademy.codequestweb.client.product.category.CategoryRegisterClient;
import com.nhnacademy.codequestweb.request.bookProduct.CategoryRegisterRequestDto;
import com.nhnacademy.codequestweb.response.product.CategoryRegisterResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryRegisterService {
    private final CategoryRegisterClient categoryRegisterClient;

    public ResponseEntity<CategoryRegisterResponseDto> saveCategory(CategoryRegisterRequestDto categoryRegisterRequestDto) {
        return categoryRegisterClient.saveCategory(categoryRegisterRequestDto);
    }
}
