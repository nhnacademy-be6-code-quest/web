package com.nhnacademy.codequestweb.client.order;

import com.nhnacademy.codequestweb.request.payment.PaymentOrderApproveRequestDto;
import com.nhnacademy.codequestweb.request.payment.PaymentOrderShowRequestDto;
import com.nhnacademy.codequestweb.response.order.client.ClientOrderForm;
import com.nhnacademy.codequestweb.response.order.client.ClientOrderGetResponseDto;
import com.nhnacademy.codequestweb.response.order.common.OrderResponseDto;
import com.nhnacademy.codequestweb.response.order.common.ProductOrderDetailOptionResponseDto;
import com.nhnacademy.codequestweb.response.order.common.ProductOrderDetailResponseDto;
import com.nhnacademy.codequestweb.response.order.nonclient.NonClientOrderForm;
import com.nhnacademy.codequestweb.response.order.nonclient.NonClientOrderGetResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 주문 클라이언트
 * @author 박희원(bakhuiwon326)
 **/

@FeignClient(name = "order", url = "http://localhost:8001")
public interface OrderClient {



    // @ 회원 주문 컨트롤러 @

    // 회원 주문 생성
    @PostMapping("/api/client/orders")
    ResponseEntity<Long> createClientOrder(@RequestHeader HttpHeaders headers, @RequestBody ClientOrderForm clientOrderForm);

    // 회원 주문 내역 리스트 조회
    @GetMapping("/api/client/orders")
    ResponseEntity<Page<ClientOrderGetResponseDto>> getClientOrders(
            @RequestHeader HttpHeaders headers,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "sortBy", defaultValue = "orderDatetime", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir);

    // 회원 주문 단건 조회
    @GetMapping("/api/client/orders/{orderId}")
    ResponseEntity<ClientOrderGetResponseDto> getClientOrder(@RequestHeader HttpHeaders headers, @PathVariable long orderId);

    // 회원 주문 결제 완료 상태 변경
    @PutMapping("/api/client/orders/{orderId}/payment-complete")
    ResponseEntity<String> paymentCompleteClientOrder(@RequestHeader HttpHeaders headers, @PathVariable long orderId);

    // 회원 주문 취소 상태 변경
    @PutMapping("/api/client/orders/{orderId}/cancel")
    ResponseEntity<String> cancelClientOrder(@RequestHeader HttpHeaders headers, @PathVariable long orderId);

    // 회원 주문 환불 상태 변경
    @PutMapping("/api/client/orders/{orderId}/refund")
    ResponseEntity<String> refundClientOrder(@RequestHeader HttpHeaders headers, @PathVariable long orderId);

    // 회원 주문 상품 상세 리스트 조회
    @GetMapping("/api/client/orders/{orderId}/detail")
    ResponseEntity<List<ProductOrderDetailResponseDto>> getClientProductOrderDetailList(@RequestHeader HttpHeaders headers, @PathVariable Long orderId);

    // 회원 주문 상품 상세 단건 조회
    @GetMapping("/api/client/orders/{orderId}/detail/{productOrderDetailId}")
    ResponseEntity<ProductOrderDetailResponseDto> getClientProductOrderDetail(@RequestHeader HttpHeaders headers, @PathVariable Long orderId,
                                                                               @PathVariable Long productOrderDetailId);

    // 회원 주문 상품 상세 옵션 조회
    @GetMapping("/api/client/orders/{orderId}/detail/{productOrderDetailId}/option")
    ResponseEntity<ProductOrderDetailOptionResponseDto> getClientProductOrderDetailOption(@RequestHeader HttpHeaders headers, @PathVariable Long orderId,
                                                                                           @PathVariable Long productOrderDetailId);




    // @ 비회원 주문 컨트롤러 @

    // 비회원 주문 생성
    @PostMapping("/api/non-client/orders")
    ResponseEntity<Long> createNonClientOrder(@RequestHeader HttpHeaders headers, @RequestBody NonClientOrderForm nonClientOrderForm);

    // 비회원 주문 결제 완료 상태 변경
    @PutMapping("/api/non-client/orders/{orderId}/payment-complete")
    ResponseEntity<String> paymentCompleteNonClientOrder(@RequestHeader HttpHeaders headers, @PathVariable long orderId);

    // 비회원 주문 취소 상태 변경
    @PutMapping("/api/non-client/orders/{orderId}/cancel")
    ResponseEntity<String> cancelNonClientOrder(@RequestHeader HttpHeaders headers, @PathVariable long orderId);

    // 비회원 주문 환불 상태 변경
    @PutMapping("/api/non-client/orders/{orderId}/refund")
    ResponseEntity<String> refundNonClientOrder(@RequestHeader HttpHeaders headers, @PathVariable long orderId);

    // 비회원 주문 단건 조회
    @GetMapping("/api/non-client/orders/{orderId}")
    ResponseEntity<NonClientOrderGetResponseDto> findNonClientOrder(@RequestHeader HttpHeaders headers, @PathVariable long orderId, @RequestParam("pwd") String orderPassword);




    // @ 그 외 컨트롤러 @

    // 주문상태 변경 - 회원, 비회원 구분 없이 바꿔주는!
    @PutMapping("/api/order/{orderId}")
    ResponseEntity<String> updateOrderStatus(@RequestHeader HttpHeaders headers, @PathVariable(name = "orderId") Long orderId, @RequestParam(name = "status", required = true) String status);

    // 결제 서비스의 '결제 요청'에 필요한 주문 정보 제공
    @GetMapping("/api/order/{orderId}/payment-request")
    ResponseEntity<PaymentOrderShowRequestDto> getPaymentOrderShowRequestDto(@RequestHeader HttpHeaders headers, @PathVariable Long orderId);

    // 결제서비스에 '결제 승인'에 필요한 주문 정보 제공
    @GetMapping("/api/order/{orderId}/approve-request")
    ResponseEntity<PaymentOrderApproveRequestDto> getPaymentOrderApproveRequestDto(@RequestHeader HttpHeaders headers, @PathVariable Long orderId);

    // 모든 주문 가져오기 (관리자 사용)
    @GetMapping("/api/order/all")
    ResponseEntity<Page<OrderResponseDto>> getOrder(@RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
                                                           @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                           @RequestParam(value = "sortBy", defaultValue = "orderDatetime", required = false) String sortBy,
                                                           @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir);

    @GetMapping("/api/order/{orderId}/detail")
    ResponseEntity<List<ProductOrderDetailResponseDto>> getProductOrderDetailList(@PathVariable Long orderId);

    @GetMapping("/api/order/{orderId}/detail/{productOrderDetailId}")
    ResponseEntity<ProductOrderDetailResponseDto> getProductOrderDetail(@PathVariable Long orderId, @PathVariable Long productOrderDetailId);

    @GetMapping("/api/order/{orderId}/detail/{productOrderDetailId}/option")
    ResponseEntity<ProductOrderDetailOptionResponseDto> getProductOrderDetailOption(@PathVariable Long orderId, @PathVariable Long productOrderDetailId);

}