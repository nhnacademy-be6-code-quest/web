package com.nhnacademy.codequestweb.product.packaging;

import com.nhnacademy.codequestweb.controller.product.admin.AdminPackagingController;
import com.nhnacademy.codequestweb.exception.review.FileSaveException;
import com.nhnacademy.codequestweb.response.product.common.ProductRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductUpdateResponseDto;
import com.nhnacademy.codequestweb.response.product.packaging.PackagingGetResponseDto;
import com.nhnacademy.codequestweb.service.image.ImageService;
import com.nhnacademy.codequestweb.service.product.PackagingService;
import feign.FeignException;
import java.time.LocalDateTime;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class AdminPackagingControllerTest {
    @InjectMocks
    private AdminPackagingController adminPackagingController;

    @Mock
    private PackagingService packagingService;

    @Mock
    private ImageService imageService;

    private MockMvc mockMvc;

    private PageRequest pageRequest;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminPackagingController).build();

        pageRequest = PageRequest.of(0, 10);
    }

    @Test
    void getPackagingPageTest1() throws Exception {
        List<PackagingGetResponseDto> packagingGetResponseDtoList = Arrays.asList(
                PackagingGetResponseDto.builder().build(),
                PackagingGetResponseDto.builder().build(),
                PackagingGetResponseDto.builder().build()
        );

        Page<PackagingGetResponseDto> responseDtoPage = new PageImpl<>(packagingGetResponseDtoList, pageRequest, 5);

        when(packagingService.getPackagingPageForAdmin (any(), eq(0), eq(1), eq(10))).thenReturn(ResponseEntity.ok(responseDtoPage));

        mockMvc.perform(get("/admin/product/packaging/page")
                .param("productState","0")
                .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("totalPage", 1));
    }

    @Test
    void getPackagingPageTest2() throws Exception {
        when(packagingService.getPackagingPageForAdmin (any(), eq(0), eq(1), eq(10))).thenReturn(ResponseEntity.ok(null));

        mockMvc.perform(get("/admin/product/packaging/page")
                        .param("productState","0")
                        .param("page", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/client/0"))
                .andExpect(flash().attribute("alterMessage","응답 본문이 비어있습니다."));
    }

    @Test
    void getPackagingPageTest3() throws Exception {
        when(packagingService.getPackagingPageForAdmin(any(), eq(0), eq(1), eq(10))).thenThrow(FeignException.class);

        mockMvc.perform(get("/admin/product/packaging/page")
                        .param("productState","0")
                        .param("page", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/client/0"))
                .andExpect(flash().attribute("alterMessage","포장지 조회에 실패했습니다."));
    }

    @Test
    void getRegisterFormTest1() throws Exception {
        when(packagingService.roleCheck(any())).thenReturn(ResponseEntity.ok(null));

        mockMvc.perform(get("/admin/product/packaging/registerForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(request().attribute("action", "register"));
    }

    @Test
    void getRegisterFormTest2() throws Exception {
        when(packagingService.roleCheck(any())).thenReturn(ResponseEntity.badRequest().build());

        mockMvc.perform(get("/admin/product/packaging/registerForm"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/client/0"))
                .andExpect(flash().attribute("alterMessage", "일시적으로 오류가 발생했습니다.\n 문제가 지속된다면 로그를 확인하세요."));

    }

    @Test
    void getRegisterFormTest3() throws Exception {
        when(packagingService.roleCheck(any())).thenThrow(FeignException.class);

        mockMvc.perform(get("/admin/product/packaging/registerForm"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/client/0"))
                .andExpect(flash().attribute("alterMessage", "일시적으로 오류가 발생했습니다.\n 문제가 지속된다면 로그를 확인하세요."));
    }

    @Test
    void getUpdateFormTest1() throws Exception {
        PackagingGetResponseDto packagingGetResponseDto = PackagingGetResponseDto.builder().build();
        when(packagingService.getPackagingByProductIdForAdmin (any(), eq(1L))).thenReturn(ResponseEntity.ok(packagingGetResponseDto));

        mockMvc.perform(get("/admin/product/packaging/updateForm/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(request().attribute("action", "update"));
    }

    @Test
    void getUpdateFormTest2() throws Exception {
        when(packagingService.getPackagingByProductIdForAdmin(any(), eq(1L))).thenReturn(ResponseEntity.ok(null));

        mockMvc.perform(get("/admin/product/packaging/updateForm/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/client/0"))
                .andExpect(flash().attribute("alterMessage","응답 본문이 비어있습니다."));
    }

    @Test
    void getUpdateFormTest3() throws Exception {

        when(packagingService.getPackagingByProductIdForAdmin(any(), eq(1L))).thenThrow(FeignException.class);

        mockMvc.perform(get("/admin/product/packaging/updateForm/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/client/0"))
                .andExpect(flash().attribute("alterMessage","포장지 조회에 실패했습니다."));
    }

    @Test
    void savePackagingTest1() throws Exception {

        MockMultipartFile
                file = new MockMultipartFile("image", "cover.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());

        ProductRegisterResponseDto responseDto = new ProductRegisterResponseDto(1L, LocalDateTime.now());

        when(imageService.uploadImage(any(MultipartFile.class))).thenReturn("success");

        when(packagingService.savePackaging(any(), any()))
                .thenReturn(new ResponseEntity<>(responseDto, null, HttpStatus.CREATED));

        mockMvc.perform(multipart("/admin/product/packaging/register")
                        .file(file))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/product/packaging/page"))
                .andExpect(flash().attribute("alterMessage", "성공적으로 등록되었습니다."));
    }

    @Test
    void savePackagingTest2() throws Exception {

        MockMultipartFile
                file = new MockMultipartFile("image", "cover.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());

        ProductRegisterResponseDto responseDto = new ProductRegisterResponseDto(1L, LocalDateTime.now());

        when(imageService.uploadImage(any(MultipartFile.class))).thenThrow(FileSaveException.class);

        when(packagingService.savePackaging(any(), any()))
                .thenReturn(new ResponseEntity<>(responseDto, null, HttpStatus.CREATED));

        mockMvc.perform(multipart("/admin/product/packaging/register")
                        .file(file))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/product/packaging/page"))
                .andExpect(flash().attribute("alterMessage", "포장지는 등록되었지만, 포장지 이미지를 저장하는 과정에서 문제가 발생했습니다.\n 자세한 정보는 로그를 확인하세요."));
    }

    @Test
    void savePackagingTest3() throws Exception {

        MockMultipartFile
                file = new MockMultipartFile("image", "cover.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());

        when(packagingService.savePackaging(any(), any()))
                .thenThrow(FeignException.class);

        mockMvc.perform(multipart("/admin/product/packaging/register")
                        .file(file))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/product/packaging/page"))
                .andExpect(flash().attribute("alterMessage", "포장지를 등록하는 데 실패했습니다.\n자세한 정보는 로그를 확인하세요."));
    }

    @Test
    void updatePackagingTest1() throws Exception {

        MockMultipartFile
                file = new MockMultipartFile("image", "cover.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());

        ProductUpdateResponseDto responseDto = new ProductUpdateResponseDto(LocalDateTime.now());

        when(imageService.uploadImage(any(MultipartFile.class))).thenReturn("success");

        when(packagingService.updatePackaging(any(), any()))
                .thenReturn(new ResponseEntity<>(responseDto, null, HttpStatus.OK));

        mockMvc.perform(multipart("/admin/product/packaging/update")
                        .file(file)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/product/packaging/page"))
                .andExpect(flash().attribute("alterMessage", "성공적으로 수정되었습니다."));
    }

    @Test
    void updatePackagingTest2() throws Exception {

        MockMultipartFile
                file = new MockMultipartFile("image", "cover.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());

        ProductUpdateResponseDto responseDto = new ProductUpdateResponseDto(LocalDateTime.now());

        when(imageService.uploadImage(any(MultipartFile.class))).thenThrow(FileSaveException.class);

        when(packagingService.updatePackaging(any(), any()))
                .thenReturn(new ResponseEntity<>(responseDto, null, HttpStatus.OK));

        mockMvc.perform(multipart("/admin/product/packaging/update")
                        .file(file)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/product/packaging/page"))
                .andExpect(flash().attribute("alterMessage", "수정은 이루어졌지만, 포장지 이미지를 저장하는 과정에서 문제가 발생했습니다.\n 자세한 정보는 로그를 확인하세요."));
    }

    @Test
    void updatePackagingTest3() throws Exception {

        MockMultipartFile
                file = new MockMultipartFile("image", "cover.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());

        when(packagingService.updatePackaging(any(), any()))
                .thenThrow(FeignException.class);

        mockMvc.perform(multipart("/admin/product/packaging/update")
                        .file(file)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/product/packaging/page"))
                .andExpect(flash().attribute("alterMessage", "포장지를 수정하는 데 실패했습니다.\n자세한 정보는 로그를 확인하세요."));
    }
}
