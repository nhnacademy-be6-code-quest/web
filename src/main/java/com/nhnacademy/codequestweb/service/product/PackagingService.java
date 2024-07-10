package com.nhnacademy.codequestweb.service.product;

import com.nhnacademy.codequestweb.client.product.packaging.PackagingClient;
import com.nhnacademy.codequestweb.request.product.packaging.PackagingRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.packaging.PackagingUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.common.ProductRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductUpdateResponseDto;
import com.nhnacademy.codequestweb.response.product.packaging.PackagingGetResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PackagingService {
    private final PackagingClient packagingClient;

    public ResponseEntity<ProductRegisterResponseDto> savePackaging(
            HttpHeaders headers,
            PackagingRegisterRequestDto requestDto) {
        return packagingClient.savePackaging(headers, requestDto);
    }

    public ResponseEntity<ProductUpdateResponseDto> updatePackaging(
            HttpHeaders headers,
            PackagingUpdateRequestDto requestDto){
        return packagingClient.updatePackaging(headers, requestDto);
    }

    public ResponseEntity<List<PackagingGetResponseDto>> getPackaging(
            Integer productState){
        return packagingClient.getPackaging(productState);
    }

    public ResponseEntity<PackagingGetResponseDto> getPackagingByProductId(
            Long productId){
        return packagingClient.getPackagingByProductId(productId);
    }
}
