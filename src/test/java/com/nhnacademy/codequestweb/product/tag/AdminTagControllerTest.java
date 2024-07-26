package com.nhnacademy.codequestweb.product.tag;

import com.nhnacademy.codequestweb.controller.product.admin.AdminTagController;
import com.nhnacademy.codequestweb.response.product.tag.TagGetResponseDto;
import com.nhnacademy.codequestweb.response.product.tag.TagRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.tag.TagUpdateResponseDto;
import com.nhnacademy.codequestweb.service.product.TagService;
import feign.FeignException;
import feign.Request;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class AdminTagControllerTest {
    @InjectMocks
    private AdminTagController adminTagController;

    @Mock
    private TagService tagService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminTagController).build();
    }

    @Test
    void getAllTagsTest1() throws Exception {
        TagGetResponseDto tagGetResponseDto1 = TagGetResponseDto.builder().build();
        TagGetResponseDto tagGetResponseDto2 = TagGetResponseDto.builder().build();

        Page<TagGetResponseDto> tagGetResponseDtoPage = new PageImpl<>(Arrays.asList(tagGetResponseDto1, tagGetResponseDto2));

        when(tagService.getAllTags(any(), any(),any(),any())).thenReturn(ResponseEntity.ok(tagGetResponseDtoPage));

        mockMvc.perform(get("/admin/tags"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalCount", 2L))
                .andExpect(model().attributeDoesNotExist("empty"))
                .andExpect(model().attribute("url", "/admin/tags?"));
    }

    @Test
    void getAllTagsTest2() throws Exception {

        when(tagService.getAllTags(any(), any(),any(),any())).thenThrow(FeignException.class);

        mockMvc.perform(get("/admin/tags"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/client/0"))
                .andExpect(flash().attribute("alterMessage","태그 정보 조회에 실패했습니다."));

    }

    @Test
    void getAllTagsTest3() throws Exception {

        when(tagService.getAllTags(any(), any(),any(),any())).thenReturn(ResponseEntity.ok(null));

        mockMvc.perform(get("/admin/tags"))

                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/client/0"))
                .andExpect(flash().attribute("alterMessage","응답 본문이 비어있습니다."));
    }

    @Test
    void getAllTagsTest4() throws Exception {

        when(tagService.getAllTags(any(), any(),any(),any())).thenReturn(null);

        mockMvc.perform(get("/admin/tags"))

                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/client/0"))
                .andExpect(flash().attribute("alterMessage","응답 자체가 비어있습니다."));
    }

    @Test
    void getNameContainingTagsTest1() throws Exception {
        TagGetResponseDto tagGetResponseDto1 = TagGetResponseDto.builder().build();
        TagGetResponseDto tagGetResponseDto2 = TagGetResponseDto.builder().build();

        Page<TagGetResponseDto> tagGetResponseDtoPage = new PageImpl<>(Arrays.asList(tagGetResponseDto1, tagGetResponseDto2));

        when(tagService.getNameContainingTagPage(any(), any(),any(),any(), eq("test"))).thenReturn(ResponseEntity.ok(tagGetResponseDtoPage));

        mockMvc.perform(get("/admin/tags/containing")
                        .param("tagName","test"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalCount", 2L))
                .andExpect(model().attributeDoesNotExist("empty"))
                .andExpect(model().attribute("url", "/admin/tags/containing?tagName=test&"));
    }

    @Test
    void getNameContainingTagsTest2() throws Exception {

        when(tagService.getNameContainingTagPage(any(), any(),any(),any(), eq("test"))).thenThrow(FeignException.class);

        mockMvc.perform(get("/admin/tags/containing")
                        .param("tagName","test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/client/0"))
                .andExpect(flash().attribute("alterMessage","태그 정보 조회에 실패했습니다."));

    }

    @Test
    void getNameContainingTagsTest3() throws Exception {

        when(tagService.getNameContainingTagPage(any(), any(),any(),any(), eq("test"))).thenReturn(ResponseEntity.ok(null));

        mockMvc.perform(get("/admin/tags/containing")
                        .param("tagName","test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/client/0"))
                .andExpect(flash().attribute("alterMessage","응답 본문이 비어있습니다."));
    }

    @Test
    void getNameContainingTagsTest4() throws Exception {

        when(tagService.getNameContainingTagPage(any(), any(),any(),any(), eq("test"))).thenReturn(null);

        mockMvc.perform(get("/admin/tags/containing")
                        .param("tagName","test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/client/0"))
                .andExpect(flash().attribute("alterMessage","응답 자체가 비어있습니다."));
    }

    @Test
    void getAllTagsModalTest1() throws Exception {

        TagGetResponseDto tagGetResponseDto1 = TagGetResponseDto.builder().build();
        TagGetResponseDto tagGetResponseDto2 = TagGetResponseDto.builder().build();

        Page<TagGetResponseDto> tagGetResponseDtoPage = new PageImpl<>(Arrays.asList(tagGetResponseDto1, tagGetResponseDto2));

        when(tagService.getAllTags(any(), any(),any(),any())).thenReturn(ResponseEntity.ok(tagGetResponseDtoPage));


        mockMvc.perform(get("/tags/all"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("pageNumbers","tagPage"))
                .andExpect(model().attribute("url", "/tags/all?page="));
    }

    @Test
    void getAllTagsModalTest2() throws Exception {

        when(tagService.getAllTags(any(), any(),any(),any())).thenThrow(FeignException.class);

        mockMvc.perform(get("/tags/all"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("pageNumbers","tagPage"))
                .andExpect(view().name("view/product/window.close"));
    }

    @Test
    void getAllTagsModalTest3() throws Exception {

        when(tagService.getAllTags(any(), any(),any(),any())).thenReturn(ResponseEntity.ok(null));

        mockMvc.perform(get("/tags/all"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("pageNumbers","tagPage"))
                .andExpect(view().name("view/product/window.close"));
    }

    @Test
    void getAllTagsModalTest4() throws Exception {

        when(tagService.getAllTags(any(), any(),any(),any())).thenReturn(null);

        mockMvc.perform(get("/tags/all"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("pageNumbers","tagPage"))
                .andExpect(view().name("view/product/window.close"));
    }

    @Test
    void getNameContainingTagsModalTest1() throws Exception {

        TagGetResponseDto tagGetResponseDto1 = TagGetResponseDto.builder().build();
        TagGetResponseDto tagGetResponseDto2 = TagGetResponseDto.builder().build();

        Page<TagGetResponseDto> tagGetResponseDtoPage = new PageImpl<>(Arrays.asList(tagGetResponseDto1, tagGetResponseDto2));

        when(tagService.getNameContainingTagPage(any(), any(),any(),any(), eq("test"))).thenReturn(ResponseEntity.ok(tagGetResponseDtoPage));


        mockMvc.perform(get("/tags/containing")
                        .param("tagName","test"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("pageNumbers","tagPage"))
                .andExpect(model().attribute("url", "/tags/containing?tagName=test&page="));
    }

    @Test
    void getNameContainingTagsModalTest2() throws Exception {

        when(tagService.getNameContainingTagPage(any(), any(),any(),any(), eq("test"))).thenThrow(FeignException.class);

        mockMvc.perform(get("/tags/containing")
                        .param("tagName","test"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("pageNumbers","tagPage"))
                .andExpect(view().name("view/product/window.close"));
    }

    @Test
    void getNameContainingTagsModalTest3() throws Exception {

        when(tagService.getNameContainingTagPage(any(), any(),any(),any(), eq("test"))).thenReturn(ResponseEntity.ok(null));

        mockMvc.perform(get("/tags/containing")
                        .param("tagName","test"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("pageNumbers","tagPage"))
                .andExpect(view().name("view/product/window.close"));
    }

    @Test
    void getNameContainingTagsModalTest4() throws Exception {

        when(tagService.getNameContainingTagPage(any(), any(),any(),any(), eq("test"))).thenReturn(null);

        mockMvc.perform(get("/tags/containing")
                        .param("tagName","test"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("pageNumbers","tagPage"))
                .andExpect(view().name("view/product/window.close"));
    }

    @Test
    void saveTagTest1() throws Exception {
        TagRegisterResponseDto responseDto = TagRegisterResponseDto.builder().build();
        when(tagService.saveTag(any(),any()))
                .thenReturn(ResponseEntity.status(201).body(responseDto));

        mockMvc.perform(post("/admin/tags/register"))
                .andExpect(status().isCreated());
    }

    @Test
    void saveTagTest2() throws Exception {
        Request req = mock(Request.class);

        when(tagService.saveTag(any(),any()))
                .thenThrow(new FeignException.BadRequest(null, req,null, null));

        mockMvc.perform(post("/admin/tags/register"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTagTest1() throws Exception {
        TagUpdateResponseDto responseDto = TagUpdateResponseDto.builder().build();
        when(tagService.updateTag(any(),any()))
                .thenReturn(ResponseEntity.status(200).body(responseDto));

        mockMvc.perform(put("/admin/tags/update"))
                .andExpect(status().isOk());
    }

    @Test
    void updateTagTest2() throws Exception {
        Request req = mock(Request.class);

        when(tagService.updateTag(any(),any()))
                .thenThrow(new FeignException.BadRequest(null, req,null, null));

        mockMvc.perform(put("/admin/tags/update"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteTagTest1() throws Exception {
        when(tagService.deleteTag(any(),eq(1L)))
                .thenReturn(ResponseEntity.status(200).body(null));

        mockMvc.perform(delete("/admin/tags/delete/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTagTest2() throws Exception {
        Request req = mock(Request.class);

        when(tagService.deleteTag(any(),eq(1L)))
                .thenThrow(new FeignException.BadRequest(null, req,null, null));

        mockMvc.perform(delete("/admin/tags/delete/1"))
                .andExpect(status().isBadRequest());
    }
}
