package com.nhnacademy.codequestweb.controller.product.permitAll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.codequestweb.request.product.cart.CartRequestDto;
import com.nhnacademy.codequestweb.response.product.common.CartGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.SaveCartResponseDto;
import com.nhnacademy.codequestweb.service.product.CartService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.LinkedHashMap;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    private final ObjectMapper objectMapper;

    private final List<CartRequestDto> emptyList = new ArrayList<>();

    private Map<String, Boolean> makeJsonCartMap(List<CartGetResponseDto> cartList) {
        return cartList.stream()
                .collect(Collectors.toMap(
                        cart -> {
                            try {
                                return objectMapper.writeValueAsString(cart);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        cart -> cart.productInventory() >= cart.productQuantityOfCart() && cart.productState() == 0,
                        (oldValue, newValue) -> oldValue, // 병합 함수 (충돌 해결)
                        LinkedHashMap::new // 맵 공급자
                ));
    }

    @GetMapping("/cart/all")
    public String cartAll(HttpServletRequest req, Model model) throws Exception {
        List<CartRequestDto> cartListOfCookie = (List<CartRequestDto>) model.getAttribute("cart");

        if (CookieUtils.isGuest(req)) {
            if (cartListOfCookie == null || cartListOfCookie.isEmpty()) {
                model.addAttribute("empty", true);
                model.addAttribute("view", "cart");
                return "index";
            }else{
                ResponseEntity<List<CartGetResponseDto>> responseEntity = cartService.getGuestCartList(cartListOfCookie);
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    List<CartGetResponseDto> cartList = responseEntity.getBody();
                    model.addAttribute("cartList", cartList);
                    model.addAttribute("view", "cart");
                    Map<String, Boolean> jsonCartMap = makeJsonCartMap(cartList);
                    model.addAttribute("jsonCartMap", jsonCartMap);
                    model.addAttribute("orderUrl", "/non-client/orders");
                    return "index";
                }else{
                    return "redirect:/";
                }
            }
        }

        else{
            ResponseEntity<List<CartGetResponseDto>> responseEntity = cartService.getClientCartList(CookieUtils.setHeader(req));
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                List<CartGetResponseDto> cartList = responseEntity.getBody();
                if (cartList == null || cartList.isEmpty()) {
                    model.addAttribute("empty", true);
                }else {
                    Map<String, Boolean> jsonCartMap = makeJsonCartMap(cartList);
                    model.addAttribute("jsonCartMap", jsonCartMap);
                    model.addAttribute("cartList", cartList);
                }
                model.addAttribute("view", "cart");
                model.addAttribute("orderUrl", "/client/orders");
                return "index";
            }else {
                return "redirect:/";
            }
        }
    }

    @Transactional
    @PostMapping("/cart/add")
    public ResponseEntity<Void> addCart(HttpServletRequest req, HttpServletResponse resp, @ModelAttribute @Valid CartRequestDto cartRequestDto, Model model) throws Exception {
        List<CartRequestDto> cartListOfCookie = emptyList;

        try {
            cartListOfCookie = (List<CartRequestDto>) model.getAttribute("cart");
        }catch (ClassCastException | NullPointerException ignore){
            return ResponseEntity.status(500).build();
        }

        ResponseEntity<SaveCartResponseDto> response;
        Long quantity = 0L;
        CartRequestDto cartDto = cartRequestDto;
        List<CartRequestDto> cartRequestDtoToDelete = new ArrayList<>();

        for (CartRequestDto cartItem : cartListOfCookie) {
            if (cartItem.productId().equals(cartRequestDto.productId())) {
//                cartDto = CartRequestDto.builder()
//                        .productId(cartItem.productId())
//                        .quantity(cartItem.quantity() + cartRequestDto.quantity())
//                        .build();
                cartRequestDtoToDelete.add(cartItem);
            }
        }
        try {
            cartListOfCookie.removeAll(cartRequestDtoToDelete);

            if (CookieUtils.isGuest(req)) {
                response = cartService.addGuestCartItem(cartDto);
            } else{
                response = cartService.addClientCartItem(CookieUtils.setHeader(req), cartDto);
            }
            if (response.getStatusCode().is2xxSuccessful()) {
                quantity = response.getBody().savedCartQuantity();

                // 장바구니에 담으려 한 수량이 상품 재고보다 많은 경우.
                if (!cartDto.quantity().equals(quantity)){
                    model.addAttribute("warn", true);
                }
            }
            cartListOfCookie.add(CartRequestDto.builder()
                    .productId(cartDto.productId())
                    .quantity(quantity).build());


            CookieUtils.deleteCookieValue(resp, "cart");
            CookieUtils.setCartCookieValue(cartListOfCookie, objectMapper, resp);

            return ResponseEntity.status(200).build();
        }catch (FeignException e){
            log.warn(e.getMessage());
            return ResponseEntity.status(e.status()).body(null);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }


    @PutMapping("/cart/update")
    public String updateCart(HttpServletRequest req, HttpServletResponse resp, @ModelAttribute @Valid CartRequestDto cartRequestDto, Model model) throws Exception {
        List<CartRequestDto> cartListOfCookie = emptyList;

        try {
            cartListOfCookie = (List<CartRequestDto>) model.getAttribute("cart");
        }catch (ClassCastException | NullPointerException ignore){
        }

        ResponseEntity<SaveCartResponseDto> response;
        Long quantity = 0L;
        List<CartRequestDto> cartRequestDtoToDelete = new ArrayList<>();

        for (CartRequestDto cartItem : cartListOfCookie) {
            if (cartItem.productId().equals(cartRequestDto.productId())) {
                cartRequestDtoToDelete.add(cartItem);
            }
        }

        cartListOfCookie.removeAll(cartRequestDtoToDelete);

        if (CookieUtils.isGuest(req)) {
            response = cartService.updateGuestCartItem(cartRequestDto);
        }else {
            response = cartService.updateClientCartItem(CookieUtils.setHeader(req), cartRequestDto);
        }

        if (response.getStatusCode().is2xxSuccessful()) {
            quantity = response.getBody().savedCartQuantity();

            // 장바구니에 담으려 한 수량이 상품 재고보다 많은 경우.
            if (!cartRequestDto.quantity().equals(quantity)){
                model.addAttribute("warn", true);
            }
        }
        cartListOfCookie.add(CartRequestDto.builder()
                .productId(cartRequestDto.productId())
                .quantity(quantity).build());

        log.info("cartListOfCookie: {}", cartListOfCookie);


        CookieUtils.deleteCookieValue(resp, "cart");
        CookieUtils.setCartCookieValue(cartListOfCookie, objectMapper, resp);
        return "redirect:/cart/all";
    }

    @DeleteMapping("/cart/{productId}")
    public String deleteCart(HttpServletRequest req, HttpServletResponse resp,  Model model, @PathVariable("productId") Long productId) throws Exception {
        log.info("call delete one item");
        List<CartRequestDto> cartListOfCookie = emptyList;

        try {
            cartListOfCookie = (List<CartRequestDto>) model.getAttribute("cart");
        }catch (ClassCastException | NullPointerException ignore){
        }

        List<CartRequestDto> cartRequestDtoToDelete = new ArrayList<>();
        for (CartRequestDto cartItem : cartListOfCookie) {
            if (cartItem.productId().equals(productId)) {
                cartRequestDtoToDelete.add(cartItem);
            }
        }

        if (CookieUtils.isGuest(req)) {
            cartListOfCookie.removeAll(cartRequestDtoToDelete);
        }else{
            ResponseEntity<Void> response = cartService.deleteClientCartItem(CookieUtils.setHeader(req), productId);
            if (response.getStatusCode().is2xxSuccessful()) {
                cartListOfCookie.removeAll(cartRequestDtoToDelete);
            }
        }

        CookieUtils.deleteCookieValue(resp, "cart");
        CookieUtils.setCartCookieValue(cartListOfCookie, objectMapper, resp);
        return "redirect:/cart/all";
    }


    @DeleteMapping("/cart/clear")
    public String clearCart(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        log.info("call clear cart");

        if (CookieUtils.isGuest(req)) {
            CookieUtils.deleteCookieValue(resp, "cart");
        }else {
            ResponseEntity<Void> clearClientCart = cartService.clearClientAllCart(CookieUtils.setHeader(req));
            if (clearClientCart.getStatusCode().is2xxSuccessful()) {
                CookieUtils.deleteCookieValue(resp, "cart");
            }
        }
        return "redirect:/cart/all";
    }
}
