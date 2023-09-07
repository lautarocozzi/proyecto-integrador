package com.mrInstruments.backend.repository;

import com.mrInstruments.backend.entities.Characteristic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureMockMvc(addFilters = false)
class CharacteristicRepositoryTest {

    @Autowired
    CharacteristicRepository characteristicRepository;

    private Characteristic char1;
    private Characteristic char2;

    @BeforeEach
    @DisplayName("Cargando Data..")
    void setup(){
        char1 = new Characteristic("Portatil","url-img");
        char2 = new Characteristic("Liviano","url-img");

    }

    @Test
    @DisplayName("Deberia listar las caracteristicas - Repository")
    void findAll() {
        characteristicRepository.save(char1);
        characteristicRepository.save(char2);
        List<Characteristic> listaCaracteristicas = characteristicRepository.findAll();
        assertFalse(listaCaracteristicas.isEmpty());
        assertTrue(listaCaracteristicas.size() == 2);
    }
    @Test
    @DisplayName("Deberia buscar caracteristicas x ID - Repository")
    void findById() {
        characteristicRepository.save(char1);
        Optional<Characteristic> charEncontrada = characteristicRepository.findById(char1.getId());
        assertTrue(charEncontrada.isPresent());
    }

    @Test
    @DisplayName("Deberia guardar caracteristica - Repository")
    void save() {
        Characteristic charGuardada = characteristicRepository.save(char1);
        assertTrue(Optional.of(charGuardada).isPresent());

    }

    @Test
    @DisplayName("Deberia eliminar caracteristica x ID - Repository")
    void deleteById() {
        characteristicRepository.save(char1);
        assertTrue(characteristicRepository.findById(char1.getId()).isPresent());
        characteristicRepository.deleteById(char1.getId());
        Optional<Characteristic> caracteristicaEliminada = characteristicRepository.findById(char1.getId());
        assertTrue((caracteristicaEliminada.isEmpty()));
    }
}