package com.nhnacademy.codequestweb.controller.client;

import com.nhnacademy.codequestweb.request.client.ClientChangePasswordRequestDto;
import com.nhnacademy.codequestweb.request.client.ClientRecoveryRequestDto;
import com.nhnacademy.codequestweb.service.client.ClientService;
import com.nhnacademy.codequestweb.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@Controller
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/change-password")
    public String changePassword(
            @RequestParam("email") String email,
            @RequestParam("token") String token,
            HttpServletRequest request
    ) {
        if (CookieUtils.getCookieValue(request, "access") != null ||
                !tokenParser(token).equals(email)) {
            return HttpStatus.FORBIDDEN.toString();
        }

        request.setAttribute("email", email);
        request.setAttribute("token", token);
        request.setAttribute("view", "change-password");
        return "index";
    }

    @PostMapping("/do/change-password")
    public String doChangePassword(@Valid @ModelAttribute ClientChangePasswordRequestDto clientChangePasswordRequestDto) {
        clientService.changePassword(clientChangePasswordRequestDto);
        return "redirect:/";
    }

    @GetMapping("/send-reset-password-email")
    public String sendResetPasswordEmail(@RequestParam("email") String email) {
        clientService.sendChangePassword(email);
        return "redirect:/auth";
    }

    @GetMapping("/send-recovery-account")
    public String sedRecoveryAccount(@RequestParam("email") String email) {
        clientService.sendRecoveryAccount(email);
        return "redirect:/auth";
    }

    @GetMapping("/send-recovery-account/dooray")
    public String sedRecoveryAccountDooray(@RequestParam("email") String email) {
        clientService.sendRecoverAccountDooray(email);
        return "redirect:/auth";
    }

    @GetMapping("/recover-account")
    public ResponseEntity<String> recoverAccount(
            @RequestParam("email") String email,
            @RequestParam("token") String token,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        if (CookieUtils.getCookieValue(request, "access") != null ||
                !tokenParser(token).equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(clientService.recoverAccount(new ClientRecoveryRequestDto(email, token)));
    }

    private String tokenParser(String token) {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(token.split("\\.")[1]));
        return extractEmailFromPayload(payload);
    }

    private String extractEmailFromPayload(String payload) {
        String emailClaim = "\"email\":\"";
        int beginIndex = payload.indexOf(emailClaim) + emailClaim.length();
        int endIndex = payload.indexOf("\"", beginIndex);
        return payload.substring(beginIndex, endIndex);
    }
}
