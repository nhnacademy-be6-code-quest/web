package com.nhnacademy.codequestweb.client.product.packaging;


import com.nhnacademy.codequestweb.request.product.packaging.PackagingRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.packaging.PackagingUpdateRequestDto;
import com.nhnacademy.codequestweb.response.coupon.ProductGetResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductUpdateResponseDto;
import com.nhnacademy.codequestweb.response.product.packaging.PackagingGetResponseDto;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "packaging", url = "http://localhost:8001")
public interface PackagingClient {

    @PostMapping("/api/product/admin/packaging/register")
    ResponseEntity<ProductRegisterResponseDto> savePackaging(
            @RequestHeader HttpHeaders headers,
            @RequestBody @Valid PackagingRegisterRequestDto requestDto);

    @PutMapping("/api/product/admin/packaging/update")
    ResponseEntity<ProductUpdateResponseDto> updatePackaging(
            @RequestHeader HttpHeaders headers,
            @RequestBody @Valid PackagingUpdateRequestDto requestDto);

    @GetMapping("/api/product/packaging/all")
    ResponseEntity<List<PackagingGetResponseDto>> getPackaging(
            @RequestParam(name = "productState", required = false) Integer productState
    );

    @GetMapping("/api/product/packaging/single/byProduct/{productId}")
    ResponseEntity<PackagingGetResponseDto> getPackagingByProductId(
            @PathVariable("productId") Long productId
    );

}
