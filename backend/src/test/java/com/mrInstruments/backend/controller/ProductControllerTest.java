package com.mrInstruments.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrInstruments.backend.dto.ProductDto;
import com.mrInstruments.backend.entities.Category;
import com.mrInstruments.backend.entities.Product;
import com.mrInstruments.backend.repository.CategoryRepository;
import com.mrInstruments.backend.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.security.PermitAll;

import static com.mrInstruments.backend.utils.UtilsDtoMapper.categoryToCategoryDto;
import static com.mrInstruments.backend.utils.UtilsDtoMapper.productToProductDto;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest()
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "Ibis@gmail.com", password = "$2a$10$jRASmaRhW4AJGRR66cQja.BiEGB8VrEpepduYpSdQnT8WNJF81obi", roles = "ADMIN")
    @DisplayName("Deberia crear un producto")
    void guardarProducto() throws Exception{

        Category category = new Category("guitarras", "instrumento de 5 cuerdas", "");
        category = categoryRepository.save(category);
        Product product1 = new Product("Epiphone", "guitarra electrica","imagen.jpg", 70.0, category);
        product1.setId(4l);
        ProductDto productDto = new ProductDto();
        productDto.setCategoria(categoryToCategoryDto(category));
        productDto = productToProductDto(product1);

        //Se simula una peticion rest (POST a "/productos" con contenido product1 en JSON
        // con metodo objectMapper.writeValueAsString transformando el objeto a JSON)
        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto))
                        .characterEncoding("utf-8")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(productDto)))
                .andReturn();
    }

    @Test
    @PermitAll
    @DisplayName("Deberia traer una lista de productos")
    void listarProductos() throws Exception {

        Category categoria = new Category("guitarras", "instrumento de 5 cuerdas", "");
        categoryRepository.save(categoria);
        Product p1 = new Product("Epiphone", "guitarra electrica","imagen.jpg", 200.0, categoria);
        Product p2 = new Product("Gracia", "guitarra criolla", "imagen.jpg",70.0, categoria);

        productRepository.save(p1);
        productRepository.save(p2);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);

        //Se simula una peticion REST (GET a "/productos") debe devolver la lista de productos mockeada arriba
        //Se verifican algunos de los campos de la lista devuelta por la peticion REST
        mockMvc.perform(get("/productos")
                        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                //.andExpect(content().string(objectMapper.writeValueAsString(listaProductos)));
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").isNotEmpty())
                .andExpect(jsonPath("$[0].nombre").isString())
                .andExpect(jsonPath("$[0].nombre").value("Epiphone"))
                .andExpect(jsonPath("$[0].descripcion").isNotEmpty())
                .andExpect(jsonPath("$[0].descripcion").isString())
                .andExpect(jsonPath("$[0].descripcion").value("guitarra electrica"))
                .andExpect(jsonPath("$[0].precio").isNotEmpty())
                .andExpect(jsonPath("$[0].precio").isNumber())
                .andExpect(jsonPath("$[0].precio").value(200.0))
                .andExpect(jsonPath("$[1].nombre").isNotEmpty())
                .andExpect(jsonPath("$[1].nombre").isString())
                .andExpect(jsonPath("$[1].nombre").value("Gracia"))
                .andExpect(jsonPath("$[1].descripcion").isNotEmpty())
                .andExpect(jsonPath("$[1].descripcion").isString())
                .andExpect(jsonPath("$[1].descripcion").value("guitarra criolla"))
                .andExpect(jsonPath("$[1].precio").isNotEmpty())
                .andExpect(jsonPath("$[1].precio").isNumber())
                .andExpect(jsonPath("$[1].precio").value(70.0));
    }

    @Test
    @DisplayName("Deberia traer un producto")
    void obtenerProductoPorId() throws Exception {

        mockMvc.perform(get("/productos/4")
                        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        mockMvc.perform(get("/productos/2")
                        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").isMap())
                .andReturn();

    }

    @Test
    @WithMockUser(username = "Ibis@gmail.com", password = "$2a$10$jRASmaRhW4AJGRR66cQja.BiEGB8VrEpepduYpSdQnT8WNJF81obi", roles = "ADMIN")
    @DisplayName("Deberia eliminar un producto")
    void eliminarProducto() throws Exception {

        //1
        assertTrue(productRepository.findAll().isEmpty(), "No deberia haber elementos en la BBDD");

        Category categoria = new Category("guitarras", "instrumento de 5 cuerdas", "");
        categoryRepository.save(categoria);
        Product p1 = new Product("Epiphone", "guitarra electrica","imagen.jpg", 200.0, categoria);
        p1.setId(1l);
        productRepository.save(p1);

        //2
        assertFalse(productRepository.findAll().isEmpty(),"Deberia tener elementos");
        //3
        assertTrue(productRepository.findAll().size() == 1,"Deberia tener 1 elemento, el que guardamos");
        //4
        mockMvc.perform(delete("/productos/1")
                        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())//deberia dar response code 200 al eliminar producto con id 1
                .andReturn();
        //5
        assertTrue(productRepository.findAll().isEmpty(), "Luego de la llamada a la api no deberiamos tener elementos en la BBDD");
        //6
        mockMvc.perform(delete("/productos/1")
                        .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isBadRequest())//deberia dar response code 400 al eliminar producto con id 1
                .andReturn();
    }
}