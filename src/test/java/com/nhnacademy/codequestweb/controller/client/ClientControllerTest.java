package com.nhnacademy.codequestweb.controller.client;

import com.nhnacademy.codequestweb.request.client.ClientChangePasswordRequestDto;
import com.nhnacademy.codequestweb.service.client.ClientService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ClientControllerTest {
    @Mock
    private ClientService clientService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private ClientController clientController;

    private MockMvc mockMvc;

    private String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InRlc3RAZXhhbXBsZS5jb20ifQ.T4ia8uMh-Xd9ldq1VNBc20ZOvwOWnWM7yGwwITXa5gc";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
    }

    @Test
    void testChangePassword() throws Exception {
        when(request.getCookies()).thenReturn(new Cookie[]{});
        String email = "test@example.com";

        mockMvc.perform(get("/change-password")
                        .param("email", email)
                        .param("token", token))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(request().attribute("email", email))
                .andExpect(request().attribute("token", token))
                .andExpect(request().attribute("view", "change-password"));
    }

    @Test
    void testDoChangePassword() throws Exception {
        ClientChangePasswordRequestDto dto = new ClientChangePasswordRequestDto("newPassword", "newPassword", "qwerzX12345@");

        doNothing().when(clientService).changePassword(dto);

        mockMvc.perform(post("/do/change-password")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", dto.getNewPassword())
                        .param("token", dto.getNewPassword())
                        .param("newPassword", dto.getNewPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(clientService, times(1)).changePassword(any(ClientChangePasswordRequestDto.class));
    }

    @Test
    void testSendResetPasswordEmail() throws Exception {
        String email = "test@example.com";

        when(clientService.sendChangePassword(email)).thenReturn("test");

        mockMvc.perform(get("/send-reset-password-email")
                        .param("email", email))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth"));

        verify(clientService, times(1)).sendChangePassword(email);
    }

    @Test
    void testSendRecoveryAccount() throws Exception {
        String email = "test@example.com";

        when(clientService.sendRecoveryAccount(email)).thenReturn("test");

        mockMvc.perform(get("/send-recovery-account")
                        .param("email", email))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth"));

        verify(clientService, times(1)).sendRecoveryAccount(email);
    }

    @Test
    void testRecoverAccount() throws Exception {
        when(request.getCookies()).thenReturn(new Cookie[]{});
        String email = "test@example.com";

        mockMvc.perform(get("/recover-account")
                        .param("email", email)
                        .param("token", token))
                .andExpect(status().isOk());

        verify(clientService, times(1)).recoverAccount(any());
    }
}
