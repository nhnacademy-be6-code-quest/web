package com.nhnacademy.codequestweb.service.product;

import com.nhnacademy.codequestweb.client.product.cart.CartClient;
import com.nhnacademy.codequestweb.request.product.cart.CartRequestDto;
import com.nhnacademy.codequestweb.response.product.common.CartGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.SaveCartResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
    private final CartClient cartClient;

    public ResponseEntity<List<CartRequestDto>> restoreClientCartList(HttpHeaders headers){
        return cartClient.restoreClientCartList(headers);
    }

    public ResponseEntity<List<CartGetResponseDto>> getClientCartList(HttpHeaders headers) {
        return cartClient.getClientCartList(headers);
    }

    public ResponseEntity<List<CartGetResponseDto>> getGuestCartList(List<CartRequestDto> cartRequestDtoList) {
        return cartClient.getGuestCartList(cartRequestDtoList);
    }

    public ResponseEntity<SaveCartResponseDto> addClientCartItem(HttpHeaders headers, CartRequestDto requestDto) {
        return cartClient.addClientCartItem(headers, requestDto);
    }

    public ResponseEntity<SaveCartResponseDto> addGuestCartItem(CartRequestDto requestDto) {
        return cartClient.addGuestCartItem(requestDto);
    }

    public ResponseEntity<SaveCartResponseDto> updateClientCartItem(HttpHeaders headers, CartRequestDto cartRequestDto){
        return cartClient.updateClientCartItem(headers, cartRequestDto);
    }

    public ResponseEntity<SaveCartResponseDto> updateGuestCartItem(CartRequestDto cartRequestDto){
        return cartClient.updateGuestCartItem(cartRequestDto);
    }

    public ResponseEntity<Void> deleteClientCartItem(HttpHeaders headers, Long productId){
        return cartClient.deleteClientCartItem(headers, productId);
    }


    public ResponseEntity<Void> clearClientAllCart(HttpHeaders headers){
        return cartClient.clearClientAllCart(headers);
    }
}
