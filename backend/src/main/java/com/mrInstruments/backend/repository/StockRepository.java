package com.mrInstruments.backend.repository;

import com.mrInstruments.backend.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    Stock save(Stock stock);
    Optional<Stock> findById(Long id);
    List<Stock> findAll();
    //Optional<Stock> findByCategoria(Categoria categoria);

}
