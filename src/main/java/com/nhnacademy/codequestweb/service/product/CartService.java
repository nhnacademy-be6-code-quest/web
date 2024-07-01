package com.nhnacademy.codequestweb.service.product;

import com.nhnacademy.codequestweb.client.product.cart.CartClient;
import com.nhnacademy.codequestweb.response.product.common.SaveCartResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartClient cartClient;

    public SaveCartResponseDto addCartItem(HttpHeaders headers, Long productId, Integer quantity) {

        return cartClient.saveCartItem(headers, )
    }
}
