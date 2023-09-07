package com.mrInstruments.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrInstruments.backend.dto.CategoryDto;
import com.mrInstruments.backend.dto.CharacteristicDto;
import com.mrInstruments.backend.entities.Characteristic;
import com.mrInstruments.backend.repository.CharacteristicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.security.PermitAll;
import static com.mrInstruments.backend.utils.UtilsDtoMapper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest()
class CharacteristicControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    CharacteristicRepository characteristicRepository;
    @Autowired
    private WebApplicationContext context;

    private Characteristic char1;
    private Characteristic char2;

    @BeforeEach
    @DisplayName("Cargando Data")
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        char1 = new Characteristic("Portatil","url-img");
        char2 = new Characteristic("Liviano","url-img");
        char1.setId(1l);
        char2.setId(2l);

    }
    @Test
    @WithMockUser(username = "Ibis@gmail.com", password = "$2a$10$jRASmaRhW4AJGRR66cQja.BiEGB8VrEpepduYpSdQnT8WNJF81obi", roles = "ADMIN")
    @DisplayName("Deberia crear una Caracteristica - Controller")
    void createCharacteristic() throws Exception {
        CharacteristicDto char1Dto = characteristicToCharacteristicDto(char1);

        mockMvc.perform(post("/caracteristicas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(char1Dto))
                .characterEncoding("utf-8")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(char1Dto)))
                .andReturn();

    }

    @Test
    @DisplayName("Deberia buscar Caracteristica x ID - Controller")
    void searchCharacteristic() throws Exception {
        characteristicRepository.save(char1);
        mockMvc.perform(get("/caracteristicas/1")
                        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.titulo").value("Portatil"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "Ibis@gmail.com", password = "$2a$10$jRASmaRhW4AJGRR66cQja.BiEGB8VrEpepduYpSdQnT8WNJF81obi", roles = "ADMIN")
    @DisplayName("Deberia eliminar Caracteristica - Controller")
    void deleteCharacteristic() throws Exception {
        assertTrue(characteristicRepository.findAll().isEmpty());
        characteristicRepository.save(char1);
        assertFalse(characteristicRepository.findAll().isEmpty());
        mockMvc.perform(delete("/caracteristicas/1")
                        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(characteristicRepository.findAll().isEmpty());

        mockMvc.perform(delete("/caracteristicas/1")
                        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @PermitAll
    @DisplayName("Deberia listar Caracteristicas - Controller")
    void listCharacteristic() throws Exception {
        characteristicRepository.save(char1);
        characteristicRepository.save(char2);


        mockMvc.perform(get("/caracteristicas")
                        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].titulo").isNotEmpty())
                .andExpect(jsonPath("$[0].titulo").isString())
                .andExpect(jsonPath("$[0].titulo").value("Portatil"))
                .andExpect(jsonPath("$[1].titulo").isNotEmpty())
                .andExpect(jsonPath("$[1].titulo").isString())
                .andExpect(jsonPath("$[1].titulo").value("Liviano"));

    }
    @Test
    @WithMockUser(username = "Ibis@gmail.com", password = "$2a$10$jRASmaRhW4AJGRR66cQja.BiEGB8VrEpepduYpSdQnT8WNJF81obi", roles = "ADMIN")
    @DisplayName("Deberia Actualizar Caracteristica x ID- Controller")
    void updateCharacteristic() throws Exception {


        assertTrue(char1.getTitulo().equals("Portatil"));

        characteristicRepository.save(char1);

        char1.setTitulo("Pesados - No Portatil");

        CharacteristicDto char1Dto = characteristicToCharacteristicDto(char1);

        mockMvc.perform(put("/caracteristicas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(char1Dto))
                        .characterEncoding("utf-8")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(char1Dto)))
                .andExpect(jsonPath("$.titulo").value("Pesados - No Portatil"))
                .andReturn();


    }
}