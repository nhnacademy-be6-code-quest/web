package com.nhnacademy.codequestweb.controller.home;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {
    @GetMapping("/")
    public String index(Model model) {
        if (model.containsAttribute("message")) {
            String message = (String) model.getAttribute("message");
            log.info(message);
            model.addAttribute("message", message);
        }
        return "index";
    }
}
