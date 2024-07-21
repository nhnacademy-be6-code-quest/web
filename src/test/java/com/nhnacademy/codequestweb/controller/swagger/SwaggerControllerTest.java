package com.nhnacademy.codequestweb.controller.swagger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SwaggerControllerTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Model model;

    @InjectMocks
    private SwaggerController swaggerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSwaggerClient() {
        String mockResponse = "{ \"swagger\": \"2.0\" }";
        when(restTemplate.getForObject("http://localhost:8003/api-docs", String.class)).thenReturn(mockResponse);

        ResponseEntity<String> response = swaggerController.swaggerClient();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void testSwaggerAuthJson() {
        String mockResponse = "{ \"swagger\": \"2.0\" }";
        when(restTemplate.getForObject("http://localhost:8009/api-docs", String.class)).thenReturn(mockResponse);

        ResponseEntity<String> response = swaggerController.swaggerAuthJson();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void testSwaggerProductJson() {
        String mockResponse = "{ \"swagger\": \"2.0\" }";
        when(restTemplate.getForObject("http://localhost:8004/api-docs", String.class)).thenReturn(mockResponse);

        ResponseEntity<String> response = swaggerController.swaggerProductJson();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void testSwaggerPath() {
        String viewName = swaggerController.swaggerAuth("auth", model);

        assertEquals("swagger/swagger", viewName);
        verify(model).addAttribute("path", "/swagger/auth/json");

        viewName = swaggerController.swaggerAuth("client", model);

        assertEquals("swagger/swagger", viewName);
        verify(model).addAttribute("path", "/swagger/client/json");

        viewName = swaggerController.swaggerAuth("product", model);

        assertEquals("swagger/swagger", viewName);
        verify(model).addAttribute("path", "/swagger/product/json");
    }

    @Test
    void testSwaggerUnknownPath() {
        String viewName = swaggerController.swaggerAuth("unknown", model);

        assertEquals("swagger/swagger", viewName);
        verify(model, never()).addAttribute(eq("path"), any());
    }
}