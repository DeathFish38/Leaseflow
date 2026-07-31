package com.leaseflow.backend.auth.controller;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.leaseflow.backend.auth.dto.LoginRequest;
import com.leaseflow.backend.auth.dto.LoginResponse;
import com.leaseflow.backend.auth.dto.RegisterRequest;
import com.leaseflow.backend.auth.dto.UserResponse;
import com.leaseflow.backend.auth.service.AuthService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @Test
    void register_shouldReturnCreated() throws Exception {

        RegisterRequest request = new RegisterRequest(
                "test@example.com",
                "John",
                "Doe",
                "password123");

        UserResponse response = new UserResponse(
                1L,
                "test@example.com",
                "John",
                "Doe");

        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(authService).register(any(RegisterRequest.class));
    }

    @Test
    void register_shouldReturnBadRequestWhenValidationFails() throws Exception {

        RegisterRequest request = new RegisterRequest(
                "",
                "",
                "",
                "");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_shouldReturnOk() throws Exception {

        LoginRequest request = new LoginRequest(
                "test@example.com",
                "password123");

        LoginResponse response = new LoginResponse(
                1L,
                "test@example.com",
                "Login successful");

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.message").value("Login successful"));

        verify(authService).login(any(LoginRequest.class));
    }

    @Test
    void login_shouldReturnBadRequestWhenValidationFails() throws Exception {

        LoginRequest request = new LoginRequest(
                "",
                "");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
