package com.nhnacademy.codequestweb.controller.swagger;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

@Controller
@RequiredArgsConstructor
public class SwaggerController {
    private final RestTemplate restTemplate;

    @GetMapping("/swagger/client/json")
    public ResponseEntity<String> swaggerClient() {
        String url = "http://localhost:8003/api-docs";
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/swagger/auth/json")
    public ResponseEntity<String> swaggerAuthJson() {
        String url = "http://localhost:8009/api-docs";
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/swagger/{path}")
    public String swaggerAuth(@PathVariable String path, Model model) {
        if (path.equals("auth")) {
            model.addAttribute("path", "/swagger/auth/json");
        } else if (path.equals("client")) {
            model.addAttribute("path", "/swagger/client/json");
        }
        return "swagger/swagger";
    }
}
