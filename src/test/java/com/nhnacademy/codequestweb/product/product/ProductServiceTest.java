package com.nhnacademy.codequestweb.product.product;

import com.nhnacademy.codequestweb.client.product.product.ProductClient;
import com.nhnacademy.codequestweb.request.product.ProductLikeRequestDto;
import com.nhnacademy.codequestweb.request.product.ProductStateUpdateRequestDto;
import com.nhnacademy.codequestweb.request.product.common.InventorySetRequestDto;
import com.nhnacademy.codequestweb.response.product.common.ProductUpdateResponseDto;
import com.nhnacademy.codequestweb.service.product.ProductService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductClient productClient;

    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void saveProductLikeTest(){
        ProductLikeRequestDto requestDto = ProductLikeRequestDto.builder().build();
        when(productClient.saveBookProductLike(any(),any())).thenReturn(ResponseEntity.ok(null));

        ResponseEntity<Void> response = productService.saveProductLike(headers, requestDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void deleteProductLikeTest(){
        when(productClient.deleteBookProductLike(any(),any())).thenReturn(ResponseEntity.ok(null));

        ResponseEntity<Void> response = productService.deleteProductLike(headers, 1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void updateProductStateTest(){
        ProductStateUpdateRequestDto requestDto = ProductStateUpdateRequestDto.builder().build();
        when(productClient.updateBookState(any(),any())).thenReturn(ResponseEntity.ok(null));

        ResponseEntity<ProductUpdateResponseDto> response = productService.updateProductState(headers, requestDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void setProductInventoryTest(){
        InventorySetRequestDto requestDto = InventorySetRequestDto.builder().build();
        when(productClient.setProductInventory(any(),any())).thenReturn(ResponseEntity.ok(null));

        ResponseEntity<Void> response = productService.setProductInventory(headers, requestDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }
}
