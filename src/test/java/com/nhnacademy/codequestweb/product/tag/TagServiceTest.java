package com.nhnacademy.codequestweb.product.tag;

import com.nhnacademy.codequestweb.client.product.tag.TagClient;
import com.nhnacademy.codequestweb.request.product.tag.TagRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.tag.TagUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.tag.TagGetResponseDto;
import com.nhnacademy.codequestweb.response.product.tag.TagRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.tag.TagUpdateResponseDto;
import com.nhnacademy.codequestweb.service.product.TagService;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @InjectMocks
    private TagService tagService;

    @Mock
    private TagClient tagClient;

    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void saveTagTest() {
        TagRegisterRequestDto requestDto = TagRegisterRequestDto.builder().build();
        TagRegisterResponseDto responseDto = TagRegisterResponseDto.builder().build();

        when(tagClient.saveTag(eq(headers), any()))
                .thenReturn(ResponseEntity.status(201).body(responseDto));

        ResponseEntity<TagRegisterResponseDto> response = tagService.saveTag(headers, requestDto);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(201, response.getStatusCode().value());

        verify(tagClient, times(1)).saveTag(eq(headers), any());
    }

    @Test
    void updateTagTest() {
        TagUpdateRequestDto requestDto = TagUpdateRequestDto.builder().build();
        TagUpdateResponseDto responseDto = TagUpdateResponseDto.builder().build();

        when(tagClient.updateTag(eq(headers), any()))
                .thenReturn(ResponseEntity.ok(responseDto));

        ResponseEntity<TagUpdateResponseDto> response = tagService.updateTag(headers, requestDto);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());

        verify(tagClient, times(1)).updateTag(eq(headers), any());
    }

    @Test
    void deleteTagTest() {
        when(tagClient.deleteTag(eq(headers), any()))
                .thenReturn(ResponseEntity.ok(null));

        ResponseEntity<Void> response = tagService.deleteTag(headers, 1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        verify(tagClient, times(1)).deleteTag(eq(headers), any());
    }

    @Test
    void getAllTagTest() {
        Page<TagGetResponseDto> responseDtoPage = new PageImpl<>(Arrays.asList(
                TagGetResponseDto.builder().build(),
                TagGetResponseDto.builder().build()
        ));

        when(tagClient.getAllTags(any(), any(), any()))
                .thenReturn(ResponseEntity.ok(responseDtoPage));

        ResponseEntity<Page<TagGetResponseDto>> response = tagService.getAllTags(null, null, null);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(responseDtoPage, response.getBody());
        verify(tagClient, times(1)).getAllTags(any(), any(), any());
    }

    @Test
    void getNameContainingTagTest() {
        Page<TagGetResponseDto> responseDtoPage = new PageImpl<>(Arrays.asList(
                TagGetResponseDto.builder().build(),
                TagGetResponseDto.builder().build()
        ));

        when(tagClient.getNameContainingTagPage(any(), any(), any(), eq("test")))
                .thenReturn(ResponseEntity.ok(responseDtoPage));

        ResponseEntity<Page<TagGetResponseDto>> response = tagService.getNameContainingTagPage(null, null, null, "test");

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(responseDtoPage, response.getBody());
        verify(tagClient, times(1)).getNameContainingTagPage(any(), any(), any(), eq("test"));
    }
}
