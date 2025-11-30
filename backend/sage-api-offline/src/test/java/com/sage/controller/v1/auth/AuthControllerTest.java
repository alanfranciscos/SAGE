package com.sage.controller.v1.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sage.config.security.TokenService;
import com.sage.dto.v1.auth.*;
import com.sage.dto.v1.caregiver.response.CaregiverResponseDto;
import com.sage.dto.v1.caregiver.response.CaregiverResponseFromPasswordTableDto;
import com.sage.services.caregiver.CaregiverServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CaregiverServiceImpl caregiverService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private CaregiverResponseDto sampleUser;
    private CaregiverResponseFromPasswordTableDto passwordTableDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        UUID userId = UUID.randomUUID();
        sampleUser = new CaregiverResponseDto(
                userId,
                "Maria Santos",
                "12345678901",
                "maria.santos@email.com",
                "11999998888",
                "TOKEN123",
                true,
                OffsetDateTime.now(),
                "chieff"
        );

        passwordTableDto = new CaregiverResponseFromPasswordTableDto(
                UUID.randomUUID(),
                userId,
                "$2a$10$senhaCriptografadaFake",
                OffsetDateTime.now(),
                true,
                false,
                "VERIFCODE",
                OffsetDateTime.now().plusHours(1)
        );
    }

    @Test
    void deveLogarComCredenciaisValidas() throws Exception {
        LoginRequestDTO login = new LoginRequestDTO("maria.santos@email.com", "123456");

        when(caregiverService.findByEmailAndReturnsCaregiverResponseDto(anyString()))
                .thenReturn(Optional.of(sampleUser));

        when(caregiverService.getCaregiverFromPasswordTable(any(UUID.class)))
                .thenReturn(Optional.of(passwordTableDto));

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(tokenService.generateToken(any())).thenReturn("FAKE_JWT_TOKEN");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("FAKE_JWT_TOKEN"))
                .andExpect(jsonPath("$.name").value("Maria Santos"));
    }

    @Test
    void deveRegistrarNovoUsuario() throws Exception {
        RegisterRequestDTO register = new RegisterRequestDTO(
                "Carlos Eduardo",
                "11122233344",
                "carlos@email.com",
                "11988887777",
                "123456",
                null
        );

        when(caregiverService.findByEmailAndReturnsCaregiverResponseDto(anyString()))
                .thenReturn(Optional.empty());
        when(caregiverService.createCaregiver(any(), any(), any(), any(), any()))
                .thenReturn(UUID.randomUUID());
        when(passwordEncoder.encode(anyString())).thenReturn("HASHED_123456");
        when(caregiverService.getCaregiverById(any())).thenReturn(sampleUser);
        when(tokenService.generateToken(any())).thenReturn("FAKE_TOKEN");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("FAKE_TOKEN"))
                .andExpect(jsonPath("$.name").value("Maria Santos"));
    }

    @Test
    void deveEnviarEmailDeRecuperacao() throws Exception {
        ForgotPasswordRequestDTO request = new ForgotPasswordRequestDTO("maria.santos@email.com");
        doNothing().when(caregiverService).sendRecoveryToken(anyString());

        mockMvc.perform(post("/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void deveResetarSenha() throws Exception {
        ResetPasswordRequestDTO request = new ResetPasswordRequestDTO(
                "maria.santos@email.com",
                "TOKEN123",
                "novaSenha",
                "novaSenha"
        );
        doNothing().when(caregiverService).resetPassword(any(), any());

        mockMvc.perform(post("/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}