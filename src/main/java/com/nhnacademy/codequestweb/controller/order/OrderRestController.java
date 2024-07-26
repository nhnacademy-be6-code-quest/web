package com.nhnacademy.codequestweb.controller.order;

import com.nhnacademy.codequestweb.response.mypage.ClientDeliveryAddressResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientPhoneNumberResponseDto;
import com.nhnacademy.codequestweb.response.order.nonclient.NonClientOrderGetResponseDto;
import com.nhnacademy.codequestweb.service.order.OrderService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderRestController {

    private final OrderService orderService;

    @GetMapping("/non-client/order")
    public NonClientOrderGetResponseDto nonClientFindOrder(HttpServletRequest req, @RequestParam("orderId") String orderIdStr, @RequestParam("orderPassword") String orderPassword){

        Long orderId = null;
        NonClientOrderGetResponseDto nonClientOrderGetResponseDto = null;
        try {
            Long.parseLong(orderIdStr);
            HttpHeaders headers = new HttpHeaders();
            headers.set("access", CookieUtils.getCookieValue(req, "access"));
            orderId = Long.parseLong(orderIdStr);
            nonClientOrderGetResponseDto = orderService.findNonClientOrder(headers, orderId, orderPassword);
        } catch (NumberFormatException e) {
            log.info("입력한 주문 아이디를 정수형으로 파싱하는데 실패했습니다.");
        } catch(RuntimeException e){
            log.info("주문 조회 실패");
        }

        return nonClientOrderGetResponseDto;

    }

    @GetMapping("/client/order/phoneNumberList")
    public List<ClientPhoneNumberResponseDto> clientPhoneNumberList(HttpServletRequest request){
        List<ClientPhoneNumberResponseDto> clientPhoneNumberResponseDtoList = null;
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.set("access", CookieUtils.getCookieValue(request, "access"));
            orderService.getClientPhoneNumberList(headers);
        } catch (RuntimeException e){
            log.info("주문화면에서 사용자의 핸드폰 번호 리스트 조회에 실패하였습니다.");
        }
        return clientPhoneNumberResponseDtoList;
    }

    @GetMapping("/client/order/addressList")
    public List<ClientDeliveryAddressResponseDto> clientDeliveryAddressList(HttpServletRequest request){
        List<ClientDeliveryAddressResponseDto> clientDeliveryAddressResponseDtoList = null;
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.set("access", CookieUtils.getCookieValue(request, "access"));
            orderService.getClientDeliveryAddressList(headers);
        } catch (RuntimeException e){
            log.info("주문화면에서 사용자의 주소 목록 조회에 실패하였습니다.");
        }
        return clientDeliveryAddressResponseDtoList;
    }

}
