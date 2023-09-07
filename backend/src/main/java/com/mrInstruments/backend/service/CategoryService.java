package com.mrInstruments.backend.service;

import com.mrInstruments.backend.dto.CategoryDto;
import com.mrInstruments.backend.entities.Category;
import com.mrInstruments.backend.exception.BadRequestException;
import com.mrInstruments.backend.exception.ResourceNotFoundException;
import com.mrInstruments.backend.repository.CategoryRepository;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mrInstruments.backend.utils.UtilsDtoMapper.categoryDtoToCategory;
import static com.mrInstruments.backend.utils.UtilsDtoMapper.categoryToCategoryDto;


@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    Logger LOGGER = LogManager.getLogger(CategoryService.class);

    public CategoryDto createCategory(CategoryDto categoryDto) throws Exception {
        Category newCategory = new Category();
        newCategory = categoryDtoToCategory(newCategory, categoryDto);
        return categoryToCategoryDto(categoryRepository.save(newCategory));
    }

    public CategoryDto findCategoryById(Long id) throws Exception {
        Category categoria = categoryRepository.findById(id).orElseThrow(
                () -> new BadRequestException("Error al buscar id: " + id + " no se encontró."));
        LOGGER.log(Level.INFO, "Categoria con id= " + id + " fue encontrado");
        return categoryToCategoryDto(categoria);
    }

    public void deleteCategory(Long id) throws BadRequestException {
        try{
            categoryRepository.deleteById(id);
            LOGGER.log(Level.INFO, "Categoria con id= " + id + " fue eliminado");
        }catch (Exception ex){
            throw new BadRequestException("Error - no se pudo eliminar la categoria ");
        }
    }

    public List<CategoryDto> listCategory() {
        List<CategoryDto> userListDto = new ArrayList<>();
        userListDto = categoryRepository.findAll()
                .stream()
                .map(category-> categoryToCategoryDto(category))
                .collect(Collectors.toList());
        LOGGER.log(Level.INFO,"Listando categorias...");
        return userListDto;
    }

    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) throws Exception{
        Optional<Long> optionalId = Optional.ofNullable(id);
        Optional<Category> categoriaOptional = categoryRepository.findById(id);
        if (optionalId.isPresent() && optionalId.get() > 0 && categoriaOptional.isPresent()) {
            try {
                Category categoriaEncontrada = categoriaOptional.get();
                categoriaEncontrada = categoryDtoToCategory(categoriaEncontrada, categoryDto);//mapeo la categoria encontrada en la BBDD con la info de el DTO

                return categoryToCategoryDto(categoryRepository.save(categoriaEncontrada));//guardo en BBDD y devuelvo el DTO
            } catch (Exception e) {
                throw new ResourceNotFoundException("Error al actualizar categoria - " + e.getMessage());
            }
        }else throw new ResourceNotFoundException("Id invalido o categoria no encontrada");
    }
}



