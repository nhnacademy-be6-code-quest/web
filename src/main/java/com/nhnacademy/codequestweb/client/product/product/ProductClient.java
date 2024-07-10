package com.nhnacademy.codequestweb.client.product.product;

import com.nhnacademy.codequestweb.request.product.ProductLikeRequestDto;
import com.nhnacademy.codequestweb.request.product.ProductStateUpdateRequestDto;
import com.nhnacademy.codequestweb.request.product.common.InventoryDecreaseRequestDto;
import com.nhnacademy.codequestweb.request.product.common.InventoryIncreaseRequestDto;
import com.nhnacademy.codequestweb.request.product.common.InventorySetRequestDto;
import com.nhnacademy.codequestweb.response.product.common.ProductUpdateResponseDto;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product", url = "http://localhost:8001")
public interface ProductClient {
    @PutMapping("/api/product/admin/update/state")
    ResponseEntity<ProductUpdateResponseDto> updateBookState(
            @RequestHeader HttpHeaders headers,
            @RequestBody ProductStateUpdateRequestDto productStateUpdateRequestDto);

    @PostMapping("/api/product/client/like")
    ResponseEntity<Void> saveBookProductLike(
            @RequestHeader HttpHeaders headers,
            @RequestBody @Valid ProductLikeRequestDto productLikeRequestDto);

    @DeleteMapping("/api/product/client/unlike")
    ResponseEntity<Void> deleteBookProductLike(
            @RequestHeader HttpHeaders httpHeaders,
            @RequestParam("productId") Long productId);

    @PutMapping("/api/product/inventory/decrease")
    ResponseEntity<Void> decreaseProductInventory(
            @RequestBody @Valid List<InventoryDecreaseRequestDto> inventoryDecreaseRequestDtoList);


    @PutMapping("/api/product/inventory/increase")
    ResponseEntity<Void> increaseProductInventory(
            @RequestBody @Valid List<InventoryIncreaseRequestDto> inventoryIncreaseRequestDtoList);

    @PutMapping("/api/product/admin/inventory/set")
    ResponseEntity<Void> setProductInventory(
            @RequestHeader HttpHeaders headers,
            @RequestBody @Valid InventorySetRequestDto inventorySetRequestDto);
}
