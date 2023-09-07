package com.mrInstruments.backend.service;

import com.mrInstruments.backend.dto.UserDTO;
import com.mrInstruments.backend.entities.User;
import com.mrInstruments.backend.enums.UserRol;
import com.mrInstruments.backend.exception.ResourceNotFoundException;
import com.mrInstruments.backend.repository.UserRepository;
import com.mrInstruments.backend.service.email.EmailTemplateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static com.mrInstruments.backend.utils.UtilsDtoMapper.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserServiceTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @MockBean
    UserRepository userRepository;
    @Autowired
    UserService userService;
    private User usuario1;
    private User usuario2;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setup(){
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
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(springSecurity())
//                .build();
//
//       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println(authentication);
    }
    @Test
    void actualizarUsuario() throws Exception {
        Mockito.when(userRepository.findById(usuario1.getId())).thenReturn(Optional.of(usuario1));
        //Mockito.when(userRepository.save(usuario1)).thenReturn(usuario1);
        lenient().when(userRepository.save(any(User.class))).thenReturn(usuario1);


        System.out.println(userRepository.save(usuario1));


        UserDTO usuarioActualizado = userService.actualizarUsuario(userToUserDto(usuario1));
        assertTrue(usuarioActualizado.getNombre().equals(usuario1.getNombre()));
        assertTrue(usuarioActualizado.getEmail().equals(usuario1.getEmail()));


    }
    @Test
    void obtenerTodosLosUsuarios() {
        List<User> listaUsers = new ArrayList<>();
        listaUsers.add(usuario1);
        listaUsers.add(usuario2);
        Mockito.when(userRepository.findAll()).thenReturn(listaUsers);
        List <UserDTO> listarUsuarios = userService.obtenerTodosLosUsuarios();

        assertTrue(!listarUsuarios.isEmpty());
        assertTrue(listarUsuarios.size()==2);
        assertTrue(listarUsuarios.get(0).getNombre().equals(usuario1.getNombre()));


    }
    @Test
    void eliminarUsuario() throws Exception {
        Mockito.when(userRepository.findById(usuario1.getId())).thenReturn(Optional.of(usuario1));
        Mockito.doNothing().when(userRepository).deleteById(usuario1.getId());
        userService.eliminarUsuario(usuario1.getId());
        verify((userRepository)).deleteById(1l);

    }

    @Test
    void loadUserByUsername() {
        Mockito.when(userRepository.findByEmail(usuario1.getEmail())).thenReturn(Optional.of(usuario1));
        UserDetails detallesUsuario = userService.loadUserByUsername(usuario1.getEmail());

        assertTrue(detallesUsuario.getUsername().equals(usuario1.getUsername()));

    }

    @Test
    void guardarUsuario() throws Exception {
        Mockito.when(userRepository.findByEmail(usuario1.getEmail())).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(any(User.class))).thenReturn(usuario1);

        UserDTO usuarioGuardado = userService.guardarUsuario(userToUserDto(usuario1));

        assertTrue(usuarioGuardado.getEmail().equals(usuario1.getEmail()));
        assertTrue(usuarioGuardado.getNombre().equals(usuario1.getNombre()));
    }

    @Test
    void buscarUserPorId() throws Exception {
        Mockito.when(userRepository.findById(usuario1.getId())).thenReturn(Optional.of(usuario1));
        Optional<UserDTO> usuarioBuscado = userService.buscarUserPorId(usuario1.getId());
        assertTrue(usuarioBuscado.get().getNombre().equals(usuario1.getNombre()));
    }

    @Test
    void buscarUsuarioPorNombre() throws ResourceNotFoundException {
        Mockito.when(userRepository.findByNombre(usuario1.getNombre())).thenReturn(Optional.of(usuario1));
        UserDTO usuarioBuscado = userService.buscarUsuarioPorNombre(usuario1.getNombre());
        assertTrue(usuarioBuscado.getNombre().equals(usuario1.getNombre()));
    }

    @Test
    void buscarUsuarioPorEmailAndPassword() {
        Mockito.when(userRepository.findByEmailAndPassword(usuario1.getEmail(),usuario1.getPassword())).thenReturn(Optional.of(usuario1));
        User usuarioBuscadoEmailPassword = userService.buscarUsuarioPorEmailAndPassword(usuario1.getEmail(),usuario1.getPassword());
        assertTrue(usuarioBuscadoEmailPassword.getId().equals(usuario1.getId()));
        assertTrue(usuarioBuscadoEmailPassword.getEmail().equals(usuario1.getEmail()));
    }

    @Test
    void findUserDtoToUser() {
        Mockito.when(userRepository.findByEmailAndPassword(usuario1.getEmail(),usuario1.getPassword())).thenReturn(Optional.of(usuario1));
    }
}