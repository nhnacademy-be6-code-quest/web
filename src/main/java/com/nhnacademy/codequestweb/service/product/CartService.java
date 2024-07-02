package com.nhnacademy.codequestweb.service.product;

import com.nhnacademy.codequestweb.client.product.cart.CartClient;
import com.nhnacademy.codequestweb.request.product.cart.CartRequestDto;
import com.nhnacademy.codequestweb.response.product.common.CartGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.SaveCartResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartClient cartClient;

    public ResponseEntity<List<CartGetResponseDto>> getClientCartList(HttpHeaders headers) {
        return cartClient.getClientCartList(headers);
    }

    public ResponseEntity<List<CartGetResponseDto>> getGuestCartList(List<CartRequestDto> cartRequestDtoList) {
        return cartClient.getGuestCartList(cartRequestDtoList);
    }

    public ResponseEntity<SaveCartResponseDto> addCartItem(HttpHeaders headers, CartRequestDto requestDto) {
        return cartClient.saveCartItem(headers, requestDto);
    }
}
