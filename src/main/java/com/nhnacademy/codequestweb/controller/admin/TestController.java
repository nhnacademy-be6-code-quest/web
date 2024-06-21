package com.nhnacademy.codequestweb.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public String test(){
        return "Test2(V0테스트용.안씀)";
    }
}
