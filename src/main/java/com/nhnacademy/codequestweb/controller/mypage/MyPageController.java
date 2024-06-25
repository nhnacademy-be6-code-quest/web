package com.nhnacademy.codequestweb.controller.mypage;

import com.nhnacademy.codequestweb.client.auth.UserClient;
import com.nhnacademy.codequestweb.request.mypage.ClientRegisterAddressRequestDto;
import com.nhnacademy.codequestweb.response.mypage.ClientDeliveryAddressResponseDto;
import com.nhnacademy.codequestweb.response.mypage.ClientPrivacyResponseDto;
import com.nhnacademy.codequestweb.service.mypage.MyPageService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping("/mypage")
    public String mypageMain(HttpServletRequest req, HttpServletResponse res) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));

        ResponseEntity<ClientPrivacyResponseDto> response = myPageService.getPrivacy(headers);
        log.info("response: {}", response.getBody());

        req.setAttribute("view", "mypage");
        req.setAttribute("mypage", "profile");
        req.setAttribute("profile", response.getBody());
        return "index";
    }

    @GetMapping("/mypage/delivary")
    public String mypageDelivery( HttpServletRequest req) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));

        ResponseEntity<List<ClientDeliveryAddressResponseDto>> response = myPageService.getDeliveryAddresses(headers);
        log.info("response: {}", response.getBody());

        req.setAttribute("view", "mypage");
        req.setAttribute("mypage", "delivaryaddress");
        req.setAttribute("clientDeliveryAddresses", response.getBody());
        return "index";
    }

    @PostMapping("/mypage/delivary")
    public String registerDeliveryAddress(@ModelAttribute ClientRegisterAddressRequestDto clientRegisterAddressRequestDto, HttpServletRequest req) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));

        ResponseEntity<String> response = myPageService.registerAddress(headers, clientRegisterAddressRequestDto);
        log.info("/mypage/delivary post response: {}", response.getBody());

        return "redirect:/mypage/delivary";
    }

    @DeleteMapping("/mypage/delivary/{deliveryAddressId}")
    public ResponseEntity<String> deleteDeliveryAddress(@PathVariable Long deliveryAddressId, HttpServletRequest req) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", CookieUtils.getCookieValue(req, "access"));
        headers.set("refresh", CookieUtils.getCookieValue(req, "refresh"));

        ResponseEntity<String> response = myPageService.deleteDeliveryAddress(headers, deliveryAddressId);
        log.info("/mypage/delivary post response: {}", response.getBody());

        return ResponseEntity.ok("Successfully deleted delivery address");
    }
}
