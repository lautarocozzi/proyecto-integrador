package com.mrInstruments.backend.repository;

import com.mrInstruments.backend.entities.Product;
import com.mrInstruments.backend.entities.Reservation;
import com.mrInstruments.backend.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Reservation save(Reservation reservation);
    List<Reservation> findAll();
    void deleteById(Long id);
    Optional<Reservation> findById(Long id);

    @Query(value = "SELECT * FROM pedidos p WHERE p.usuarios_id = :id",
            nativeQuery = true)
    List<Reservation> findAllByUsuarioId(@Param("id") Long id);
}
