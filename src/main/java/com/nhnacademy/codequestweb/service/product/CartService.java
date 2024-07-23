package com.nhnacademy.codequestweb.service.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.codequestweb.client.product.cart.CartClient;
import com.nhnacademy.codequestweb.exception.product.CartProcessingException;
import com.nhnacademy.codequestweb.request.product.cart.CartRequestDto;
import com.nhnacademy.codequestweb.response.product.common.CartGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.SaveCartResponseDto;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import com.nhnacademy.codequestweb.utils.SecretKeyUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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

    private final ObjectMapper objectMapper;

    private static final TypeReference<List<CartRequestDto>> TYPE_REFERENCE = new TypeReference<List<CartRequestDto>>() {};

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


    //이거 사실 카트 서비스 상관 없이 그냥 paymentController 에서 바로 이 내용 실행해도 아무 문제 없을 듯..? 그리고 쿠키값 변조라 컨트롤러 레이어에서 하는 게 맞을 것도 같고.
    public void clearCartByCheckout(HttpServletRequest req, HttpServletResponse resp, List<Long> productIdList) throws Exception{
        String encryptedCart = CookieUtils.getCookieValue(req, "cart");
        if (encryptedCart != null) {
            try {
                String cartJson = SecretKeyUtils.decrypt(encryptedCart, SecretKeyUtils.getSecretKey());
                List<CartRequestDto> cartListOfCookie = objectMapper.readValue(cartJson, TYPE_REFERENCE);
                List<CartRequestDto> cartRequestDtoToDelete = new ArrayList<>();

                for (CartRequestDto cartItem : cartListOfCookie) {
                    if (productIdList.contains(cartItem.productId())) {
                        cartRequestDtoToDelete.add(cartItem);
                    }
                }
                cartListOfCookie.removeAll(cartRequestDtoToDelete);

                CookieUtils.deleteCookieValue(resp, "cart");
                CookieUtils.setCartCookieValue(cartListOfCookie, objectMapper, resp);
            } catch (Exception e) {
                throw new CartProcessingException("error occurred while checkout cart. message : "+ e.getMessage());
            }
        }else{
            log.warn("cart controller advice may have some problem with processing deleted cookie. check the log with cart controller advice class");
        }
    }
}
