package com.nhnacademy.codequestweb.service.product;

import com.nhnacademy.codequestweb.client.product.packaging.PackagingClient;
import com.nhnacademy.codequestweb.request.product.packaging.PackagingRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.packaging.PackagingUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.common.ProductRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductUpdateResponseDto;
import com.nhnacademy.codequestweb.response.product.packaging.PackagingGetResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PackagingService {
    private final PackagingClient packagingClient;

    public ResponseEntity<Void> roleCheck(
            HttpHeaders headers){
        return packagingClient.roleCheck(headers);
    }

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

    public ResponseEntity<List<PackagingGetResponseDto>> getPackagingList(
            Integer productState){
        return packagingClient.getPackagingList(productState);
    }

    public ResponseEntity<Page<PackagingGetResponseDto>> getPackagingPage(
            Integer productState, int page, int size){
        return packagingClient.getPackagingPage(productState, page, size);
    }

    public ResponseEntity<Page<PackagingGetResponseDto>> getPackagingPageForAdmin(
            HttpHeaders headers,
            Integer productState, int page, int size){
        return packagingClient.getPackagingPageForAdmin(headers, productState, page, size);
    }

    public ResponseEntity<PackagingGetResponseDto> getPackagingByProductIdForAdmin(
            HttpHeaders headers,
            Long productId){
        return packagingClient.getPackagingByProductId(headers, productId);
    }
}
