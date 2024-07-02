//package com.nhnacademy.codequestweb.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.cloud.openfeign.support.PageJacksonModule;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class JacksonConfig implements WebMvcConfigurer {
//
//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new PageJacksonModule());
//        return objectMapper;
//    }
//
//    @Bean
//    public PageableHandlerMethodArgumentResolverCustomizer customize() {
//        return p -> p.setOneIndexedParameters(true);
//    }
//}
