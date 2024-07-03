package com.nhnacademy.codequestweb.controller.product.permitAll;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.codequestweb.request.product.cart.CartRequestDto;
import com.nhnacademy.codequestweb.response.product.common.CartGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.SaveCartResponseDto;
import com.nhnacademy.codequestweb.service.product.CartService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
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


@Slf4j
@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    private final ObjectMapper objectMapper;

    private final List<CartRequestDto> emptyList = new ArrayList<>();

    @GetMapping("/cart/all")
    public String cartAll(HttpServletRequest req, Model model) throws Exception {
        List<CartRequestDto> cartListOfCookie = (List<CartRequestDto>) model.getAttribute("cart");

        if (CookieUtils.isGuest(req)) {
            if (cartListOfCookie == null || cartListOfCookie.isEmpty()) {
                model.addAttribute("empty", true);
                return "/view/product/cart";
            }else{
                ResponseEntity<List<CartGetResponseDto>> responseEntity = cartService.getGuestCartList(cartListOfCookie);
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    List<CartGetResponseDto> cartList = responseEntity.getBody();
                    for (CartGetResponseDto cartGetResponseDto : cartList) {
                        log.error("map : {}",cartGetResponseDto.categoryMapOfIdAndName());
                    }
                    model.addAttribute("cartList", cartList);
                    return "/view/product/cart";
                }else{
                    return "redirect:/";
                }
            }
        }
        // 아마도 CartControllerAdvice 의 로직이 잘 동작한다면 (쿠키에 담긴 회원 장바구니를 변조/삭제했을 시 데이터베이스 조회해서 자동 복구하고 쿠키에 새로 담음)
        // 로직을 둘로 구분 (isGuest 인지 아닌지 경우로) 할 필요 없이 그냥 동일하게 위 로직 써도 될 것 같은데 어떻게 생각하시나요?
        // 가독성 부분에선 그렇게 하는게 나을 것 같고, 혹시 모르니 안정성 관점에선 로직을 둘로 나눠야 할 것도 같고..

        else{
            ResponseEntity<List<CartGetResponseDto>> responseEntity = cartService.getClientCartList(CookieUtils.setHeader(req));
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                List<CartGetResponseDto> cartList = responseEntity.getBody();
                if (cartList == null || cartList.isEmpty()) {
                    model.addAttribute("empty", true);
                }else {
                    model.addAttribute("cartList", cartList);
                }
                return "/view/product/cart";
            }else {
                return "redirect:/";
            }
        }
    }

    @Transactional
    @PostMapping("/cart/add")
    public String addCart(HttpServletRequest req, HttpServletResponse resp, @ModelAttribute @Valid CartRequestDto cartRequestDto, Model model) throws Exception {
        log.info("call add cart");

        List<CartRequestDto> cartListOfCookie = emptyList;

        try {
            cartListOfCookie = (List<CartRequestDto>) model.getAttribute("cart");
        }catch (ClassCastException | NullPointerException ignore){
        }

        ResponseEntity<SaveCartResponseDto> response;
        Long quantity = 0L;
        CartRequestDto cartDto = cartRequestDto;
        List<CartRequestDto> cartRequestDtoToDelete = new ArrayList<>();

        for (CartRequestDto cartItem : cartListOfCookie) {
            if (cartItem.productId().equals(cartRequestDto.productId())) {
                cartDto = CartRequestDto.builder()
                        .productId(cartItem.productId())
                        .quantity(cartItem.quantity() + cartRequestDto.quantity())
                        .build();
                cartRequestDtoToDelete.add(cartItem);
            }
        }

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
        log.info("cartListOfCookie: {}", cartListOfCookie);


        CookieUtils.deleteCookieValue(resp, "cart");
        CookieUtils.setCartCookieValue(cartListOfCookie, objectMapper, resp);
        return "redirect:/";
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
