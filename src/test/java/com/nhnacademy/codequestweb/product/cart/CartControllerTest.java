package com.nhnacademy.codequestweb.product.cart;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.codequestweb.controller.product.everyone.CartController;
import com.nhnacademy.codequestweb.request.product.cart.CartRequestDto;
import com.nhnacademy.codequestweb.response.product.common.CartGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.SaveCartResponseDto;
import com.nhnacademy.codequestweb.service.product.CartService;
import feign.FeignException;
import feign.Request;
import jakarta.servlet.http.Cookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@Slf4j
class CartControllerTest {
    private static final String VIEW = "view";

    private static final String INDEX = "index";

    private static final String CART = "cart";


    @InjectMocks
    private CartController cartController;

    @Mock
    private CartService cartService;

    @Mock
    private ObjectMapper mapper;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private Cookie testAccessCookie;

    private List<CartRequestDto> requestDtoList;

    private CartGetResponseDto responseDto1;

    private List<CartGetResponseDto> responseDtoList;

    private SaveCartResponseDto saveCartResponseDto;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
        requestDtoList = Arrays.asList(
                CartRequestDto.builder()
                        .productId(1L)
                        .quantity(10L)
                        .build(),
                CartRequestDto.builder()
                        .productId(2L)
                        .quantity(20L)
                        .build()
        );

        responseDto1 = CartGetResponseDto.builder()
                .productInventory(100L)
                .productQuantityOfCart(10L)
                .productState(0)
                .build();

        CartGetResponseDto responseDto2 = CartGetResponseDto.builder()
                .productInventory(200L)
                .productQuantityOfCart(20L)
                .productState(0)
                .build();

        CartGetResponseDto responseDto3 = CartGetResponseDto.builder()
                .productInventory(300L)
                .productQuantityOfCart(30L)
                .productState(1)
                .build();

        CartGetResponseDto responseDto4 = CartGetResponseDto.builder()
                .productInventory(4L)
                .productQuantityOfCart(400L)
                .productState(0)
                .build();

        responseDtoList = Arrays.asList(
                responseDto1, responseDto2, responseDto3, responseDto4
        );

        objectMapper = new ObjectMapper();

        testAccessCookie = new Cookie("access", "test");

        saveCartResponseDto = SaveCartResponseDto.builder()
                .savedCartQuantity(5L)
                .build();

        when(mapper.writeValueAsString(responseDto1)).thenReturn("item1");
        when(mapper.writeValueAsString(responseDto2)).thenReturn("item2");
        when(mapper.writeValueAsString(responseDto3)).thenReturn("item3");
        when(mapper.writeValueAsString(responseDto4)).thenReturn("item4");

        List<CartRequestDto> cartRequestDtoList = new ArrayList<>();

        cartRequestDtoList.add(CartRequestDto.builder()
                .productId(1L)
                .quantity(5L)
                .build());

        when(mapper.writeValueAsString(cartRequestDtoList)).thenReturn("item1");
        when(mapper.writeValueAsString(cartRequestDtoList)).thenReturn("item1");

        List<CartRequestDto> emptyCartRequestDtoList = new ArrayList<>();

        when(mapper.writeValueAsString(emptyCartRequestDtoList)).thenReturn("empty");
    }

    @DisplayName("비회원 장바구니 조회 테스트 - 비어있는 장바구니")
    @Test
    void getEmptyGuestCartTest() throws Exception{
        mockMvc.perform(get("/cart/all")
                )
                .andExpect(status().isOk())
                .andExpect(view().name(INDEX))
                .andExpect(model().attribute(VIEW, CART))
                .andExpect(model().attribute("empty", true))
                .andExpect(model().attribute("orderUrl", "/non-client/orders"));

        verify(cartService, times(0)).getGuestCartList(any());
    }


    @DisplayName("비회원 장바구니 조회 테스트 - 비어있지 않은 장바구니")
    @Test
    void getExistingGuestCartTest() throws Exception{
        String cartJson = objectMapper.writeValueAsString(requestDtoList);
        String encodedCart = Base64.getEncoder().encodeToString(cartJson.getBytes());

        when(mapper.readValue(eq(cartJson), any(TypeReference.class))).thenReturn(requestDtoList);

        when(cartService.getGuestCartList(requestDtoList)).thenReturn(ResponseEntity.ok(responseDtoList));

        mockMvc.perform(get("/cart/all")
                        .cookie(new Cookie(CART, encodedCart)))
                .andExpect(status().isOk())
                .andExpect(view().name(INDEX))
                .andExpect(model().attribute(VIEW, CART))
                .andExpect(model().attributeDoesNotExist("empty"))
                .andExpect(model().attribute("orderUrl", "/non-client/orders"));

        verify(cartService, times(1)).getGuestCartList(any());
    }

    @DisplayName("비회원 장바구니 조회 테스트 - 조회 실패")
    @Test
    void getExistingGuestCartTest2() throws Exception{
        String cartJson = objectMapper.writeValueAsString(requestDtoList);
        String encodedCart = Base64.getEncoder().encodeToString(cartJson.getBytes());

        when(mapper.readValue(eq(cartJson), any(TypeReference.class)))
                .thenReturn(requestDtoList);

        when(cartService.getGuestCartList(requestDtoList)).thenThrow(FeignException.class);

        mockMvc.perform(get("/cart/all")
                        .cookie(new Cookie(CART, encodedCart)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andDo(print());

        verify(cartService, times(1)).getGuestCartList(any());
    }


    @DisplayName("회원 장바구니 조회 성공 테스트")
    @Test
    void getClientCartSuccessTest() throws Exception{
        when(cartService.getClientCartList(any())).thenReturn(ResponseEntity.ok(responseDtoList));

        mockMvc.perform(get("/cart/all")
                        .cookie(testAccessCookie))
                .andExpect(status().isOk())
                .andExpect(view().name(INDEX))
                .andExpect(model().attribute("cartList", responseDtoList))
                .andExpect(model().attribute(VIEW, CART))
                .andExpect(model().attribute("orderUrl", "/client/orders"))
                .andExpect(model().attribute("jsonCartMap", Map.of(
                        "item1", true,
                        "item2", true,
                        "item3", false,
                        "item4", false)));
    }


    @DisplayName("회원 장바구니 조회 실패 테스트 - Json parsing 오류")
    @Test
    void getClientCartFailureTest1() throws Exception{
        when(cartService.getClientCartList(any())).thenReturn(ResponseEntity.ok(responseDtoList));
        when(mapper.writeValueAsString(responseDto1)).thenThrow(new JsonParseException("test"));

        mockMvc.perform(get("/cart/all")
                        .cookie(testAccessCookie))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("alterMessage", "장바구니 처리 과정에 일시적으로 문제가 발생했습니다.\n문제가 지속된다면 문의 부탁드립니다."));
    }


    @DisplayName("회원 장바구니 조회 테스트 - 응답 코드가 redirection 이고 리디렉션 URI 가 존재 (실제 서비스에선 당장 발생할 일은 없음)")
    @Test
    void getClientCart1() throws Exception{
        when(cartService.getClientCartList(any())).thenReturn(ResponseEntity.status(301).location(URI.create("/test/redirect")).body(null));

        mockMvc.perform(get("/cart/all")
                        .cookie(testAccessCookie))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/test/redirect"))
                .andDo(print());
    }


    @DisplayName("회원 장바구니 조회 테스트 - 응답 코드가 redirection 이고 리디렉션 URI 가 존재하지 않음 (실제 서비스에선 당장 발생할 일은 없음)")
    @Test
    void getClientCart2() throws Exception{
        when(cartService.getClientCartList(any())).thenReturn(ResponseEntity.status(301).body(null));

        mockMvc.perform(get("/cart/all")
                        .cookie(testAccessCookie))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andDo(print());
    }

    @DisplayName("회원 장바구니 조회 테스트 - 응답 코드가 redirection 이고 리디렉션 URI 가 존재하지 않음 (실제 서비스에선 당장 발생할 일은 없음)")
    @Test
    void getClientCart3() throws Exception{
        when(cartService.getClientCartList(any()))
                .thenThrow(FeignException.class);

        mockMvc.perform(get("/cart/all")
                        .cookie(testAccessCookie))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andDo(print());
    }

    @DisplayName("비회원 장바구니 물품 추가 성공 테스트")
    @Test
    void addGuestCartItemTest1() throws Exception{
        List<CartRequestDto> cartRequestDtoList = new ArrayList<>();
        cartRequestDtoList.add(CartRequestDto.builder()
                .productId(1L)
                .quantity(5L)
                .build());

        when(cartService.addGuestCartItem(any())).thenReturn(ResponseEntity.ok().body(saveCartResponseDto));
        when(mapper.writeValueAsString(cartRequestDtoList)).thenReturn("item1");

        mockMvc.perform(post("/cart/add")
                        .param("productId", "1")
                        .param("quantity", "10"))
                .andExpect(status().isOk())
        ;

        verify(cartService, times(0)).addClientCartItem(any(), any());
        verify(cartService, times(1)).addGuestCartItem(any());
    }


    @DisplayName("회원 장바구니 물품 추가 성공 테스트")
    @Test
    void addClientCartItemTest1() throws Exception{
        List<CartRequestDto> cartRequestDtoList = new ArrayList<>();
        cartRequestDtoList.add(CartRequestDto.builder()
                .productId(1L)
                .quantity(5L)
                .build());

        when(cartService.addClientCartItem(any(), any())).thenReturn(ResponseEntity.ok().body(saveCartResponseDto));
        when(mapper.writeValueAsString(cartRequestDtoList)).thenReturn("item1");

        mockMvc.perform(post("/cart/add")
                        .cookie(testAccessCookie)
                        .param("productId", "1")
                        .param("quantity", "10"))
                .andExpect(status().isOk())
        ;

        verify(cartService, times(1)).addClientCartItem(any(), any());
        verify(cartService, times(0)).addGuestCartItem(any());
    }

    @DisplayName("회원 장바구니 물품 추가 실패 테스트 - feign exception")
    @Test
    void addClientCartItemTest2() throws Exception{
        Request req = mock(Request.class);
        when(cartService.addClientCartItem(any(), any()))
                .thenThrow(new FeignException.BadRequest(null, req,null, null));

        mockMvc.perform(post("/cart/add")
                        .cookie(testAccessCookie)
                        .param("productId", "1")
                        .param("quantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("alterMessage", "장바구니 처리 과정에 일시적으로 문제가 발생했습니다.\n문제가 지속된다면 문의 부탁드립니다."));


        verify(cartService, times(1)).addClientCartItem(any(), any());
        verify(cartService, times(0)).addGuestCartItem(any());
    }


    @DisplayName("회원 장바구니 물품 수량 변경 성공 테스트")
    @Test
    void updateClientCartItemTest1() throws Exception{
        when(cartService.updateClientCartItem(any(),any())).thenReturn(ResponseEntity.ok().body(saveCartResponseDto));

        mockMvc.perform(put("/cart/update")
                        .cookie(testAccessCookie)
                        .param("productId", "1")
                        .param("quantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/all"))
                .andExpect(flash().attribute("alterMessage", "물건을 장바구니에 담았습니다."));

        verify(cartService, times(1)).updateClientCartItem(any(), any());
        verify(cartService, times(0)).updateGuestCartItem(any());
    }


    @DisplayName("회원 장바구니 물품 수량 변경 실패 테스트")
    @Test
    void updateClientCartItemTest2() throws Exception{
        when(cartService.updateClientCartItem(any(),any())).thenThrow(new RuntimeException());

        mockMvc.perform(put("/cart/update")
                        .cookie(testAccessCookie)
                        .param("productId", "1")
                        .param("quantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("alterMessage", "장바구니 처리 과정에 일시적으로 문제가 발생했습니다.\n문제가 지속된다면 문의 부탁드립니다."));

        verify(cartService, times(1)).updateClientCartItem(any(), any());
        verify(cartService, times(0)).updateGuestCartItem(any());
    }


    @DisplayName("비회원 장바구니 물품 수량 변경 성공 테스트")
    @Test
    void updateGuestCartItemTest1() throws Exception{
        when(cartService.updateGuestCartItem(any())).thenReturn(ResponseEntity.ok().body(saveCartResponseDto));

        mockMvc.perform(put("/cart/update")
                        .param("productId", "1")
                        .param("quantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/all"))
                .andExpect(flash().attribute("alterMessage", "물건을 장바구니에 담았습니다."));

        verify(cartService, times(0)).updateClientCartItem(any(), any());
        verify(cartService, times(1)).updateGuestCartItem(any());
    }


    @DisplayName("비회원 장바구니 물품 수량 변경 실패 테스트")
    @Test
    void updateGuestCartItemTest2() throws Exception{
        when(cartService.updateGuestCartItem(any())).thenThrow(new RuntimeException());

        mockMvc.perform(put("/cart/update")
                        .param("productId", "1")
                        .param("quantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("alterMessage", "장바구니 처리 과정에 일시적으로 문제가 발생했습니다.\n문제가 지속된다면 문의 부탁드립니다."));

        verify(cartService, times(0)).updateClientCartItem(any(), any());
        verify(cartService, times(1)).updateGuestCartItem(any());
    }


    @DisplayName("회원 장바구니 물품 삭제 성공 테스트")
    @Test
    void deleteClientCartItemTest1() throws Exception{
        when(cartService.deleteClientCartItem(any(),any())).thenReturn(ResponseEntity.ok().body(null));

        mockMvc.perform(delete("/cart/1")
                        .cookie(testAccessCookie)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/all"))
                .andExpect(flash().attribute("alterMessage", "장바구니에서 물건을 제거했습니다."));

        verify(cartService, times(1)).deleteClientCartItem(any(), any());
    }


    @DisplayName("회원 장바구니 물품 삭제 실패 테스트")
    @Test
    void deleteClientCartItemTest2() throws Exception{
        when(cartService.deleteClientCartItem(any(),any())).thenThrow(new RuntimeException());

        mockMvc.perform(delete("/cart/1")
                        .cookie(testAccessCookie)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("alterMessage", "장바구니 처리 과정에 일시적으로 문제가 발생했습니다.\n문제가 지속된다면 문의 부탁드립니다."));

        verify(cartService, times(1)).deleteClientCartItem(any(), any());
    }


    @DisplayName("비회원 장바구니 물품 삭제 성공 테스트")
    @Test
    void deleteGuestCartItemTest1() throws Exception{
        when(cartService.deleteClientCartItem(any(),any())).thenReturn(ResponseEntity.ok().body(null));

        mockMvc.perform(delete("/cart/1")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/all"))
                .andExpect(flash().attribute("alterMessage", "장바구니에서 물건을 제거했습니다."));

        verify(cartService, times(0)).deleteClientCartItem(any(), any());
    }


    @DisplayName("회원 장바구니 비우기 성공 테스트")
    @Test
    void clearClientCartTest1() throws Exception{
        when(cartService.clearClientAllCart(any())).thenReturn(ResponseEntity.ok().body(null));

        mockMvc.perform(delete("/cart/clear")
                .cookie(testAccessCookie))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/all"))
                .andExpect(flash().attribute("alterMessage", "장바구니를 비웠습니다."));

        verify(cartService, times(1)).clearClientAllCart(any());
    }

    @DisplayName("회원 장바구니 비우기 실패 테스트")
    @Test
    void clearClientCartTest2() throws Exception{
        when(cartService.clearClientAllCart(any())).thenThrow(new RuntimeException());

        mockMvc.perform(delete("/cart/clear")
                        .cookie(testAccessCookie))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("alterMessage", "장바구니 처리 과정에 일시적으로 문제가 발생했습니다.\n문제가 지속된다면 문의 부탁드립니다."));

        verify(cartService, times(1)).clearClientAllCart(any());
    }


    @DisplayName("비회원 장바구니 비우기 성공 테스트")
    @Test
    void clearGuestCartTest() throws Exception{
        mockMvc.perform(delete("/cart/clear"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart/all"))
                .andExpect(flash().attribute("alterMessage", "장바구니를 비웠습니다."));

        verify(cartService, times(0)).clearClientAllCart(any());
    }
}
