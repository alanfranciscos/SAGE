package com.sage.controller.v1.caregiver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sage.dto.v1.caregiver.request.CreateCaregiverRequestDto;
import com.sage.dto.v1.caregiver.request.UpdateCaregiverActiveStatusRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "user", roles = {"USER", "ADMIN"})
class CaregiverIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveListarTodosOsCuidadores() throws Exception {
        mockMvc.perform(get("/v1/caregiver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").exists());
    }

    @Test
    void deveBuscarCuidadorPorId() throws Exception {
        String id = "11111111-2222-4333-a444-555666777888"; // Maria Santos do banco de teste
        mockMvc.perform(get("/v1/caregiver/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Maria Santos"));
    }

    @Test
    void deveCriarNovoCuidador() throws Exception {
        CreateCaregiverRequestDto dto = new CreateCaregiverRequestDto(
                "Novo Cuidador",
                "12345678901",
                "novo.cuidador@email.com",
                "11912345678",
                "employee"
        );

        mockMvc.perform(post("/v1/caregiver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().string(org.hamcrest.Matchers.notNullValue()));
    }

    @Test
    void deveAtualizarDadosDoCuidador() throws Exception {
        String id = "22222222-3333-4444-b555-666777888999"; // Carlos Oliveira
        CreateCaregiverRequestDto dto = new CreateCaregiverRequestDto(
                "Carlos Oliveira Atualizado",
                "76734514099",
                "carlos.oliveira@email.com",
                "21988887777",
                "employee"
        );

        mockMvc.perform(put("/v1/caregiver/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveAtualizarStatusAtivoDoCuidador() throws Exception {
        String id = "33333333-4444-5555-c666-777888999000"; // Ana Lima
        UpdateCaregiverActiveStatusRequestDto dto = new UpdateCaregiverActiveStatusRequestDto(false);

        mockMvc.perform(patch("/v1/caregiver/{id}/active", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveContarCuidadoresLideres() throws Exception {
        mockMvc.perform(get("/v1/caregiver/count-caregiver-leader"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").isNumber());
    }
}