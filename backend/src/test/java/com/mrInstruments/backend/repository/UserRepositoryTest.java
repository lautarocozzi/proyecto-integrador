package com.mrInstruments.backend.repository;

import com.mrInstruments.backend.entities.User;
import com.mrInstruments.backend.enums.UserRol;
import io.jsonwebtoken.lang.Assert;
import io.swagger.v3.oas.models.media.UUIDSchema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.AssertTrue;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureMockMvc(addFilters = false)
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    private User usuario1;
    private User admin1;

    @BeforeEach
    @DisplayName("Cargando Data..")
    void setup(){
        usuario1 = new User("Juan","Perez","jperez@gmail.com","hola123", UserRol.ROLE_USER);
        admin1 = new User("Camila","Ordoniez","camilao@gmail.com","ASD123", UserRol.ROLE_ADMIN);
    }

    @Test
    @DisplayName("Deberia listar los usuarios - Repository")
    void findAll() {
        userRepository.save(usuario1);
        userRepository.save(admin1);

        List<User> listaUsuarios = userRepository.findAll();

        assertFalse(listaUsuarios.isEmpty());
        assertTrue(listaUsuarios.size() == 2);


    }

    @Test
    @DisplayName("Deberia buscar usuarios x ID - Repository")
    void findById() {
        userRepository.save(usuario1);
        Optional<User> userEncontrado = userRepository.findById(usuario1.getId());
        assertTrue(userEncontrado.isPresent());
        System.out.println(userEncontrado);
    }

    @Test
    @DisplayName("Deberia guardar usuario - Repository")
    void save() {
        User usuarioGuardado = userRepository.save(usuario1);
        assertTrue(Optional.of(usuarioGuardado).isPresent());
    }

    @Test
    @DisplayName("Deberia eliminar usuario x ID - Repository")
    void deleteById() {
        userRepository.save(usuario1);
        assertTrue(userRepository.findById(usuario1.getId()).isPresent());
        userRepository.deleteById(usuario1.getId());
        Optional<User> userEliminado = userRepository.findById(usuario1.getId());
        assertTrue(userEliminado.isEmpty());
    }

    @Test
    @DisplayName("Deberia buscar usuarios x Email - Repository")
    void findByEmail() {
        //given
        userRepository.save(usuario1);
        //when
        Optional<UserDetails> userEncontrado = userRepository.findByEmail(usuario1.getEmail());
        //then
        assertTrue(userEncontrado.isPresent());

    }

    @Test
    @DisplayName("Deberia buscar usuarios x Nombre - Repository")
    void findByNombre() {
        //given
        userRepository.save(usuario1);
        //when
        List<User> usersEncontrados = userRepository.findByNombre(usuario1.getNombre());
        //then
        assertTrue(!usersEncontrados.isEmpty());

    }

    @Test
    @DisplayName("Deberia buscar usuarios x Email & Password - Repository")
    void findByEmailAndPassword() {
        //given
        userRepository.save(usuario1);
        //when
        Optional<User> userEncontrado = userRepository.findByEmailAndPassword(usuario1.getEmail(),usuario1.getPassword());
        //then
        assertTrue(userEncontrado.isPresent());
    }
}