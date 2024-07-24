package com.nhnacademy.codequestweb.product.packaging;

import com.nhnacademy.codequestweb.client.product.packaging.PackagingClient;
import com.nhnacademy.codequestweb.request.product.packaging.PackagingRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.packaging.PackagingUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.common.ProductRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductUpdateResponseDto;
import com.nhnacademy.codequestweb.response.product.packaging.PackagingGetResponseDto;
import com.nhnacademy.codequestweb.service.product.PackagingService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PackagingServiceTest {
    @InjectMocks
    private PackagingService packagingService;

    @Mock
    private PackagingClient packagingClient;

    private HttpHeaders headers;

    private PageRequest pageRequest;

    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        pageRequest = PageRequest.of(0, 10);
    }

    @Test
    void savePackagingTest(){
        PackagingRegisterRequestDto requestDto = PackagingRegisterRequestDto.builder().build();

        ProductRegisterResponseDto responseDto = new ProductRegisterResponseDto(1L, LocalDateTime.now());
        when(packagingClient.savePackaging(any(), any())).thenReturn(ResponseEntity.ok(responseDto));

        ResponseEntity<ProductRegisterResponseDto> response = packagingService.savePackaging(headers, requestDto);

        assertNotNull(response);
        assertEquals(responseDto, response.getBody());

        verify(packagingClient, times(1)).savePackaging(any(), any());
    }

    @Test
    void updatePackagingTest(){
        PackagingUpdateRequestDto requestDto = PackagingUpdateRequestDto.builder().build();

        ProductUpdateResponseDto responseDto = new ProductUpdateResponseDto(LocalDateTime.now());
        when(packagingClient.updatePackaging(any(), any())).thenReturn(ResponseEntity.ok(responseDto));

        ResponseEntity<ProductUpdateResponseDto> response = packagingService.updatePackaging(headers, requestDto);

        assertNotNull(response);
        assertEquals(responseDto, response.getBody());

        verify(packagingClient, times(1)).updatePackaging(any(), any());
    }

    @Test
    void getPackagingListTest(){
        List<PackagingGetResponseDto> responseDtoList = Arrays.asList(
                PackagingGetResponseDto.builder().build(),
                PackagingGetResponseDto.builder().build(),
                PackagingGetResponseDto.builder().build()
        );


        when(packagingClient.getPackagingList(any())).thenReturn(ResponseEntity.ok(responseDtoList));

        ResponseEntity<List<PackagingGetResponseDto>> response = packagingService.getPackagingList(0);

        assertNotNull(response);
        assertEquals(responseDtoList, response.getBody());

        verify(packagingClient, times(1)).getPackagingList(0);
    }

    @Test
    void getPackagingPageTest(){
        List<PackagingGetResponseDto> responseDtoList = Arrays.asList(
                PackagingGetResponseDto.builder().build(),
                PackagingGetResponseDto.builder().build(),
                PackagingGetResponseDto.builder().build()
        );

        Page<PackagingGetResponseDto> responseDtoPage = new PageImpl<>(responseDtoList, pageRequest, 2);

        when(packagingClient.getPackagingPage(anyInt(), anyInt(), anyInt())).thenReturn(ResponseEntity.ok(responseDtoPage));

        ResponseEntity<Page<PackagingGetResponseDto>> response = packagingService.getPackagingPage(0, 1, 1);

        assertNotNull(response);
        assertEquals(responseDtoPage, response.getBody());

        verify(packagingClient, times(1)).getPackagingPage(anyInt(), anyInt(), anyInt());
    }

    @Test
    void getSinglePackagingTest(){
        PackagingGetResponseDto responseDto = PackagingGetResponseDto.builder().build();
        when(packagingClient.getPackagingByProductId(1L)).thenReturn(ResponseEntity.ok(responseDto));

        ResponseEntity<PackagingGetResponseDto> response = packagingService.getPackagingByProductId(1L);
        assertNotNull(response);
        assertEquals(responseDto, response.getBody());

        verify(packagingClient, times(1)).getPackagingByProductId(1L);
    }
}
