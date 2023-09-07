package com.mrInstruments.backend.service;

import com.mrInstruments.backend.dto.CategoryDto;
import com.mrInstruments.backend.entities.Category;
import com.mrInstruments.backend.entities.Product;
import com.mrInstruments.backend.exception.BadRequestException;
import com.mrInstruments.backend.repository.CategoryRepository;
import static com.mrInstruments.backend.utils.UtilsDtoMapper.categoryDtoToCategory;
import static com.mrInstruments.backend.utils.UtilsDtoMapper.categoryToCategoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;
    @InjectMocks
    CategoryService categoryService;

    private CategoryDto cat1;
    private CategoryDto cat2;


    @BeforeEach
    @DisplayName("Cargando Data")
    void setup(){
        cat1 = new CategoryDto(1l,"Cordofonos","vibracion de cuerdas","url-img");
        cat2 = new CategoryDto(2l,"Percusion","decrip","url-img");

    }
    @Test
    @DisplayName("Deberia guardar categoria - Service")
    void createCategory() throws Exception {
        //given
        lenient().when(categoryRepository.save(any(Category.class))).thenReturn(categoryDtoToCategory(new Category(),cat1));
        //when
        CategoryDto categoriaGuardada = categoryService.createCategory(cat2);
        //then
        assertTrue(Optional.of(categoriaGuardada).isPresent());


    }

    @Test
    @DisplayName("Deberia buscar categorias x ID - Service")
    void findCategoryById() throws Exception {
        //given
        given(categoryRepository.findById(cat1.getId())).willReturn(Optional.of(categoryDtoToCategory(new Category(),cat1)));
        //when
       CategoryDto categoriaEncontrado =categoryService.findCategoryById(cat1.getId());
        //then
        assertTrue(Optional.of(categoriaEncontrado).isPresent());

    }

    @Test
    @DisplayName("Deberia eliminar categoria x ID - Service")
    void deleteCategory() throws BadRequestException {
        //given
        willDoNothing().given(categoryRepository).deleteById(cat1.getId());
        //when
        categoryService.deleteCategory(cat1.getId());
        //then
        verify(categoryRepository,timeout(1)).deleteById(cat1.getId());

    }

    @Test
    @DisplayName("Deberia listar categorias - Service")
    void listCategory() throws Exception{
        //given
        given(categoryRepository.findAll()).willReturn(List.of(categoryDtoToCategory(new Category(),cat1),categoryDtoToCategory(new Category(), cat2)));
        //when
        List<CategoryDto> listadoProductos = categoryService.listCategory();
        //then
        assertTrue(listadoProductos.size() == 2);
        assertTrue(Optional.of(listadoProductos).isPresent());
    }

    @Test
    @DisplayName("Deberia actualizar categoria - Service")
    void updateCategory() throws Exception {
        //given
        lenient().when(categoryRepository.save(any(Category.class))).thenReturn(categoryDtoToCategory(new Category(),cat1));
        lenient().when(categoryRepository.findById(cat1.getId())).thenReturn(Optional.of(categoryDtoToCategory(new Category(), cat1)));
        //when
        CategoryDto categoriaGuardada = categoryService.updateCategory(cat1.getId(), cat1);
        //then
        assertTrue(Optional.of(categoriaGuardada).isPresent());

    }
}