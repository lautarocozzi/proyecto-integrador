package com.mrInstruments.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrInstruments.backend.dto.CategoryDto;
import com.mrInstruments.backend.entities.Category;
import com.mrInstruments.backend.repository.CategoryRepository;
import com.mrInstruments.backend.service.CategoryService;

import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static com.mrInstruments.backend.utils.UtilsDtoMapper.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest()
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CategoryRepository  categoryRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    private Category cat1;
    private Category cat2;


    @BeforeEach
    @DisplayName("Cargando Data")
    void setup(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        cat1 = new Category("Cordofonos","vibracion de cuerdas","url-img");
        cat1.setId(1l);
        cat2 = new Category("guitarras", "instrumento de 5 cuerdas", "");
        cat2.setId(2l);

    }
    @Test
    @WithMockUser(username = "Ibis@gmail.com", password = "$2a$10$jRASmaRhW4AJGRR66cQja.BiEGB8VrEpepduYpSdQnT8WNJF81obi", roles = "ADMIN")
    @DisplayName("Deberia crear una Categoria - Controller")
    void createCategory() throws Exception {
        CategoryDto categoryDto = categoryToCategoryDto(cat1);

        mockMvc.perform(post("/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDto))
                .characterEncoding("utf-8")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(categoryDto)))
                .andReturn();



    }

    @Test
    @DisplayName("Deberia buscar Categoria x ID - Controller")
    void searchCategory() throws Exception {
        categoryRepository.save(cat1);
        mockMvc.perform(get("/categorias/1")
                .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isMap())
                .andReturn();

    }

    @Test
    @WithMockUser(username = "Ibis@gmail.com", password = "$2a$10$jRASmaRhW4AJGRR66cQja.BiEGB8VrEpepduYpSdQnT8WNJF81obi", roles = "ADMIN")
    @DisplayName("Deberia eliminar Categoria - Controller")
    void deleteCategory() throws Exception {
        assertTrue(categoryRepository.findAll().isEmpty());
        categoryRepository.save(cat1);
        assertFalse(categoryRepository.findAll().isEmpty());
        mockMvc.perform(delete("/categorias/1")
                .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(categoryRepository.findAll().isEmpty());

        mockMvc.perform(delete("/categorias/1")
                        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @PermitAll
    @DisplayName("Deberia listar categorias - Controller")
    void listCategory() throws Exception {
        categoryRepository.save(cat1);
        categoryRepository.save(cat2);


        mockMvc.perform(get("/categorias/")
                .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].titulo").isNotEmpty())
                .andExpect(jsonPath("$[0].titulo").isString())
                .andExpect(jsonPath("$[0].titulo").value("Cordofonos"))
                .andExpect(jsonPath("$[0].descripcion").isNotEmpty())
                .andExpect(jsonPath("$[0].descripcion").isString())
                .andExpect(jsonPath("$[0].descripcion").value("vibracion de cuerdas"))
                .andExpect(jsonPath("$[1].titulo").isNotEmpty())
                .andExpect(jsonPath("$[1].titulo").isString())
                .andExpect(jsonPath("$[1].titulo").value("guitarras"))
                .andExpect(jsonPath("$[1].descripcion").isNotEmpty())
                .andExpect(jsonPath("$[1].descripcion").isString())
                .andExpect(jsonPath("$[1].descripcion").value("instrumento de 5 cuerdas"));

    }

    @Test
    @WithMockUser(username = "Ibis@gmail.com", password = "$2a$10$jRASmaRhW4AJGRR66cQja.BiEGB8VrEpepduYpSdQnT8WNJF81obi", roles = "ADMIN")
    @DisplayName("Deberia Actualizar Categoria x ID- Controller")
    void updateCategory() throws Exception {

        assertTrue(cat1.getTitulo().equals("Cordofonos"));

        categoryRepository.save(cat1);

        cat1.setTitulo("guitarras");

        CategoryDto categoryDto = categoryToCategoryDto(cat1);

        mockMvc.perform(put("/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto))
                        .characterEncoding("utf-8")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(jsonPath("$.titulo").value("guitarras"))
                .andReturn();



    }
}