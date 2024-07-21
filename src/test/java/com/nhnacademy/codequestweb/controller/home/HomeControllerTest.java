package com.nhnacademy.codequestweb.controller.home;

import com.nhnacademy.codequestweb.service.home.HomeService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {

    @Mock
    private HomeService homeService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private HomeController homeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIndexWithAlterMessage() {
        // 준비
        when(request.getParameter("alterMessage")).thenReturn("Test message");
        when(homeService.getPopularBooks(any())).thenReturn(Collections.emptyList());
        when(homeService.getNewBooks(any())).thenReturn(Collections.emptyList());
        when(homeService.getAlmostSoldOutBooks(any())).thenReturn(Collections.emptyList());

        // 실행
        String viewName = homeController.index(request);

        // 검증
        assertEquals("index", viewName);
        verify(request).setAttribute("alterMessage", "Test message");
        verify(request).setAttribute(eq("popularBooks"), any());
        verify(request).setAttribute(eq("newBooks"), any());
        verify(request).setAttribute(eq("almostSoldOutBooks"), any());
        verify(homeService).getPopularBooks(any());
        verify(homeService).getNewBooks(any());
        verify(homeService).getAlmostSoldOutBooks(any());
    }

    @Test
    void testIndexWithoutAlterMessage() {
        // 준비
        when(request.getParameter("alterMessage")).thenReturn(null);
        when(homeService.getPopularBooks(any())).thenReturn(Collections.emptyList());
        when(homeService.getNewBooks(any())).thenReturn(Collections.emptyList());
        when(homeService.getAlmostSoldOutBooks(any())).thenReturn(Collections.emptyList());

        // 실행
        String viewName = homeController.index(request);

        // 검증
        assertEquals("index", viewName);
        verify(request, never()).setAttribute(eq("alterMessage"), any());
        verify(request).setAttribute(eq("popularBooks"), any());
        verify(request).setAttribute(eq("newBooks"), any());
        verify(request).setAttribute(eq("almostSoldOutBooks"), any());
        verify(homeService).getPopularBooks(any());
        verify(homeService).getNewBooks(any());
        verify(homeService).getAlmostSoldOutBooks(any());
    }
}