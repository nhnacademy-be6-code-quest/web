package com.nhnacademy.codequestweb.config.cart;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.codequestweb.request.product.cart.CartRequestDto;
import com.nhnacademy.codequestweb.service.product.CartService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class CartControllerAdviceTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private CartService cartService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Model model;

    private CartControllerAdvice cartControllerAdvice;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartControllerAdvice = new CartControllerAdvice(objectMapper, cartService);
    }

    @Test
    void testAddAttributesWithValidEncodedCart() throws Exception {
        String encodedCart = Base64.getEncoder().encodeToString("[]".getBytes());

        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("cart", encodedCart)});
        when(objectMapper.readValue(anyString(), (TypeReference<List<CartRequestDto>>) any())).thenReturn(new ArrayList<CartRequestDto>());

        cartControllerAdvice.addAttributes(request, response, model);

        verify(model).addAttribute(eq("cart"), any(List.class));
    }

    @Test
    void testAddAttributesWithCorruptedCookie() throws Exception {
        Cookie corruptedCookie = new Cookie("cart", "corrupted");
        when(request.getCookies()).thenReturn(new Cookie[]{corruptedCookie});

        Cookie accessTokenCookie = new Cookie("access", "accessToken");
        Cookie refreshTokenCookie = new Cookie("refresh", "refreshToken");
        when(request.getCookies()).thenReturn(new Cookie[]{accessTokenCookie, refreshTokenCookie});
        when(cartService.restoreClientCartList(any())).thenReturn(ResponseEntity.ok(new ArrayList<>()));
        when(objectMapper.writeValueAsString(any())).thenReturn("string");

        cartControllerAdvice.addAttributes(request, response, model);

        verify(response, times(2)).addCookie(any(Cookie.class));
        verify(model).addAttribute(eq("cart"), any(List.class));
    }

    @Test
    void testAddAttributesWithNoCookie() throws Exception {
        when(request.getCookies()).thenReturn(null);
        when(objectMapper.writeValueAsString(any())).thenReturn("string");

        cartControllerAdvice.addAttributes(request, response, model);

        verify(response,times(2)).addCookie(any(Cookie.class));
        verify(model).addAttribute(eq("cart"), any(List.class));
    }

    @Test
    void testAddAttributesWithExceptionDuringDecryption() throws Exception {
        String encodedCart = Base64.getEncoder().encodeToString("[]".getBytes());

        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("cart", encodedCart)});
        when(objectMapper.writeValueAsString(any())).thenReturn("string");
        when(objectMapper.readValue(anyString(), (TypeReference<List<CartRequestDto>>) any())).thenThrow(new RuntimeException("Decryption error"));

        cartControllerAdvice.addAttributes(request, response, model);

        verify(model).addAttribute(eq("cart"), any(List.class));
    }
}