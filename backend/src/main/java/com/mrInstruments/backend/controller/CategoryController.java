package com.mrInstruments.backend.controller;

import com.mrInstruments.backend.dto.CategoryDto;
import com.mrInstruments.backend.entities.Category;
import com.mrInstruments.backend.exception.BadRequestException;
import com.mrInstruments.backend.exception.ResourceNotFoundException;
import com.mrInstruments.backend.service.CategoryService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/categorias")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @RolesAllowed("ROLE_ADMIN")
    //se utiliza para aplicar reglas de seguridad en métodos o controladores de una aplicación
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) throws Exception {
        return ResponseEntity.ok(categoryService.createCategory(categoryDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> searchCategory(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok(categoryService.findCategoryById(id));
    }

    @DeleteMapping("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) throws Exception {
        CategoryDto categoria = categoryService.findCategoryById(id);
        categoryService.deleteCategory(categoria.getId());
        return ResponseEntity.ok("Se elimino correctamente la categoria con id: " + id);
    }

    @GetMapping
    public ResponseEntity<?> listCategory() throws BadRequestException {
        return ResponseEntity.ok(categoryService.listCategory());
    }

    @RolesAllowed("ROLE_ADMIN")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) throws Exception {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDto));
    }
}
