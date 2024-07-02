package com.nhnacademy.codequestweb.client.product.cart;

import com.nhnacademy.codequestweb.request.product.cart.CartRequestDto;
import com.nhnacademy.codequestweb.response.product.common.CartGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.SaveCartResponseDto;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "cart", url = "http://localhost:8001/api/product")
public interface CartClient {

    @GetMapping("/client/cart")
    ResponseEntity<List<CartGetResponseDto>> getClientCartList(
            @RequestHeader HttpHeaders headers);

    @PostMapping("/guest/cart")
    ResponseEntity<List<CartGetResponseDto>> getGuestCartList(
            @RequestBody List<@Valid CartRequestDto> cartRequestDtoList
    );

    @PostMapping("/client/cart/add")
    ResponseEntity<SaveCartResponseDto> saveCartItem(
            @RequestHeader HttpHeaders headers,
            @RequestBody @Valid CartRequestDto cartRequestDto);

    @PutMapping("/client/cart/update")
    ResponseEntity<SaveCartResponseDto> updateCartItem(
            @RequestHeader HttpHeaders headers,
            @RequestBody @Valid CartRequestDto cartRequestDto);

    @DeleteMapping("/client/cart/items/{productId}")
    ResponseEntity<Void> deleteCartItem(
            @RequestHeader HttpHeaders headers,
            @PathVariable Long productId);

    @DeleteMapping("/client/cart/all")
    ResponseEntity<Void> clearAllCart(
            @RequestHeader HttpHeaders headers);
}
