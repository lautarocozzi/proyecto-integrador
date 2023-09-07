package com.mrInstruments.backend.service;

import com.mrInstruments.backend.dto.ProductDto;
import com.mrInstruments.backend.entities.Category;
import com.mrInstruments.backend.entities.Characteristic;
import com.mrInstruments.backend.entities.Product;
import com.mrInstruments.backend.exception.BadRequestException;
import com.mrInstruments.backend.exception.ResourceNotFoundException;
import com.mrInstruments.backend.repository.CategoryRepository;
import com.mrInstruments.backend.repository.CharacteristicRepository;
import com.mrInstruments.backend.repository.ProductRepository;
import com.mrInstruments.backend.utils.UtilsDtoMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mrInstruments.backend.utils.UtilsDtoMapper.*;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CharacteristicRepository characteristicRepository;
    private final CategoryRepository categoryRepository;


    public ProductService(ProductRepository productRepository, CharacteristicRepository characteristicRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.characteristicRepository = characteristicRepository;
        this.categoryRepository = categoryRepository;
    }

    Logger LOGGER = LogManager.getLogger(ProductService.class);

    public List<ProductDto> listarProductos() {
        List<ProductDto> productListDto = productRepository.findAll()
                .stream()
                .map(UtilsDtoMapper::productToProductDto)
                .collect(Collectors.toList());
        LOGGER.log(Level.INFO,"Listando productos...");
        return productListDto;
    }

    public Optional<ProductDto> obtenerProductoPorId(Long id) throws Exception {
        Optional<Product> productoBuscado = productRepository.findById(id);
        if(productoBuscado.isPresent()){
            Product product = productoBuscado.get();
            return Optional.of(productToProductDto(product));
        }else{
            throw new ResourceNotFoundException("No existe id asociado a un producto en el sistema");
        }
    }

    public ProductDto guardarProducto(ProductDto productDto) throws Exception {
        Set<Characteristic> characteristics =
                productDto.getCaracteristicas()
                .stream()
                .map(charactDto -> {
                    try {
                        return characteristicRepository.findById(charactDto.getId())
                                .orElseThrow(()->new ResourceNotFoundException("No se encontro caracteristica asociada al producto"));
                    } catch (Exception e) {
                        try {
                            throw new BadRequestException(e.getMessage());
                        } catch (BadRequestException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }).collect(Collectors.toSet());
        Product newProduct = productDtoToProduct(new Product(), productDto);
        newProduct.setCharacteristics(characteristics);//seteo caracteristicas
        newProduct.setCategory(categoryDtoToCategory(new Category(), productDto.getCategoria()));//seteo categoria
        newProduct = productRepository.save(newProduct);
        LOGGER.log(Level.INFO, "Producto registrado correctamente");
        return productToProductDto(newProduct);
    }

    public void eliminarProducto(Long id) throws BadRequestException {
        try {
            productRepository.deleteById(id);
            LOGGER.log(Level.INFO, "Producto con id= " + id + " fue eliminado");
        } catch (Exception ex) {
            throw new BadRequestException("Error - no se pudo eliminar el producto");
        }
    }

    public ProductDto updateProducto(Long id, ProductDto productDto) throws Exception {
        if (!productRepository.existsById(id)) {
            try {
                throw new ResourceNotFoundException("Producto no encontrado");
            } catch (ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        productDto.setId(id);
        return guardarProducto(productDto);
    }

    public List<ProductDto> buscarProductosPorPalabraClave(String palabraClave) throws Exception {
        try {
            List<Product> productos = productRepository.findByNombre(palabraClave);
            return productos.stream()
                    .map(UtilsDtoMapper::productToProductDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error al buscar productos.");
        }

    }
}