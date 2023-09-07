package com.mrInstruments.backend.repository;

import com.mrInstruments.backend.entities.Category;
import com.mrInstruments.backend.entities.Characteristic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacteristicRepository extends JpaRepository<Characteristic, Long> {
    List<Characteristic> findAll();
    Optional<Characteristic> findById(Long id);
    Characteristic save(Characteristic product);
    void deleteById(Long id);
}
