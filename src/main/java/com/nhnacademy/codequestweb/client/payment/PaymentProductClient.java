package com.nhnacademy.codequestweb.client.payment;

import com.nhnacademy.codequestweb.request.product.common.InventoryDecreaseRequestDto;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "paymentProduct", url = "http://localhost:8001")
public interface PaymentProductClient {

    @PutMapping("api/product/inventory/decrease")
    ResponseEntity<Void> decreaseProductInventory(
        @RequestBody @Valid List<InventoryDecreaseRequestDto> inventoryDecreaseRequestDtoList);
}