package com.mrInstruments.backend.repository;

import com.mrInstruments.backend.entities.Category;
import com.mrInstruments.backend.entities.Product;
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
@AutoConfigureMockMvc(addFilters=false)
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;
    private Category cat1;
    private Category cat2;

    @BeforeEach
    @DisplayName("Cargando Data")
    void setup(){
        cat1 = new Category("Cordofonos","vibracion de cuerdas","url-img");
        cat2 = new Category("Percusion","decrip","url-img");

    }
    @Test
    @DisplayName("Deberia listar las categorias - Repository")
    void findAll() {
        categoryRepository.save(cat1);
        categoryRepository.save(cat2);
        List<Category> listadoCategorias = categoryRepository.findAll();
        assertFalse(listadoCategorias.isEmpty());
    }

    @Test
    @DisplayName("Deberia buscar categoria x ID - Repository")
    void findById() {
        categoryRepository.save(cat1);
        Optional<Category> categoriaEncontrada = categoryRepository.findById(cat1.getId());
        assertTrue(categoriaEncontrada.isPresent());
    }

    @Test
    @DisplayName("Deberia guardar categoria - Repository")
    void save() {
        Category categoriaGuardada = categoryRepository.save(cat1);
        assertTrue(Optional.of(categoriaGuardada).isPresent());
    }

    @Test
    @DisplayName("Deberia eliminar categoria x ID - Repository")
    void deleteById() {
        categoryRepository.save(cat1);
        categoryRepository.deleteById(cat1.getId());
        Optional<Category> categoriaEliminada = categoryRepository.findById(cat1.getId());
        assertTrue(categoriaEliminada.isEmpty());
    }
}