package com.mrInstruments.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrInstruments.backend.dto.UserDTO;
import com.mrInstruments.backend.entities.User;
import com.mrInstruments.backend.enums.UserRol;
import com.mrInstruments.backend.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.mrInstruments.backend.utils.UtilsDtoMapper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest()
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    private User usuario1;
    private User usuario2;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        usuario1 = new User();
        usuario1.setNombre("Julian");
        usuario1.setApellido("Perez");
        usuario1.setEmail("jperez@gmail.com");
        usuario1.setPassword(bCryptPasswordEncoder.encode("12345"));
        usuario1.setUserRol(UserRol.ROLE_ADMIN);

        usuario2 = new User();
        usuario2.setNombre("Alfredo");
        usuario2.setApellido("Talones");
        usuario2.setEmail("atalones@gmail.com");
        usuario2.setPassword(bCryptPasswordEncoder.encode("12345"));
        usuario2.setUserRol(UserRol.ROLE_USER);

        usuario1.setId(1l);
        usuario2.setId(2l);

    }



    @Test
    @DisplayName("Deberia guardar usuarios - Controller")
    @Order(1)
    void guardarUser() throws Exception {
        UserDTO usuarioDto1 = userToUserDto(usuario1);

        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDto1))
                .characterEncoding("utf-8")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").isNotEmpty())
                .andExpect(jsonPath("$.nombre").isString())
                .andExpect(jsonPath("$.nombre").value("Julian"))
                .andExpect(jsonPath("$.apellido").isNotEmpty())
                .andExpect(jsonPath("$.apellido").isString())
                .andExpect(jsonPath("$.apellido").value("Perez"))
                .andReturn();



    }

    @Test
    @WithMockUser(username = "Ibis@gmail.com", password = "$2a$10$jRASmaRhW4AJGRR66cQja.BiEGB8VrEpepduYpSdQnT8WNJF81obi", roles = "ADMIN")
    @DisplayName("Deberia listar usuarios - Controller")
    void listarUser() throws Exception {
        userRepository.save(usuario1);
        userRepository.save(usuario2);

        mockMvc.perform(get("/usuarios")
                .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").isNotEmpty())
                .andExpect(jsonPath("$[0].nombre").isString())
                .andExpect(jsonPath("$[0].nombre").value("Julian"))
                .andExpect(jsonPath("$[0].apellido").isNotEmpty())
                .andExpect(jsonPath("$[0].apellido").isString())
                .andExpect(jsonPath("$[0].apellido").value("Perez"))
                .andExpect(jsonPath("$[0].email").isNotEmpty())
                .andExpect(jsonPath("$[0].email").isString())
                .andExpect(jsonPath("$[0].email").value("jperez@gmail.com"))
                .andExpect(jsonPath("$[1].nombre").isNotEmpty())
                .andExpect(jsonPath("$[1].nombre").isString())
                .andExpect(jsonPath("$[1].nombre").value("Alfredo"))
                .andExpect(jsonPath("$[1].apellido").isNotEmpty())
                .andExpect(jsonPath("$[1].apellido").isString())
                .andExpect(jsonPath("$[1].apellido").value("Talones"))
                .andExpect(jsonPath("$[1].email").isNotEmpty())
                .andExpect(jsonPath("$[1].email").isString())
                .andExpect(jsonPath("$[1].email").value("atalones@gmail.com"));


    }



    @Test
    @WithMockUser(username = "Ibis@gmail.com", password = "$2a$10$jRASmaRhW4AJGRR66cQja.BiEGB8VrEpepduYpSdQnT8WNJF81obi", roles = "ADMIN")
    @DisplayName("Deberia buscar usuarios x ID - Controller")
    void buscarUserPorId() throws Exception {
        userRepository.save(usuario1);

        mockMvc.perform(get("/usuarios/1").accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.nombre").value("Julian"))
                .andExpect(jsonPath("$.apellido").value("Perez"))
                .andReturn();

        mockMvc.perform(get("/usuarios/4").accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

    }

    @Test
    @WithMockUser(username = "Ibis@gmail.com", password = "$2a$10$jRASmaRhW4AJGRR66cQja.BiEGB8VrEpepduYpSdQnT8WNJF81obi", roles = "ADMIN")
    @DisplayName("Deberia actualizar usuarios - Controller")
    void actualizarUsuario() throws Exception {
        userRepository.save(usuario1);

        usuario1.setNombre("Carlos");

        mockMvc.perform(put("/usuarios").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToUserDto(usuario1)))
                        .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.nombre").value("Carlos"))
                .andExpect(jsonPath("$.apellido").value("Perez"))
                .andReturn();


    }

    @Test
    @WithMockUser(username = "Ibis@gmail.com", password = "$2a$10$jRASmaRhW4AJGRR66cQja.BiEGB8VrEpepduYpSdQnT8WNJF81obi", roles = "ADMIN")
    @DisplayName("Deberia buscar usuarios x Email- Controller")
    void buscarUsuariosPorEmail() throws Exception {
        userRepository.save(usuario1);

        mockMvc.perform(get("/usuarios/email/jperez@gmail.com")
                .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.nombre").value("Julian"))
                .andExpect(jsonPath("$.apellido").value("Perez"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "Ibis@gmail.com", password = "$2a$10$jRASmaRhW4AJGRR66cQja.BiEGB8VrEpepduYpSdQnT8WNJF81obi", roles = "ADMIN")
    @DisplayName("Deberia buscar usuarios x Nombre - Controller")
    void buscarUsuarioPorNombre() throws Exception {
        //userRepository.save(usuario2);
        userRepository.save(usuario1);
        userRepository.save(usuario2);

        mockMvc.perform(get("/usuarios/nombre/Alfredo")
                        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.nombre").value("Alfredo"))
                .andExpect(jsonPath("$.apellido").value("Talones"))
                .andReturn();

        mockMvc.perform(get("/usuarios/nombre/Julian")
                        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.nombre").value("Julian"))
                .andExpect(jsonPath("$.apellido").value("Perez"))
                .andReturn();

        mockMvc.perform(get("/usuarios/nombre/Hernan")
                        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

    }
    @Test
    @WithMockUser(username = "Ibis@gmail.com", password = "$2a$10$jRASmaRhW4AJGRR66cQja.BiEGB8VrEpepduYpSdQnT8WNJF81obi", roles = "ADMIN")
    @DisplayName("Deberia eliminar usuarios x ID - Controller")
    void eliminarUser() throws Exception {

        assertTrue(userRepository.findAll().isEmpty());

        userRepository.save(usuario1);
        assertFalse(userRepository.findAll().isEmpty());

        mockMvc.perform(delete("/usuarios/1")
                .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk()).andReturn();


        mockMvc.perform(delete("/usuarios/1")
                        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isNotFound()).andReturn();

        assertTrue(userRepository.findAll().size() == 0,"No deberia tener elementos");


    }
}