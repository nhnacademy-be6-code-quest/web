package com.nhnacademy.codequestweb.product.packaging;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nhnacademy.codequestweb.controller.product.everyone.PackagingController;
import com.nhnacademy.codequestweb.response.product.packaging.PackagingGetResponseDto;
import com.nhnacademy.codequestweb.service.product.PackagingService;
import feign.FeignException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class PackagingControllerTest {
    @InjectMocks
    private PackagingController packagingController;

    @Mock
    private PackagingService packagingService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(packagingController).build();

    }

    @Test
    void getPackagingPageTest1() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 10);

        List<PackagingGetResponseDto> packagingGetResponseDtoList = Arrays.asList(
                PackagingGetResponseDto.builder().build(),
                PackagingGetResponseDto.builder().build(),
                PackagingGetResponseDto.builder().build()
        );

        Page<PackagingGetResponseDto> responseDtoPage = new PageImpl<>(packagingGetResponseDtoList, pageRequest, 5);

        when(packagingService.getPackagingPage(0,1,9))
                .thenReturn(ResponseEntity.ok(responseDtoPage));

        mockMvc.perform(get("/product/packaging/page")
                        .param("productState","0")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("totalPage", 1));
    }

    @Test
    void getPackagingPageTest2() throws Exception {

        when(packagingService.getPackagingPage(0,1,9))
                .thenReturn(ResponseEntity.ok(null));

        mockMvc.perform(get("/product/packaging/page")
                        .param("productState","0")
                        .param("page", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("alterMessage", "응답 본문이 비어있습니다."));
    }

    @Test
    void getPackagingPageTest3() throws Exception {

        when(packagingService.getPackagingPage(0,1,9))
                .thenThrow(FeignException.class);

        mockMvc.perform(get("/product/packaging/page")
                        .param("productState","0")
                        .param("page", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("alterMessage", "포장지 조회에 실패했습니다."));
    }
}
