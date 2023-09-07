package com.mrInstruments.backend.controller;

import com.mrInstruments.backend.dto.CharacteristicDto;
import com.mrInstruments.backend.dto.ProductDto;
import com.mrInstruments.backend.entities.Product;
import com.mrInstruments.backend.entities.Reservation;
import com.mrInstruments.backend.entities.User;
import com.mrInstruments.backend.exception.BadRequestException;
import com.mrInstruments.backend.exception.ResourceNotFoundException;
import com.mrInstruments.backend.service.ProductService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productos")
public class ProductController {

    @Autowired
    private ProductService productService;

    private static final Logger logger = LogManager.getLogger(ProductController.class);

    @PermitAll
    @GetMapping
    public ResponseEntity<?> listarProductos() throws Exception {
        return ResponseEntity.ok(productService.listarProductos());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> obtenerProductoPorId(@PathVariable Long id) throws Exception {
        Optional<ProductDto> producto = productService.obtenerProductoPorId(id);
        if(producto.isPresent()){
            logger.log(Level.INFO,"Producto con id= " + id + " fue encontrado");
            return ResponseEntity.ok(producto.get());
        }else {
            throw new ResourceNotFoundException("Error al buscar id: "+id+" no se encontró.");
        }
    }

    @PostMapping
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<ProductDto> guardarProducto(@RequestBody ProductDto productDto) throws Exception {
        return ResponseEntity.ok(productService.guardarProducto(productDto));
    }

    @DeleteMapping("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) throws BadRequestException {
        productService.eliminarProducto(id);
        return ResponseEntity.ok("Producto eliminado");
    }

    @PutMapping("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<ProductDto> updateProducto(@PathVariable Long id, @RequestBody ProductDto productDto) throws Exception  {
        return ResponseEntity.ok(productService.updateProducto(id,productDto));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> buscarProductosPorNombre(@PathVariable String nombre) throws Exception {
        List<ProductDto> productos = productService.buscarProductosPorPalabraClave(nombre);
        return ResponseEntity.ok(productos);
    }
}

