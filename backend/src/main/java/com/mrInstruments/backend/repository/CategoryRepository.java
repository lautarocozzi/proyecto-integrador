package com.mrInstruments.backend.repository;

import com.mrInstruments.backend.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>  {
    List<Category> findAll();
    Optional<Category> findById(Long id);
    Category save(Category product);
    void deleteById(Long id);
}
