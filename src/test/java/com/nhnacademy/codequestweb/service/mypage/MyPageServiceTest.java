package com.nhnacademy.codequestweb.service.mypage;

import com.nhnacademy.codequestweb.client.auth.UserClient;
import com.nhnacademy.codequestweb.client.product.bookProduct.BookProductClient;
import com.nhnacademy.codequestweb.client.review.ReviewClient;
import com.nhnacademy.codequestweb.request.auth.ClientRegisterRequestDto;
import com.nhnacademy.codequestweb.request.mypage.ClientRegisterAddressRequestDto;
import com.nhnacademy.codequestweb.request.mypage.ClientRegisterPhoneNumberRequestDto;
import com.nhnacademy.codequestweb.request.mypage.ClientUpdatePrivacyRequestDto;
import com.nhnacademy.codequestweb.response.auth.ClientRegisterResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientDeliveryAddressResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientPhoneNumberResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientPrivacyResponseDto;
import com.nhnacademy.codequestweb.response.product.book.BookProductGetResponseDto;
import com.nhnacademy.codequestweb.response.review.ReviewInfoResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MyPageServiceTest {

    @Mock
    private UserClient userClient;

    @Mock
    private ReviewClient reviewClient;

    @Mock
    private BookProductClient bookProductClient;

    @InjectMocks
    private MyPageService myPageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        ClientRegisterRequestDto requestDto = new ClientRegisterRequestDto();
        ResponseEntity<ClientRegisterResponseDto> expectedResponse = ResponseEntity.ok(new ClientRegisterResponseDto("test@email.com", LocalDateTime.now()));
        when(userClient.register(requestDto)).thenReturn(expectedResponse);

        ResponseEntity<ClientRegisterResponseDto> result = myPageService.register(requestDto);

        assertEquals(expectedResponse, result);
        verify(userClient).register(requestDto);
    }

    @Test
    void testGetPrivacy() {
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<ClientPrivacyResponseDto> expectedResponse = ResponseEntity.ok(new ClientPrivacyResponseDto());
        when(userClient.getPrivacy(headers)).thenReturn(expectedResponse);

        ResponseEntity<ClientPrivacyResponseDto> result = myPageService.getPrivacy(headers);

        assertEquals(expectedResponse, result);
        verify(userClient).getPrivacy(headers);
    }

    @Test
    void testGetDeliveryAddresses() {
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<List<ClientDeliveryAddressResponseDto>> expectedResponse = ResponseEntity.ok(Collections.emptyList());
        when(userClient.getDeliveryAddresses(headers)).thenReturn(expectedResponse);

        ResponseEntity<List<ClientDeliveryAddressResponseDto>> result = myPageService.getDeliveryAddresses(headers);

        assertEquals(expectedResponse, result);
        verify(userClient).getDeliveryAddresses(headers);
    }

    @Test
    void testRegisterAddress() {
        HttpHeaders headers = new HttpHeaders();
        ClientRegisterAddressRequestDto requestDto = new ClientRegisterAddressRequestDto();
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("Success");
        when(userClient.registerAddress(headers, requestDto)).thenReturn(expectedResponse);

        ResponseEntity<String> result = myPageService.registerAddress(headers, requestDto);

        assertEquals(expectedResponse, result);
        verify(userClient).registerAddress(headers, requestDto);
    }

    @Test
    void testDeleteDeliveryAddress() {
        HttpHeaders headers = new HttpHeaders();
        Long addressId = 1L;
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("Deleted");
        when(userClient.deleteAddress(headers, addressId)).thenReturn(expectedResponse);

        ResponseEntity<String> result = myPageService.deleteDeliveryAddress(headers, addressId);

        assertEquals(expectedResponse, result);
        verify(userClient).deleteAddress(headers, addressId);
    }

    @Test
    void testDeleteClient() {
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("Deleted");
        when(userClient.deleteClient(headers)).thenReturn(expectedResponse);

        ResponseEntity<String> result = myPageService.deleteClient(headers);

        assertEquals(expectedResponse, result);
        verify(userClient).deleteClient(headers);
    }

    @Test
    void testGetPhoneNumbers() {
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<List<ClientPhoneNumberResponseDto>> expectedResponse = ResponseEntity.ok(Collections.emptyList());
        when(userClient.getPhoneNumber(headers)).thenReturn(expectedResponse);

        ResponseEntity<List<ClientPhoneNumberResponseDto>> result = myPageService.getPhoneNumbers(headers);

        assertEquals(expectedResponse, result);
        verify(userClient).getPhoneNumber(headers);
    }

    @Test
    void testRegisterPhoneNumbers() {
        HttpHeaders headers = new HttpHeaders();
        ClientRegisterPhoneNumberRequestDto requestDto = new ClientRegisterPhoneNumberRequestDto();
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("Registered");
        when(userClient.registerPhoneNumber(headers, requestDto)).thenReturn(expectedResponse);

        ResponseEntity<String> result = myPageService.registerPhoneNumbers(headers, requestDto);

        assertEquals(expectedResponse, result);
        verify(userClient).registerPhoneNumber(headers, requestDto);
    }

    @Test
    void testDeletePhoneNumber() {
        HttpHeaders headers = new HttpHeaders();
        Long phoneNumberId = 1L;
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("Deleted");
        when(userClient.deletePhoneNumber(headers, phoneNumberId)).thenReturn(expectedResponse);

        ResponseEntity<String> result = myPageService.deletePhoneNumber(headers, phoneNumberId);

        assertEquals(expectedResponse, result);
        verify(userClient).deletePhoneNumber(headers, phoneNumberId);
    }

    @Test
    void testUpdateClient() {
        HttpHeaders headers = new HttpHeaders();
        ClientUpdatePrivacyRequestDto requestDto = new ClientUpdatePrivacyRequestDto();
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("Updated");
        when(userClient.updateClient(headers, requestDto)).thenReturn(expectedResponse);

        ResponseEntity<String> result = myPageService.updateClient(headers, requestDto);

        assertEquals(expectedResponse, result);
        verify(userClient).updateClient(headers, requestDto);
    }

    @Test
    void testGetMyReviewInfo() {
        String access = "token";
        int page = 0;
        int size = 10;
        ReviewInfoResponseDto reviewInfo = new ReviewInfoResponseDto();
        reviewInfo.setProductId(1L);
        Page<ReviewInfoResponseDto> reviewPage = new PageImpl<>(Collections.singletonList(reviewInfo));
        BookProductGetResponseDto bookInfo = BookProductGetResponseDto.builder()
                .productId(1L)
                .productName("Book Name")
                .build();

        when(reviewClient.getMyReviews(page, size, access)).thenReturn(ResponseEntity.ok(reviewPage));
        when(bookProductClient.getSingleBookInfo(null, 1L)).thenReturn(ResponseEntity.ok(bookInfo));

        Page<ReviewInfoResponseDto> result = myPageService.getMyReviewInfo(access, page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Book Name", result.getContent().get(0).getProductName());
        verify(reviewClient).getMyReviews(page, size, access);
        verify(bookProductClient).getSingleBookInfo(null, 1L);
    }
}