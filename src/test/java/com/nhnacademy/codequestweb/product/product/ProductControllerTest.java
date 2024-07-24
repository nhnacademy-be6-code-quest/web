package com.nhnacademy.codequestweb.product.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nhnacademy.codequestweb.controller.product.client.ProductController;
import com.nhnacademy.codequestweb.service.product.ProductService;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class ProductControllerTest {
    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void likeTest1() throws Exception {

        when(productService.saveProductLike(any(), any())).thenReturn(ResponseEntity.ok(null));

        mockMvc.perform(post("/product/client/like"))
                .andExpect(status().isOk())
                .andExpect(view().name("view/product/refresh"));
    }

    @Test
    void likeTest2() throws Exception {

        when(productService.saveProductLike(any(), any())).thenReturn(null);

        mockMvc.perform(post("/product/client/like"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/books"))
                .andExpect(flash().attribute("alterMessage", "요청 처리 과정에서 오류가 발생했습니다."));
    }

    @Test
    void likeTest3() throws Exception {

        when(productService.saveProductLike(any(), any())).thenReturn(ResponseEntity.status(302).build());

        mockMvc.perform(post("/product/client/like"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/books"))
                .andExpect(flash().attribute("alterMessage", "요청 처리 과정에서 오류가 발생했습니다."));
    }

    @Test
    void likeTest4() throws Exception {

        when(productService.saveProductLike(any(), any())).thenThrow(FeignException.class);

        mockMvc.perform(post("/product/client/like"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/books"))
                .andExpect(flash().attribute("alterMessage", "요청 처리 과정에서 오류가 발생했습니다."));
    }

    @Test
    void unlikeTest1() throws Exception {

        when(productService.deleteProductLike(any(), any())).thenReturn(ResponseEntity.ok(null));

        mockMvc.perform(delete("/product/client/unlike")
                        .param("productId","1"))
                .andExpect(status().isOk())
                .andExpect(view().name("view/product/refresh"));
    }

    @Test
    void unlikeTest2() throws Exception {

        when(productService.deleteProductLike(any(), any())).thenReturn(null);

        mockMvc.perform(delete("/product/client/unlike")
                        .param("productId","1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/books"))
                .andExpect(flash().attribute("alterMessage", "요청 처리 과정에서 오류가 발생했습니다."));
    }

    @Test
    void unlikeTest3() throws Exception {

        when(productService.deleteProductLike(any(), any())).thenReturn(ResponseEntity.status(302).build());

        mockMvc.perform(delete("/product/client/unlike")
                        .param("productId","1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/books"))
                .andExpect(flash().attribute("alterMessage", "요청 처리 과정에서 오류가 발생했습니다."));
    }

    @Test
    void unlikeTest4() throws Exception {

        when(productService.deleteProductLike(any(), any())).thenThrow(FeignException.class);

        mockMvc.perform(delete("/product/client/unlike")
                        .param("productId","1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/books"))
                .andExpect(flash().attribute("alterMessage", "요청 처리 과정에서 오류가 발생했습니다."));
    }

    @Test
    void updateBookStateTest1() throws Exception {

        when(productService.updateProductState(any(), any())).thenReturn(ResponseEntity.ok(null));

        mockMvc.perform(put("/admin/product/state")
                        .param("productId", "1")
                        .param("productState", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void updateBookStateTest2() throws Exception {
        Request req = mock(Request.class);
        when(productService.updateProductState(any(), any()))
                .thenThrow(new FeignException.BadRequest(null, req,null, null));

        mockMvc.perform(put("/admin/product/state")
                .param("productId", "1")
                .param("productState", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void setBookInventoryTest1() throws Exception {

        when(productService.setProductInventory(any(), any())).thenReturn(ResponseEntity.ok(null));

        mockMvc.perform(put("/admin/inventory/set")
                        .param("productId", "1")
                        .param("quantityToSet", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void setBookInventoryTest2() throws Exception {
        Request req = mock(Request.class);
        when(productService.setProductInventory(any(), any()))
                .thenThrow(new FeignException.BadRequest(null, req,null, null));

        mockMvc.perform(put("/admin/inventory/set")
                        .param("productId", "1")
                        .param("quantityToSet", "1"))
                .andExpect(status().isBadRequest());
    }
}
