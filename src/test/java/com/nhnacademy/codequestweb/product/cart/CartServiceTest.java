package com.nhnacademy.codequestweb.product.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.codequestweb.client.product.cart.CartClient;
import com.nhnacademy.codequestweb.request.product.cart.CartRequestDto;
import com.nhnacademy.codequestweb.response.product.common.CartGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.SaveCartResponseDto;
import com.nhnacademy.codequestweb.service.product.CartService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartClient cartClient;

    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void restoreCart() {
        when(cartClient.restoreClientCartList(headers)).thenReturn(ResponseEntity.ok(Arrays.asList(
                CartRequestDto.builder().build(),
                CartRequestDto.builder().build()
        )));
        ResponseEntity<List<CartRequestDto>> responseEntity = cartService.restoreClientCartList(headers);
        assertNotNull(responseEntity);
        assertEquals(200,responseEntity.getStatusCode().value());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().size());

        verify(cartClient, times(1)).restoreClientCartList(headers);
    }

    @Test
    void getClientCartList(){
        when(cartClient.getClientCartList(headers)).thenReturn(ResponseEntity.ok(Arrays.asList(
                CartGetResponseDto.builder().build(),
                CartGetResponseDto.builder().build(),
                CartGetResponseDto.builder().build()
        )));

        ResponseEntity<List<CartGetResponseDto>> responseEntity = cartService.getClientCartList(headers);

        assertNotNull(responseEntity);
        assertEquals(200,responseEntity.getStatusCode().value());
        assertNotNull(responseEntity.getBody());
        assertEquals(3, responseEntity.getBody().size());

        verify(cartClient, times(1)).getClientCartList(headers);
    }

    @Test
    void getGuestCartList(){
        when(cartClient.getGuestCartList(any())).thenReturn(ResponseEntity.ok(Arrays.asList(
                CartGetResponseDto.builder().build(),
                CartGetResponseDto.builder().build(),
                CartGetResponseDto.builder().build(),
                CartGetResponseDto.builder().build()
        )));

        List<CartRequestDto> requestDtoList = new ArrayList<>();
        ResponseEntity<List<CartGetResponseDto>> responseEntity = cartService.getGuestCartList(requestDtoList);

        assertNotNull(responseEntity);
        assertEquals(200,responseEntity.getStatusCode().value());
        assertNotNull(responseEntity.getBody());
        assertEquals(4, responseEntity.getBody().size());

        verify(cartClient, times(1)).getGuestCartList(requestDtoList);
    }

    @Test
    void addClientCartItemTest(){
        CartRequestDto requestDto = CartRequestDto.builder().build();
        SaveCartResponseDto saveCartResponseDto = SaveCartResponseDto.builder().build();

        when(cartClient.addClientCartItem(headers, requestDto)).thenReturn(ResponseEntity.ok(saveCartResponseDto));

        ResponseEntity<SaveCartResponseDto> responseEntity = cartService.addClientCartItem(headers, requestDto);
        assertNotNull(responseEntity);
        assertEquals(200,responseEntity.getStatusCode().value());
        assertNotNull(responseEntity.getBody());
        assertEquals(saveCartResponseDto, responseEntity.getBody());

        verify(cartClient, times(1)).addClientCartItem(headers, requestDto);
    }

    @Test
    void addGuestCartItemTest(){
        CartRequestDto requestDto = CartRequestDto.builder().build();
        SaveCartResponseDto saveCartResponseDto = SaveCartResponseDto.builder().build();

        when(cartClient.addGuestCartItem(requestDto)).thenReturn(ResponseEntity.ok(saveCartResponseDto));

        ResponseEntity<SaveCartResponseDto> responseEntity = cartService.addGuestCartItem(requestDto);
        assertNotNull(responseEntity);
        assertEquals(200,responseEntity.getStatusCode().value());
        assertNotNull(responseEntity.getBody());
        assertEquals(saveCartResponseDto, responseEntity.getBody());
        verify(cartClient, times(1)).addGuestCartItem(requestDto);
    }

    @Test
    void updateClientCartItemTest(){
        CartRequestDto requestDto = CartRequestDto.builder().build();
        SaveCartResponseDto saveCartResponseDto = SaveCartResponseDto.builder().build();

        when(cartClient.updateClientCartItem(headers, requestDto)).thenReturn(ResponseEntity.ok(saveCartResponseDto));

        ResponseEntity<SaveCartResponseDto> responseEntity = cartService.updateClientCartItem(headers, requestDto);
        assertNotNull(responseEntity);
        assertEquals(200,responseEntity.getStatusCode().value());
        assertNotNull(responseEntity.getBody());
        assertEquals(saveCartResponseDto, responseEntity.getBody());

        verify(cartClient, times(1)).updateClientCartItem(headers, requestDto);
    }

    @Test
    void updateGuestCartItemTest(){
        CartRequestDto requestDto = CartRequestDto.builder().build();
        SaveCartResponseDto saveCartResponseDto = SaveCartResponseDto.builder().build();

        when(cartClient.updateGuestCartItem(requestDto)).thenReturn(ResponseEntity.ok(saveCartResponseDto));

        ResponseEntity<SaveCartResponseDto> responseEntity = cartService.updateGuestCartItem(requestDto);

        assertNotNull(responseEntity);
        assertEquals(200,responseEntity.getStatusCode().value());
        assertNotNull(responseEntity.getBody());
        assertEquals(saveCartResponseDto, responseEntity.getBody());

        verify(cartClient, times(1)).updateGuestCartItem(requestDto);
    }

    @Test
    void deleteClientCartItemTest(){
        when(cartClient.deleteClientCartItem(headers,1L)).thenReturn(ResponseEntity.ok().body(null));

        ResponseEntity<Void> responseEntity = cartService.deleteClientCartItem(headers,1L);
        assertNotNull(responseEntity);
        assertEquals(200,responseEntity.getStatusCode().value());

        verify(cartClient, times(1)).deleteClientCartItem(headers,1L);
    }

    @Test
    void clearClientCartTest(){

        when(cartClient.clearClientAllCart(headers)).thenReturn(ResponseEntity.ok().body(null));

        ResponseEntity<Void> responseEntity = cartService.clearClientAllCart(headers);
        assertNotNull(responseEntity);
        assertEquals(200,responseEntity.getStatusCode().value());

        verify(cartClient, times(1)).clearClientAllCart(headers);
    }

}
