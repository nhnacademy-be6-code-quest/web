package com.nhnacademy.codequestweb.controller.common;

import com.nhnacademy.codequestweb.service.common.CommonService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommonControllerTest {
    @Mock
    private CommonService commonService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private CommonController commonController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commonController).build();
    }

    @Test
    void testHeaderPageRoute_NoAccessCookie() throws Exception {
        mockMvc.perform(get("/page/route"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/non-client/order/find"));
    }

    @Test
    void testHeaderPageRoute_AdminAccess() throws Exception {
        Cookie accessCookie = new Cookie("access", "admin-token");

        when(request.getCookies()).thenReturn(new Cookie[]{accessCookie});
        when(commonService.isAdmin("admin-token")).thenReturn(true);

        mockMvc.perform(get("/page/route").cookie(accessCookie))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(commonService, times(1)).isAdmin("admin-token");
    }

    @Test
    void testHeaderPageRoute_ClientAccess() throws Exception {
        Cookie accessCookie = new Cookie("access", "client-token");

        when(request.getCookies()).thenReturn(new Cookie[]{accessCookie});
        when(commonService.isAdmin("client-token")).thenReturn(false);

        mockMvc.perform(get("/page/route").cookie(accessCookie))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mypage"));

        verify(commonService, times(1)).isAdmin("client-token");
    }
}
