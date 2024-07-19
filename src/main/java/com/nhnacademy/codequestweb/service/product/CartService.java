package com.nhnacademy.codequestweb.service.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.codequestweb.client.product.cart.CartClient;
import com.nhnacademy.codequestweb.request.product.cart.CartRequestDto;
import com.nhnacademy.codequestweb.response.product.common.CartGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.SaveCartResponseDto;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartClient cartClient;
    private final List<CartRequestDto> emptyList = new ArrayList<>();

    private final ObjectMapper objectMapper;


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
    };

    public ResponseEntity<SaveCartResponseDto> updateGuestCartItem(CartRequestDto cartRequestDto){
        return cartClient.updateGuestCartItem(cartRequestDto);
    }

    public ResponseEntity<Void> deleteClientCartItem(HttpHeaders headers, Long productId){
        return cartClient.deleteClientCartItem(headers, productId);
    }

    public void clearCartByCheckout(HttpServletResponse resp, List<Long> productIdList, Model model) throws Exception {
        List<CartRequestDto> cartListOfCookie = emptyList;

        try {
            cartListOfCookie = (List<CartRequestDto>) model.getAttribute("cart");
        }catch (ClassCastException | NullPointerException ignore){
        }
        List<CartRequestDto> cartRequestDtoToDelete = new ArrayList<>();

        for (CartRequestDto cartItem : cartListOfCookie) {
            if (productIdList.contains(cartItem.productId())) {
                cartRequestDtoToDelete.add(cartItem);
            }
        }
        
        cartListOfCookie.removeAll(cartRequestDtoToDelete);

        CookieUtils.deleteCookieValue(resp, "cart");
        CookieUtils.setCartCookieValue(cartListOfCookie, objectMapper, resp);
    }

    public ResponseEntity<Void> clearClientAllCart(HttpHeaders headers){
        return cartClient.clearClientAllCart(headers);
    }

}
