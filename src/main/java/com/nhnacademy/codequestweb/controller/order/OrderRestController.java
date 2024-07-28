package com.nhnacademy.codequestweb.controller.order;

import com.nhnacademy.codequestweb.request.mypage.ClientRegisterAddressRequestDto;
import com.nhnacademy.codequestweb.request.mypage.ClientRegisterPhoneNumberRequestDto;
import com.nhnacademy.codequestweb.request.order.nonclient.FindNonClientOrderIdRequestDto;
import com.nhnacademy.codequestweb.request.order.nonclient.UpdateNonClientOrderPasswordRequestDto;
import com.nhnacademy.codequestweb.response.mypage.ClientDeliveryAddressResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientPhoneNumberResponseDto;
import com.nhnacademy.codequestweb.response.order.nonclient.FindNonClientOrderIdInfoResponseDto;
import com.nhnacademy.codequestweb.response.order.nonclient.NonClientOrderGetResponseDto;
import com.nhnacademy.codequestweb.service.order.OrderService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderRestController {

    private final OrderService orderService;

    @GetMapping("/non-client/order")
    public ResponseEntity<NonClientOrderGetResponseDto> nonClientFindOrder(HttpServletRequest req,
        @RequestParam("orderId") String orderIdStr,
        @RequestParam("orderPassword") String orderPassword) {

        Long orderId = null;
        NonClientOrderGetResponseDto nonClientOrderGetResponseDto = null;
        try {
            Long.parseLong(orderIdStr);
            HttpHeaders headers = new HttpHeaders();
            headers.set("access", CookieUtils.getCookieValue(req, "access"));
            orderId = Long.parseLong(orderIdStr);
            nonClientOrderGetResponseDto = orderService.findNonClientOrder(headers, orderId,
                orderPassword);
        } catch (NumberFormatException e) {
            log.warn("입력한 주문 아이디를 정수형으로 파싱하는데 실패했습니다.");
            return ResponseEntity.badRequest().build();
        } catch (FeignException.NotFound e) {
            log.warn("주문 조회 실패");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(nonClientOrderGetResponseDto);

    }

    @GetMapping("/non-client/order/orderId")
    public ResponseEntity<List<FindNonClientOrderIdInfoResponseDto>> nonClientFindOrderId(
        HttpServletRequest request, FindNonClientOrderIdRequestDto findNonClientOrderIdRequestDto) {

        List<FindNonClientOrderIdInfoResponseDto> nonClientOrderIdInfoResponseDto = null;

        nonClientOrderIdInfoResponseDto = orderService.nonClientFindOrderId(request,
            findNonClientOrderIdRequestDto);

        return ResponseEntity.ok(nonClientOrderIdInfoResponseDto);
    }

    @PutMapping("/non-client/order/{orderId}/password")
    public ResponseEntity<String> nonClientFindOrderPassword(HttpServletRequest request,
        @PathVariable long orderId,
        @RequestBody UpdateNonClientOrderPasswordRequestDto updateNonClientOrderPasswordRequestDto) {
        try{
            orderService.nonClientFindOrderPassword(request, orderId,
                updateNonClientOrderPasswordRequestDto);

        } catch (FeignException.NotFound e){
            log.warn("비회원 주문 비밀번호 변경 실패. 입력한 정보와 일치하는 주문을 찾을 수 없음.");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("비회원 주문 비밀번호 변경에 성공");
    }

    @GetMapping("/client/order/phoneNumberList")
    public ResponseEntity<List<ClientPhoneNumberResponseDto>> clientPhoneNumberListOnOrderPage(
        HttpServletRequest request) {
        List<ClientPhoneNumberResponseDto> clientPhoneNumberResponseDtoList = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("access", CookieUtils.getCookieValue(request, "access"));
            clientPhoneNumberResponseDtoList = orderService.getClientPhoneNumberListOnOrderPage(
                headers);
        } catch (FeignException.NotFound e) {
            log.warn("주문화면에서 사용자의 핸드폰 번호 리스트 조회에 실패하였습니다.");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clientPhoneNumberResponseDtoList);
    }

    @GetMapping("/client/order/addressList")
    public ResponseEntity<List<ClientDeliveryAddressResponseDto>> clientDeliveryAddressListOnOrderPage(
        HttpServletRequest request) {
        List<ClientDeliveryAddressResponseDto> clientDeliveryAddressResponseDtoList = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("access", CookieUtils.getCookieValue(request, "access"));
            clientDeliveryAddressResponseDtoList = orderService.getClientDeliveryAddressListOnOrderPage(
                headers);
        } catch (FeignException.NotFound e) {
            log.warn("주문화면에서 사용자의 주소 목록 조회에 실패하였습니다. 검색된 유저가 없습니다.");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clientDeliveryAddressResponseDtoList);
    }

    @PostMapping("/client/order/phone")
    public ResponseEntity<String> registerPhoneNumberOnOrderPage(HttpServletRequest request,
        @RequestBody ClientRegisterPhoneNumberRequestDto requestDto) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("access", CookieUtils.getCookieValue(request, "access"));
            log.info(orderService.registerPhoneNumberOnOrderPage(headers, requestDto));
        } catch (FeignException.NotFound e) {
            log.warn("주문화면에서 사용자의 전화번호 등록에 실패하였습니다. 검색된 유저가 없습니다.");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("주문화면에서 전화번호 등록 성공!");
    }

    @PostMapping("/client/order/address")
    public ResponseEntity<String> registerAddressOnOrderPage(HttpServletRequest request,
        @RequestBody ClientRegisterAddressRequestDto requestDto) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("access", CookieUtils.getCookieValue(request, "access"));
            orderService.registerAddressOnOrderPage(headers, requestDto);
        } catch (FeignException.BadRequest e) {
            log.warn("주문화면에서 사용자의 주소지 등록에 실패하였습니다. 10개 이상의 배송지를 등록할 수 없습니다.");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok("주문화면에서 주소지 등록 성공!");
    }

}
