package com.mrInstruments.backend.service;

import com.mrInstruments.backend.dto.ProductDto;
import com.mrInstruments.backend.entities.Category;
import com.mrInstruments.backend.entities.Product;
import com.mrInstruments.backend.exception.BadRequestException;
import com.mrInstruments.backend.repository.CategoryRepository;
import com.mrInstruments.backend.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;

import static com.mrInstruments.backend.utils.UtilsDtoMapper.productToProductDto;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @MockBean
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductService productService;

    private Category category1;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setup(){
        category1 = categoryRepository.save(new Category("Cuerda", "categoria cuerda descripcion", "imagecategoria.jpg"));
        product1 = new Product("guitarra","guitarra electrica","imagen1.jpg", 2500.00, category1);
        product1.setId(1l);
        product2 = new Product("violin","violin stradivarius","imagen1.jpg", 100000.00, category1);
        product2.setId(2l);

    }

    @Test
    @DisplayName("Deberia Listar los productos - Service")
    void listarProductos() {
        List<Product> listOfProduct = new ArrayList<>();
        listOfProduct.add(product1);
        listOfProduct.add(product2);

        System.out.println("list product "+listOfProduct);
        Mockito.when(productRepository.findAll()).thenReturn(listOfProduct);

        List<ProductDto> productDtoList = productService.listarProductos();
        System.out.println("list productDTO "+productDtoList);

        assertTrue(!productDtoList.isEmpty(),"No deberia estar vacia la lista retornada");
        assertTrue(productDtoList.size() == 2,"Debe tener 2 elemento");
        assertTrue(productDtoList.get(0).getNombre().equals(product1.getNombre()),"Debe tener el mismo nombre que el primer elemento que guardamos" );
        assertTrue(productDtoList.get(1).getNombre().equals(product2.getNombre()),"Debe tener el mismo nombre que el segundo elemento que guardamos" );
        assertTrue(productDtoList.get(0).getDescripcion().equals(product1.getDescripcion()),"Debe tener la misma descripcion que el primer elemento que guardamos" );
        assertTrue(productDtoList.get(1).getDescripcion().equals(product2.getDescripcion()),"Debe tener la misma descripcion que el segundo elemento que guardamos" );

    }

    @Test
    @DisplayName("Deberia Guardar un producto  - Service")
    void guardarProducto () throws Exception {
        lenient().when(productRepository.save(any(Product.class))).thenReturn(product1);

        ProductDto productoGuardado = productService.guardarProducto(productToProductDto(product1));

        assertTrue(productoGuardado.getNombre().equals(product1.getNombre()));
        assertTrue(productoGuardado.getDescripcion().equals(product1.getDescripcion()));

    }

    @Test
    @DisplayName("Deberia buscar producto x ID - Service")
    void obtenerProductoPorId() throws Exception {


        Mockito.when(productRepository.save(any())).thenReturn(product1);
        productService.guardarProducto(productToProductDto(product1));
        Mockito.when(productRepository.findById(1l)).thenReturn(Optional.of(product1));
        Optional <ProductDto> productoEncontrado = productService.obtenerProductoPorId(1l);

        assertTrue(productoEncontrado.get().getNombre().equals(product1.getNombre()));
    }

    @Test
    @DisplayName("Deberia Eliminar producto x ID - Service")
    void eliminarProducto() throws BadRequestException {

        Mockito.doNothing().when(productRepository).deleteById(1l);

        productService.eliminarProducto(1l);

        verify(productRepository).deleteById(1l);



    }



}