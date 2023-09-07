package com.mrInstruments.backend.repository;

import com.mrInstruments.backend.entities.Category;
import com.mrInstruments.backend.entities.Characteristic;
import com.mrInstruments.backend.entities.Product;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import javax.validation.constraints.AssertFalse;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureMockMvc(addFilters=false)
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;
    private Product p1;
    private Product p2;
    private Category cat1;
    @BeforeEach
    @DisplayName("Cargando Data")
    void setup(){

        cat1 = new Category("Cordofonos","vibracion de cuerdas","url-img");
        categoryRepository.save(cat1);
        p1 = new Product("Guitarra Electrica","descrip","url-img",350.0,cat1);
        p2 = new Product("Guitarra Criolla","descrip","url-img",200.0,cat1);

    }
    @Test
    @DisplayName("Deberia listar los productos - Repository")
    void findAll() {
        productRepository.save(p1);
        productRepository.save(p2);

        List<Product> listadoProductos = productRepository.findAll();

        assertFalse(listadoProductos.isEmpty());
        assertTrue(listadoProductos.size() == 2);


    }

    @Test
    @DisplayName("Deberia buscar producto x ID - Repository")
    void findById() {
        productRepository.save(p1);
        Optional<Product> productoEncontrado = productRepository.findById(p1.getId());

        assertTrue(productoEncontrado.isPresent());
        assertTrue(productoEncontrado.get().getId().describeConstable().isPresent());
        System.out.println(productoEncontrado.get().getId().describeConstable());
    }

    @Test
    @DisplayName("Deberia guardar producto - Repository")
    void save() {
        Product productoGuardado = productRepository.save(p1);

        assertTrue(Optional.of(productoGuardado).isPresent());
    }

    @Test
    @DisplayName("Deberia eliminar producto x ID - Repository")
    void deleteById() {
        productRepository.save(p1);
        productRepository.deleteById(p1.getId());
        Optional<Product> productoEliminado = productRepository.findById(p1.getId());
        assertTrue(productoEliminado.isEmpty());

    }
}