package com.nhnacademy.codequestweb.service.product;

import com.nhnacademy.codequestweb.client.product.product.ProductClient;
import com.nhnacademy.codequestweb.request.product.ProductLikeRequestDto;
import com.nhnacademy.codequestweb.request.product.ProductStateUpdateRequestDto;
import com.nhnacademy.codequestweb.request.product.common.InventorySetRequestDto;
import com.nhnacademy.codequestweb.response.product.common.ProductUpdateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductClient productClient;

    public ResponseEntity<Void> saveProductLike(HttpHeaders headers, ProductLikeRequestDto productLikeRequestDto) {
        return productClient.saveBookProductLike(headers, productLikeRequestDto);
    }


    public ResponseEntity<Void> deleteProductLike(HttpHeaders headers, Long productId) {
        return productClient.deleteBookProductLike(headers, productId);
    }


    public ResponseEntity<ProductUpdateResponseDto> updateProductState(
            HttpHeaders headers,
            ProductStateUpdateRequestDto productStateUpdateRequestDto){
        return productClient.updateBookState(headers, productStateUpdateRequestDto);
    }


    public ResponseEntity<Void> setProductInventory(
            HttpHeaders headers,
            InventorySetRequestDto requestDto) {
        return productClient.setProductInventory(headers, requestDto);
    }

}
